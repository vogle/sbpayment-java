package com.vogle.sbpayment.springboot;

import com.vogle.sbpayment.client.SpsResult;
import com.vogle.sbpayment.client.params.PaymentInfo;
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
import com.vogle.sbpayment.springboot.autoconfigure.SbpaymentProperties;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
    private static final String SESSION_PAYMENT_TYPE = "PAYMENT-TYPE";
    private static final String SESSION_TRACKING_ID = "TRACKING-ID";
    private static final String SESSION_RESULT = "RESULT";

    private final SbpaymentProperties sbpaymentProperties;
    private CreditCardPayment creditCardPayment;
    private PayEasyPayment payEasyPayment;

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

    @GetMapping("/")
    public String checkout(ModelMap modelMap, HttpSession session) {
        session.removeAttribute(SESSION_PAYMENT_TYPE);

        if (creditCardPayment != null) {
            modelMap.addAttribute("hasCreditCard", true);
            modelMap.addAttribute("spsTokenUrl", sbpaymentProperties.getCreditcard().getTokenUrl());
            modelMap.addAttribute("merchantId", sbpaymentProperties.getClient().getMerchantId());
            modelMap.addAttribute("serviceId", sbpaymentProperties.getClient().getServiceId());

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

            saveCreditCardInfo(session, creditCardPayment.authorize(paymentInfo, token));
        } else if ("myCard".equals(request.getParameter("type"))) {
            BySavedCard savedCard = BySavedCard.builder().build();
            saveCreditCardInfo(session, creditCardPayment.authorize(paymentInfo, savedCard));
        } else if ("payeasy".equals(request.getParameter("type"))) {
            BySavedCard savedCard = BySavedCard.builder().build();
            PayEasy payEasy = PayEasy.builder()
                .firstName("太郎").lastName("名前")
                .firstNameKana("タロウ").lastNameKana("ナマエ")
                .tel("08011112222")
                .mail("mail@sample.sample")
                .build();

            session.setAttribute(SESSION_PAYMENT_TYPE, "PAYEASY");
            saveResult(session, payEasyPayment.payment(paymentInfo, payEasy));
        } else {
            throw new IllegalArgumentException("Don't have payment type");
        }

        return UrlBasedViewResolver.REDIRECT_URL_PREFIX.concat("/result");
    }

    @GetMapping("/result")
    public String result(ModelMap modelMap, HttpSession session) {
        String paymentType = (String) session.getAttribute(SESSION_PAYMENT_TYPE);
        ObjectMapper mapper = new ObjectMapper();
        if ("CARD".equals(paymentType)) {
            String trackingId = (String) session.getAttribute(SESSION_TRACKING_ID);
            SpsResult result = (SpsResult) session.getAttribute(SESSION_RESULT);
            modelMap.addAttribute("title", "Credit Card: " + result.getBody().getDescription());
            modelMap.addAttribute("trackingId", trackingId);
            modelMap.addAttribute("status", result.getStatus());
            modelMap.addAttribute("headers", result.getHeaders());
            modelMap.addAttribute("bodyMap", mapper.convertValue(result.getBody(), Map.class));
            modelMap.addAttribute("result", result);
        } else if ("DELETE_CARD".equals(paymentType)) {
            SpsResult result = (SpsResult) session.getAttribute(SESSION_RESULT);
            modelMap.addAttribute("title", "Credit Card: " + result.getBody().getDescription());
            modelMap.addAttribute("headers", result.getHeaders());
            modelMap.addAttribute("bodyMap", mapper.convertValue(result.getBody(), Map.class));
        } else if ("PAYEASY".equals(paymentType)) {
            SpsResult result = (SpsResult) session.getAttribute(SESSION_RESULT);
            modelMap.addAttribute("title", "Credit Card: " + result.getBody().getDescription());
            modelMap.addAttribute("headers", result.getHeaders());
            modelMap.addAttribute("bodyMap", mapper.convertValue(result.getBody(), Map.class));
        } else {
            throw new IllegalStateException("Don't have payment type");
        }
        return "result";
    }

    private void saveCreditCardInfo(HttpSession session, SpsResult result) {
        session.setAttribute(SESSION_PAYMENT_TYPE, "CARD");
        session.setAttribute(SESSION_RESULT, result);

        if (result.isSuccess()) {
            String trackingId = ((CardAuthorizeResponse) result.getBody()).getTrackingId();
            session.setAttribute(SESSION_TRACKING_ID, trackingId);
        }
    }

    private String saveResult(HttpSession session, SpsResult result) {
        session.setAttribute(SESSION_RESULT, result);
        return UrlBasedViewResolver.REDIRECT_URL_PREFIX.concat("/result");
    }

    @GetMapping("/cancel")
    public String cancel(HttpSession session) {
        String trackingId = (String) session.getAttribute(SESSION_TRACKING_ID);
        SpsResult<DefaultResponse> result = creditCardPayment.cancel(trackingId);
        session.removeAttribute(SESSION_TRACKING_ID);
        return saveResult(session, result);
    }

    @GetMapping("/capture")
    public String capture(HttpSession session) {
        String trackingId = (String) session.getAttribute(SESSION_TRACKING_ID);
        SpsResult<DefaultResponse> result = creditCardPayment.capture(trackingId);
        return saveResult(session, result);
    }

    @GetMapping("/delete")
    public String delete(HttpSession session) {
        SpsResult<CardInfoDeleteResponse> result = creditCardPayment.deleteCard(
            SAMPLE_CUSTOMER_CODE);
        session.setAttribute(SESSION_PAYMENT_TYPE, "DELETE_CARD");
        return saveResult(session, result);
    }
}
