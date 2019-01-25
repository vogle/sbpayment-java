package com.vogle.sbpayment.creditcard;

import com.vogle.sbpayment.client.SpsClient;
import com.vogle.sbpayment.client.SpsResult;
import com.vogle.sbpayment.client.SpsValidator;
import com.vogle.sbpayment.creditcard.params.ByCreditCard;
import com.vogle.sbpayment.creditcard.params.BySavedCard;
import com.vogle.sbpayment.creditcard.params.ByToken;
import com.vogle.sbpayment.creditcard.params.ByTrackingInfo;
import com.vogle.sbpayment.creditcard.params.CardInfoResponseType;
import com.vogle.sbpayment.creditcard.params.PaymentInfo;
import com.vogle.sbpayment.creditcard.params.SaveCardByToken;
import com.vogle.sbpayment.creditcard.params.SaveCreditCard;
import com.vogle.sbpayment.creditcard.requests.CardAuthorizeMethod;
import com.vogle.sbpayment.creditcard.requests.CardAuthorizeOptions;
import com.vogle.sbpayment.creditcard.requests.CardAuthorizeRequest;
import com.vogle.sbpayment.creditcard.requests.CardCancelRequest;
import com.vogle.sbpayment.creditcard.requests.CardCaptureOptions;
import com.vogle.sbpayment.creditcard.requests.CardCaptureRequest;
import com.vogle.sbpayment.creditcard.requests.CardCommitRequest;
import com.vogle.sbpayment.creditcard.requests.CardInfoDeleteRequest;
import com.vogle.sbpayment.creditcard.requests.CardInfoLookupOptions;
import com.vogle.sbpayment.creditcard.requests.CardInfoLookupRequest;
import com.vogle.sbpayment.creditcard.requests.CardInfoMethod;
import com.vogle.sbpayment.creditcard.requests.CardInfoOptions;
import com.vogle.sbpayment.creditcard.requests.CardInfoSaveRequest;
import com.vogle.sbpayment.creditcard.requests.CardInfoUpdateRequest;
import com.vogle.sbpayment.creditcard.requests.CardPartialRefundOptions;
import com.vogle.sbpayment.creditcard.requests.CardPartialRefundRequest;
import com.vogle.sbpayment.creditcard.requests.CardReauthorizeOptions;
import com.vogle.sbpayment.creditcard.requests.CardReauthorizeRequest;
import com.vogle.sbpayment.creditcard.requests.CardTranLookupOptions;
import com.vogle.sbpayment.creditcard.requests.CardTranLookupRequest;
import com.vogle.sbpayment.creditcard.requests.LegacyCardAuthorizeMethod;
import com.vogle.sbpayment.creditcard.requests.LegacyCardAuthorizeOptions;
import com.vogle.sbpayment.creditcard.requests.LegacyCardAuthorizeRequest;
import com.vogle.sbpayment.creditcard.requests.LegacyCardInfoMethod;
import com.vogle.sbpayment.creditcard.requests.LegacyCardInfoOptions;
import com.vogle.sbpayment.creditcard.requests.LegacyCardInfoSaveRequest;
import com.vogle.sbpayment.creditcard.requests.LegacyCardInfoUpdateRequest;
import com.vogle.sbpayment.creditcard.responses.CardAuthorizeResponse;
import com.vogle.sbpayment.creditcard.responses.CardInfoDeleteResponse;
import com.vogle.sbpayment.creditcard.responses.CardInfoLookupResponse;
import com.vogle.sbpayment.creditcard.responses.CardInfoSaveResponse;
import com.vogle.sbpayment.creditcard.responses.CardInfoUpdateResponse;
import com.vogle.sbpayment.creditcard.responses.CardTranLookupResponse;
import com.vogle.sbpayment.creditcard.responses.DefaultResponse;
import com.vogle.sbpayment.creditcard.responses.LegacyCardInfoSaveResponse;
import com.vogle.sbpayment.creditcard.responses.LegacyCardInfoUpdateResponse;

/**
 * Credit Card service implements
 *
 * @author Allan Im
 */
public class DefaultCreditCardService implements CreditCardService {

    private final SpsClient client;
    private String returnCustomerInfo = "0";
    private String returnCardBrand = "0";

    public DefaultCreditCardService(SpsClient client) {
        this.client = client;
    }

    public DefaultCreditCardService enable(Feature... features) {
        for (Feature feature : features) {
            if (Feature.RETURN_CUSTOMER_INFO.equals(feature)) {
                this.returnCustomerInfo = "1";
            } else if (Feature.RETURN_CARD_BRAND.equals(feature)) {
                this.returnCardBrand = "1";
            }
        }
        return this;
    }

    private CardAuthorizeRequest newCardAuthorizeRequest(PaymentInfo paymentInfo, DealingsType dealingsType,
                                                         String divideTimes) {
        CardAuthorizeRequest request = client.newRequest(CardAuthorizeRequest.class);

        // authorizeExecute info
        request.setPaymentInfo(paymentInfo);
        request.setSpsCustInfoReturnFlg(returnCustomerInfo);

        // method
        request.setPayMethod(new CardAuthorizeMethod(dealingsType, divideTimes));

        return request;
    }

    @Override
    public SpsResult<CardAuthorizeResponse> authorize(PaymentInfo paymentInfo, ByToken token) {
        SpsValidator.beanValidate(paymentInfo, token);

        CardAuthorizeRequest request = newCardAuthorizeRequest(paymentInfo, token.getDealingsType(),
                token.getDivideTimes());

        // options
        CardAuthorizeOptions options = new CardAuthorizeOptions();
        options.setToken(token.getToken());
        options.setTokenKey(token.getTokenKey());
        options.setCustManageFlg(token.getSavingCreditCard());
        options.setCardbrandReturnFlg(returnCardBrand);
        request.setPayOptions(options);

        return client.execute(request);
    }

    @Override
    public SpsResult<CardAuthorizeResponse> authorize(PaymentInfo paymentInfo, BySavedCard savedCard) {
        SpsValidator.beanValidate(paymentInfo, savedCard);

        CardAuthorizeRequest request = newCardAuthorizeRequest(paymentInfo, savedCard.getDealingsType(),
                savedCard.getDivideTimes());

        // options
        CardAuthorizeOptions options = new CardAuthorizeOptions();
        options.setCustManageFlg("0");
        options.setCardbrandReturnFlg(returnCardBrand);
        request.setPayOptions(options);

        return client.execute(request);
    }

    @Override
    public SpsResult<CardAuthorizeResponse> authorize(PaymentInfo paymentInfo, ByCreditCard creditCard) {
        SpsValidator.beanValidate(paymentInfo, creditCard);
        LegacyCardAuthorizeRequest request = client.newRequest(LegacyCardAuthorizeRequest.class);

        // authorizeExecute info
        request.setPaymentInfo(paymentInfo);
        request.setSpsCustInfoReturnFlg(returnCustomerInfo);

        // method
        LegacyCardAuthorizeMethod method = new LegacyCardAuthorizeMethod();
        method.setCreditCard(creditCard);
        request.setPayMethod(method);

        // options
        LegacyCardAuthorizeOptions options = new LegacyCardAuthorizeOptions();
        options.setCustManageFlg(creditCard.getSavingCreditCard());
        options.setCardbrandReturnFlg(returnCardBrand);
        request.setPayOptions(options);

        return client.execute(request);
    }

    @Override
    public SpsResult<CardAuthorizeResponse> reauthorize(PaymentInfo paymentInfo, ByTrackingInfo trackingInfo) {
        SpsValidator.beanValidate(paymentInfo, trackingInfo);
        CardReauthorizeRequest request = client.newRequest(CardReauthorizeRequest.class);

        // tracking id
        request.setTrackingId(trackingInfo.getTrackingId());

        // authorizeExecute info
        request.setPaymentInfo(paymentInfo);
        request.setSpsCustInfoReturnFlg(returnCustomerInfo);

        // method
        CardAuthorizeMethod method = new CardAuthorizeMethod();
        method.setPayMethod(trackingInfo.getDealingsType(), trackingInfo.getDivideTimes());
        method.setResrv1(trackingInfo.getResrv1());
        method.setResrv2(trackingInfo.getResrv2());
        method.setResrv3(trackingInfo.getResrv3());
        request.setPayMethod(method);

        // options
        CardReauthorizeOptions options = new CardReauthorizeOptions();
        options.setCustManageFlg("0");
        options.setCardbrandReturnFlg(returnCardBrand);
        options.setPayInfoControlType("B"); // 前回与信時の情報を使用する
        options.setPayInfoDetailControlType("R"); // 前回与信時の情報を使用する
        request.setPayOptions(options);

        return client.execute(request);
    }

    @Override
    public SpsResult<DefaultResponse> commit(String trackingId) {
        SpsValidator.assertsNotEmpty("trackingId", trackingId);
        CardCommitRequest request = client.newRequest(CardCommitRequest.class);

        // set transaction information
        request.setTrackingId(trackingId);
        request.setProcessingDatetime(request.getRequestDate());

        return client.execute(request);
    }

    @Override
    public SpsResult<DefaultResponse> capture(String trackingId) {
        SpsValidator.assertsNotEmpty("trackingId", trackingId);
        CardCaptureRequest request = client.newRequest(CardCaptureRequest.class);

        // set transaction information
        request.setTrackingId(trackingId);
        request.setProcessingDatetime(request.getRequestDate());

        return client.execute(request);
    }

    @Override
    public SpsResult<DefaultResponse> capture(String trackingId, Integer amount) {
        SpsValidator.assertsNotEmpty("trackingId", trackingId);
        SpsValidator.assertsAmount(amount);
        CardCaptureRequest request = client.newRequest(CardCaptureRequest.class);

        // set transaction information
        request.setTrackingId(trackingId);
        request.setProcessingDatetime(request.getRequestDate());

        // set options
        CardCaptureOptions options = new CardCaptureOptions();
        options.setAmount(amount);
        request.setPayOptions(options);

        return client.execute(request);
    }

    @Override
    public SpsResult<DefaultResponse> cancel(String trackingId) {
        SpsValidator.assertsNotEmpty("trackingId", trackingId);
        CardCancelRequest request = client.newRequest(CardCancelRequest.class);

        // set transaction information
        request.setTrackingId(trackingId);
        request.setProcessingDatetime(request.getRequestDate());

        return client.execute(request);
    }

    @Override
    public SpsResult<DefaultResponse> refund(String trackingId) {
        return cancel(trackingId);
    }

    @Override
    public SpsResult<DefaultResponse> refund(String trackingId, Integer amount) {
        SpsValidator.assertsNotEmpty("trackingId", trackingId);
        SpsValidator.assertsAmount(amount);
        CardPartialRefundRequest request = client.newRequest(CardPartialRefundRequest.class);

        // set transaction information
        request.setTrackingId(trackingId);
        request.setProcessingDatetime(request.getRequestDate());

        // set options
        CardPartialRefundOptions options = new CardPartialRefundOptions();
        options.setAmount(amount);
        request.setPayOptions(options);

        return client.execute(request);
    }

    @Override
    public SpsResult<CardTranLookupResponse> lookup(String trackingId) {
        return lookup(trackingId, CardInfoResponseType.NONE);
    }

    @Override
    public SpsResult<CardTranLookupResponse> lookup(String trackingId, CardInfoResponseType type) {
        SpsValidator.assertsNotEmpty("trackingId", trackingId);
        SpsValidator.assertsNotNull("type", type);

        CardTranLookupRequest request = client.newRequest(CardTranLookupRequest.class);

        // set transaction information
        request.setTrackingId(trackingId);

        // set card pay info type
        request.setResponseInfoType(type.code());

        // set options
        CardTranLookupOptions options = new CardTranLookupOptions();
        options.setCardbrandReturnFlg(returnCardBrand);
        request.setPayOptions(options);

        return client.execute(request);
    }

    @Override
    public SpsResult<CardInfoSaveResponse> saveCard(String customerCode, SaveCardByToken token) {
        SpsValidator.assertsNotEmpty("customerCode", customerCode);
        SpsValidator.beanValidate(token);

        CardInfoSaveRequest request = client.newRequest(CardInfoSaveRequest.class);

        // customer info
        request.setCustCode(customerCode);
        request.setSpsCustInfoReturnFlg(returnCustomerInfo);

        // method
        request.setPayMethod(new CardInfoMethod(token.getResrv1(), token.getResrv2(), token.getResrv3()));

        // options
        request.setPayOptions(new CardInfoOptions(token.getToken(), token.getTokenKey(), returnCardBrand));

        return client.execute(request);
    }

    @Override
    public SpsResult<LegacyCardInfoSaveResponse> saveCard(String customerCode, SaveCreditCard creditCard) {
        SpsValidator.assertsNotEmpty("customerCode", customerCode);
        SpsValidator.beanValidate(creditCard);

        LegacyCardInfoSaveRequest request = client.newRequest(LegacyCardInfoSaveRequest.class);

        // customer info
        request.setCustCode(customerCode);
        request.setSpsCustInfoReturnFlg(returnCustomerInfo);

        // card info
        LegacyCardInfoMethod method = new LegacyCardInfoMethod();
        method.setSaveCreditCard(creditCard);
        request.setPayMethod(method);

        // options
        LegacyCardInfoOptions options = new LegacyCardInfoOptions();
        options.setCardbrandReturnFlg(returnCardBrand);
        request.setPayOptions(options);

        return client.execute(request);
    }

    @Override
    public SpsResult<CardInfoUpdateResponse> updateCard(String customerCode, SaveCardByToken token) {
        SpsValidator.assertsNotEmpty("customerCode", customerCode);
        SpsValidator.beanValidate(token);

        CardInfoUpdateRequest request = client.newRequest(CardInfoUpdateRequest.class);

        // customer info
        request.setCustCode(customerCode);
        request.setSpsCustInfoReturnFlg(returnCustomerInfo);

        // card info
        request.setPayMethod(new CardInfoMethod(token.getResrv1(), token.getResrv2(), token.getResrv3()));

        // options
        request.setPayOptions(new CardInfoOptions(token.getToken(), token.getTokenKey(), returnCardBrand));

        return client.execute(request);
    }

    @Override
    public SpsResult<LegacyCardInfoUpdateResponse> updateCard(String customerCode, SaveCreditCard creditCard) {
        SpsValidator.assertsNotEmpty("customerCode", customerCode);
        SpsValidator.beanValidate(creditCard);

        LegacyCardInfoUpdateRequest request = client.newRequest(LegacyCardInfoUpdateRequest.class);

        // customer info
        request.setCustCode(customerCode);
        request.setSpsCustInfoReturnFlg(returnCustomerInfo);

        // card info
        LegacyCardInfoMethod method = new LegacyCardInfoMethod();
        method.setSaveCreditCard(creditCard);
        request.setPayMethod(method);

        // options
        LegacyCardInfoOptions options = new LegacyCardInfoOptions();
        options.setCardbrandReturnFlg(returnCardBrand);
        request.setPayOptions(options);

        return client.execute(request);
    }

    @Override
    public SpsResult<CardInfoDeleteResponse> deleteCard(String customerCode) {
        SpsValidator.assertsNotEmpty("customerCode", customerCode);

        CardInfoDeleteRequest request = client.newRequest(CardInfoDeleteRequest.class);

        // customer info
        request.setCustCode(customerCode);
        request.setSpsCustInfoReturnFlg(returnCustomerInfo);

        return client.execute(request);
    }

    @Override
    public SpsResult<CardInfoLookupResponse> lookupCard(String customerCode) {
        return lookupCard(customerCode, CardInfoResponseType.NONE);
    }

    @Override
    public SpsResult<CardInfoLookupResponse> lookupCard(String customerCode, CardInfoResponseType type) {
        SpsValidator.assertsNotEmpty("customerCode", customerCode);
        SpsValidator.assertsNotNull("type", type);

        CardInfoLookupRequest request = client.newRequest(CardInfoLookupRequest.class);

        // customer info
        request.setCustCode(customerCode);
        request.setSpsCustInfoReturnFlg(returnCustomerInfo);

        // card responses type
        request.setResponseInfoType(type.code());

        // options
        CardInfoLookupOptions options = new CardInfoLookupOptions();
        options.setCardbrandReturnFlg(returnCardBrand);
        request.setPayOptions(options);

        return client.execute(request);
    }

    public enum Feature {
        /**
         * When sending customer information, return it from Softbank payment.<br/>
         * 顧客コードを送るとき、ソフトバングペイメントから顧客情報を返却する。
         */
        RETURN_CUSTOMER_INFO,

        /**
         * When sending credit-card information, return credit-card brand.<br/>
         * カード情報を送るとき、カードブランド情報を返却する。
         */
        RETURN_CARD_BRAND
    }
}
