package com.vogle.sbpayment.springboot;

import com.vogle.sbpayment.client.SpsResult;
import com.vogle.sbpayment.client.params.PaymentInfo;
import com.vogle.sbpayment.creditcard.CreditCardPayment;
import com.vogle.sbpayment.creditcard.params.ByToken;
import com.vogle.sbpayment.creditcard.responses.CardAuthorizeResponse;
import com.vogle.sbpayment.creditcard.responses.CardInfoLookupMethodInfo;
import com.vogle.sbpayment.creditcard.responses.CardInfoLookupResponse;
import com.vogle.sbpayment.creditcard.responses.CardTranLookupResponse;
import com.vogle.sbpayment.payeasy.PayEasyPayment;
import com.vogle.sbpayment.springboot.autoconfigure.SbpaymentProperties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

import com.fasterxml.jackson.databind.ObjectMapper;

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

    private PaymentInfo makeSamplePaymentInfo() {
        return PaymentInfo.builder()
                .orderId(UUID.randomUUID().toString())
                .customerCode(SAMPLE_CUSTOMER_CODE)
                .amount(1080)
                .tax(80)
                .itemId(UUID.randomUUID().toString().replace("-", ""))
                .build();

    }

    @GetMapping("/")
    public String checkout(ModelMap modelMap, HttpServletRequest request) {
        if (creditCardPayment != null) {
            modelMap.addAttribute("hasCreditCard", true);
            modelMap.addAttribute("spsTokenUrl", sbpaymentProperties.getCreditcard().getTokenUrl());
            modelMap.addAttribute("merchantId", sbpaymentProperties.getClient().getMerchantId());
            modelMap.addAttribute("serviceId", sbpaymentProperties.getClient().getServiceId());

            SpsResult<CardInfoLookupResponse> cardInfo = creditCardPayment.lookupCard(SAMPLE_CUSTOMER_CODE);
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
            ByToken token = ByToken.builder()
                    .token(paramToken)
                    .tokenKey(paramTokenKey)
                    .build();
            SpsResult<CardAuthorizeResponse> result = creditCardPayment.authorize(paymentInfo, token);

            if (result.isSuccess()) {
                String trackingId = result.getBody().getTrackingId();
                session.setAttribute(SESSION_PAYMENT_TYPE, "CARD");
                session.setAttribute(SESSION_TRACKING_ID, trackingId);
            } else {
                throw new IllegalStateException(result.getBody().getErrCode());
            }
        } else {
            throw new IllegalArgumentException("Don't have payment type");
        }

        return UrlBasedViewResolver.REDIRECT_URL_PREFIX.concat("/complete");
    }

    @GetMapping("/complete")
    public String complete(ModelMap modelMap, HttpSession session) {
        String paymentType = (String) session.getAttribute(SESSION_PAYMENT_TYPE);
        ObjectMapper mapper = new ObjectMapper();
        if ("CARD".equals(paymentType)) {
            String trackingId = (String) session.getAttribute(SESSION_TRACKING_ID);
            SpsResult<CardTranLookupResponse> result = creditCardPayment.lookup(trackingId);
            modelMap.addAttribute("title", "Credit Card");
            modelMap.addAttribute("trackingId", trackingId);
            modelMap.addAttribute("status", result.getStatus());
            modelMap.addAttribute("headers", result.getHeaders());
            modelMap.addAttribute("bodyMap", mapper.convertValue(result.getBody(), Map.class));
            modelMap.addAttribute("result", result);


        } else {
            throw new IllegalStateException("Don't have payment type");
        }
        return "complete";
    }
}
