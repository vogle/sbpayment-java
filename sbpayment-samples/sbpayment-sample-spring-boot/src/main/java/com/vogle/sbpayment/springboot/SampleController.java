/*
 * Copyright 2019 Vogle Labs.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.vogle.sbpayment.springboot;

import com.vogle.sbpayment.client.InvalidAccessException;
import com.vogle.sbpayment.client.SpsResult;
import com.vogle.sbpayment.client.params.PaymentInfo;
import com.vogle.sbpayment.client.responses.SpsResponse;
import com.vogle.sbpayment.creditcard.CreditCardPayment;
import com.vogle.sbpayment.creditcard.params.BySavedCard;
import com.vogle.sbpayment.creditcard.params.ByToken;
import com.vogle.sbpayment.creditcard.params.CardInfoResponseType;
import com.vogle.sbpayment.creditcard.responses.CardAuthorizeResponse;
import com.vogle.sbpayment.creditcard.responses.CardInfoDeleteResponse;
import com.vogle.sbpayment.creditcard.responses.CardInfoLookupMethodInfo;
import com.vogle.sbpayment.creditcard.responses.CardInfoLookupResponse;
import com.vogle.sbpayment.creditcard.responses.DefaultResponse;
import com.vogle.sbpayment.payeasy.PayEasyPayment;
import com.vogle.sbpayment.payeasy.params.PayEasy;
import com.vogle.sbpayment.payeasy.receivers.PayEasyDepositReceived;
import com.vogle.sbpayment.payeasy.receivers.PayEasyExpiredCancelReceived;
import com.vogle.sbpayment.payeasy.responses.PayEasyPaymentResponse;
import com.vogle.sbpayment.springboot.autoconfigure.SbpaymentProperties;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Payment controller
 *
 * @author Allan Im
 */
@Controller
public class SampleController {

    private static final String SAMPLE_CUSTOMER_CODE = "SAMPLE_01";
    private static final String SESSION_TRACKING_ID = "TRACKING-ID";
    private static final String SESSION_RESULT = "RESULT";

    private final SbpaymentProperties sbpaymentProperties;
    private CreditCardPayment creditCardPayment;
    private PayEasyPayment payEasyPayment;

    private ObjectMapper mapper = new ObjectMapper();

    /**
     * Sample constructor
     */
    public SampleController(SbpaymentProperties sbpaymentProperties) {
        this.sbpaymentProperties = sbpaymentProperties;
    }

    @Autowired(required = false)
    public void setCreditCardPayment(CreditCardPayment creditCardPayment) {
        this.creditCardPayment = creditCardPayment;
    }

    @Autowired(required = false)
    public void setPayEasyPayment(PayEasyPayment payEasyPayment) {
        this.payEasyPayment = payEasyPayment;
    }

    /**
     * Checkout
     */
    @GetMapping("/")
    public String checkout(ModelMap modelMap) {
        if (creditCardPayment != null) {
            modelMap.addAttribute("hasCreditCard", true);
            modelMap.addAttribute("spsTokenUrl", sbpaymentProperties.getCreditcard().getTokenUrl());
            modelMap.addAttribute("merchantId", sbpaymentProperties.getClient().getMerchantId());
            modelMap.addAttribute("serviceId", sbpaymentProperties.getClient().getServiceId());

            // saved card information
            SpsResult<CardInfoLookupResponse> cardInfo = creditCardPayment.lookupCard(
                SAMPLE_CUSTOMER_CODE, CardInfoResponseType.LOWER4);
            CardInfoLookupMethodInfo methodInfo = cardInfo.getBody().getPayMethodInfo();
            modelMap.addAttribute("hasSavedCard", methodInfo != null);
            modelMap.addAttribute("savedCard", methodInfo);
        }

        if (payEasyPayment != null) {
            modelMap.addAttribute("hasPayEasy", true);
        }
        return "checkout";
    }

    /**
     * Result
     */
    @GetMapping("/result")
    public String result(ModelMap modelMap, HttpSession session) {
        SpsResult result = (SpsResult) session.getAttribute(SESSION_RESULT);
        Assert.notNull(result, "Don't have result information");

        if (result.isSuccessfulConnection()) {
            modelMap.addAttribute("title", result.getBody().getDescription());
        } else {
            modelMap.addAttribute("title", "Fail: " + result.getStatus());
        }

        // check tracking ID
        String trackingId = (String) session.getAttribute(SESSION_TRACKING_ID);
        if (!StringUtils.isEmpty(trackingId)) {
            modelMap.addAttribute("hasTrackingId", true);
        }

        SpsResponse response = result.getBody();
        if (response instanceof PayEasyPaymentResponse) {
            String custNumber = ((PayEasyPaymentResponse) response).getPayEasyInfo().getCustNumber();
            modelMap.addAttribute("custNumberUrl", custNumber);
        }

        modelMap.addAttribute("headers", result.getHeaders());
        modelMap.addAttribute("bodyMap", mapper.convertValue(result.getBody(), Map.class));

        return "result";
    }

    private String saveCreditCardResult(HttpSession session, SpsResult result) {
        if (result.isSuccess()) {
            String trackingId = ((CardAuthorizeResponse) result.getBody()).getTrackingId();
            session.setAttribute(SESSION_TRACKING_ID, trackingId);
        }

        return saveResult(session, result);
    }

    private String saveResult(HttpSession session, SpsResult result) {
        session.setAttribute(SESSION_RESULT, result);
        return UrlBasedViewResolver.REDIRECT_URL_PREFIX.concat("/result");
    }

    /**
     * Payment Sample
     */
    @PostMapping("/payment")
    public String payment(HttpServletRequest request, HttpSession session) {

        PaymentInfo paymentInfo = PaymentInfo.builder()
            .orderId(UUID.randomUUID().toString())
            .customerCode(SAMPLE_CUSTOMER_CODE)
            .amount(1080)
            .tax(80)
            .itemId(UUID.randomUUID().toString().replace("-", ""))
            .build();

        if ("newCard".equals(request.getParameter("type"))) {
            String paramToken = request.getParameter("token");
            String paramTokenKey = request.getParameter("tokenKey");
            boolean doSave = Boolean.valueOf(request.getParameter("isSaveCard"));
            ByToken token = ByToken.builder()
                .token(paramToken)
                .tokenKey(paramTokenKey)
                .savingCreditCard(doSave)
                .build();
            return saveCreditCardResult(session, creditCardPayment.authorize(paymentInfo, token));

        } else if ("myCard".equals(request.getParameter("type"))) {
            BySavedCard savedCard = BySavedCard.builder().build();
            return saveCreditCardResult(session, creditCardPayment.authorize(paymentInfo, savedCard));

        } else if ("payeasy".equals(request.getParameter("type"))) {
            PayEasy payEasy = PayEasy.builder()
                .firstName("太郎").lastName("名前")
                .firstNameKana("タロウ").lastNameKana("ナマエ")
                .tel("08011112222")
                .mail("mail@sample.sample")
                .build();

            return saveResult(session, payEasyPayment.payment(paymentInfo, payEasy));

        } else {
            throw new IllegalStateException("Don't have the payment type");
        }
    }

    /**
     * Cancel Credit-card
     */
    @GetMapping("/cancel")
    public String cancel(HttpSession session) {
        String trackingId = (String) session.getAttribute(SESSION_TRACKING_ID);
        Assert.hasText(trackingId, "Don't have the trackingId");

        SpsResult<DefaultResponse> result = creditCardPayment.cancel(trackingId);
        session.removeAttribute(SESSION_TRACKING_ID);
        return saveResult(session, result);
    }

    /**
     * Capture Credit-card
     */
    @GetMapping("/capture")
    public String capture(HttpSession session) {
        String trackingId = (String) session.getAttribute(SESSION_TRACKING_ID);
        Assert.hasText(trackingId, "Don't have the trackingId");

        SpsResult<DefaultResponse> result = creditCardPayment.capture(trackingId);
        return saveResult(session, result);
    }

    /**
     * Delete saved-credit-card
     */
    @GetMapping("/delete")
    public String delete(HttpSession session) {
        SpsResult<CardInfoDeleteResponse> result = creditCardPayment.deleteCard(SAMPLE_CUSTOMER_CODE);
        return saveResult(session, result);
    }

    /**
     * Receive the deposit for pay-easy
     */
    @GetMapping("/deposit")
    public ResponseEntity receiveDeposit(@RequestBody String body) {
        try {
            PayEasyDepositReceived depositReceived = payEasyPayment.receiveDeposit(body);
            // some check data
            String trackingId = depositReceived.getTrackingId();
            String errorMsg = "";
            if (trackingId == null) {
                errorMsg = "Don't have order";
            }

            if (StringUtils.isEmpty(errorMsg)) {
                // some success process
                return ResponseEntity.ok(payEasyPayment.successDeposit());
            } else {
                return ResponseEntity.ok(payEasyPayment.failDeposit(errorMsg));
            }
        } catch (InvalidAccessException e) {
            return ResponseEntity.ok(payEasyPayment.failDeposit(e.getMessage()));
        }
    }

    /**
     * Receive the expired cancel for pay-easy
     */
    @GetMapping("/expired-cancel")
    public ResponseEntity receiveExpiredCancel(@RequestBody String body) {
        try {
            PayEasyExpiredCancelReceived cancelReceived = payEasyPayment.receiveExpiredCancel(body);
            // some check data
            String trackingId = cancelReceived.getTrackingId();
            String errorMsg = "";
            if (trackingId == null) {
                errorMsg = "Don't have order";
            }

            if (StringUtils.isEmpty(errorMsg)) {
                // some cancel process
                return ResponseEntity.ok(payEasyPayment.successExpiredCancel());
            } else {
                return ResponseEntity.ok(payEasyPayment.failExpiredCancel(errorMsg));
            }
        } catch (InvalidAccessException e) {
            return ResponseEntity.ok(payEasyPayment.failExpiredCancel(e.getMessage()));
        }
    }
}
