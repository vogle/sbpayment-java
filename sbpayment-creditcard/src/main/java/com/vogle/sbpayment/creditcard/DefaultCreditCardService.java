package com.vogle.sbpayment.creditcard;

import com.vogle.sbpayment.client.SpsClient;
import com.vogle.sbpayment.client.SpsResult;
import com.vogle.sbpayment.client.SpsValidator;
import com.vogle.sbpayment.creditcard.params.CardInfoResponseType;
import com.vogle.sbpayment.creditcard.params.PayCreditCard;
import com.vogle.sbpayment.creditcard.params.PaySavedCard;
import com.vogle.sbpayment.creditcard.params.PayToken;
import com.vogle.sbpayment.creditcard.params.PayTrackingInfo;
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

import static com.vogle.sbpayment.creditcard.RequestMapper.mapItem;

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

    /**
     * When sending customer information, return it from Softbank payment.<br/>
     * 顧客コードを送るとき、ソフトバングペイメントから顧客情報を返却する。
     */
    public DefaultCreditCardService enabledReturnCustomerInfo(boolean enabled) {
        this.returnCustomerInfo = enabled ? "1" : "0";
        return this;
    }

    /**
     * When sending credit-card information, return credit-card brand.<br/>
     * カード情報を送るとき、カードブランド情報を返却する。
     */
    public DefaultCreditCardService enabledReturnCardBrand(boolean enabled) {
        this.returnCardBrand = enabled ? "1" : "0";
        return this;
    }

    @Override
    public SpsResult<CardAuthorizeResponse> authorize(PaymentInfo paymentInfo, PayToken token) {
        SpsValidator.beanValidate(paymentInfo, token);
        CardAuthorizeRequest request = client.newRequest(CardAuthorizeRequest.class);

        // authorizeExecute info
        request.setCustCode(paymentInfo.getCustomerCode());
        request.setOrderId(paymentInfo.getOrderId());
        request.setItemId(paymentInfo.getItemId());
        request.setItemName(paymentInfo.getItemName());
        request.setTax(paymentInfo.getTax());
        request.setAmount(paymentInfo.getAmount());
        request.setFree1(paymentInfo.getFree1());
        request.setFree2(paymentInfo.getFree2());
        request.setFree3(paymentInfo.getFree3());
        request.setOrderRowno(paymentInfo.getOrderRowNo());
        request.setSpsCustInfoReturnFlg(returnCustomerInfo);

        // item details
        request.setPayDetails(mapItem(paymentInfo.getItems()));

        // method
        CardAuthorizeMethod method = new CardAuthorizeMethod();
        if (token.getDealingsType() != null) {
            method.setDealingsType(token.getDealingsType().code());
            if (DealingsType.INSTALLMENT.equals(token.getDealingsType())) {
                method.setDivideTimes(token.getDivideTimes());
            }
        }
        request.setPayMethod(method);

        // options
        CardAuthorizeOptions options = new CardAuthorizeOptions();
        options.setToken(token.getToken());
        options.setTokenKey(token.getTokenKey());
        options.setCustManageFlg(token.getSaveCreditCard());
        options.setCardbrandReturnFlg(returnCardBrand);
        request.setPayOptions(options);

        return client.execute(request);
    }

    @Override
    public SpsResult<CardAuthorizeResponse> authorize(PaymentInfo paymentInfo, PaySavedCard savedCard) {
        SpsValidator.beanValidate(paymentInfo, savedCard);
        CardAuthorizeRequest request = client.newRequest(CardAuthorizeRequest.class);

        // authorizeExecute info
        request.setCustCode(paymentInfo.getCustomerCode());
        request.setOrderId(paymentInfo.getOrderId());
        request.setItemId(paymentInfo.getItemId());
        request.setItemName(paymentInfo.getItemName());
        request.setTax(paymentInfo.getTax());
        request.setAmount(paymentInfo.getAmount());
        request.setFree1(paymentInfo.getFree1());
        request.setFree2(paymentInfo.getFree2());
        request.setFree3(paymentInfo.getFree3());
        request.setOrderRowno(paymentInfo.getOrderRowNo());
        request.setSpsCustInfoReturnFlg(returnCustomerInfo);

        // item details
        request.setPayDetails(mapItem(paymentInfo.getItems()));

        // method
        CardAuthorizeMethod method = new CardAuthorizeMethod();
        if (savedCard.getDealingsType() != null) {
            method.setDealingsType(savedCard.getDealingsType().code());
            if (DealingsType.INSTALLMENT.equals(savedCard.getDealingsType())) {
                method.setDivideTimes(savedCard.getDivideTimes());
            }
        }
        request.setPayMethod(method);

        // options
        CardAuthorizeOptions options = new CardAuthorizeOptions();
        options.setCustManageFlg("0");
        options.setCardbrandReturnFlg(returnCardBrand);
        request.setPayOptions(options);

        return client.execute(request);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SpsResult<CardAuthorizeResponse> authorize(PaymentInfo paymentInfo, PayCreditCard creditCard) {
        SpsValidator.beanValidate(paymentInfo, creditCard);
        LegacyCardAuthorizeRequest request = client.newRequest(LegacyCardAuthorizeRequest.class);

        // authorizeExecute info
        request.setCustCode(paymentInfo.getCustomerCode());
        request.setOrderId(paymentInfo.getOrderId());
        request.setItemId(paymentInfo.getItemId());
        request.setItemName(paymentInfo.getItemName());
        request.setTax(paymentInfo.getTax());
        request.setAmount(paymentInfo.getAmount());
        request.setFree1(paymentInfo.getFree1());
        request.setFree2(paymentInfo.getFree2());
        request.setFree3(paymentInfo.getFree3());
        request.setOrderRowno(paymentInfo.getOrderRowNo());
        request.setSpsCustInfoReturnFlg(returnCustomerInfo);

        // item details
        request.setPayDetails(mapItem(paymentInfo.getItems()));

        // method
        LegacyCardAuthorizeMethod method = new LegacyCardAuthorizeMethod();
        method.setCcNumber(creditCard.getNumber());
        method.setCcExpiration(creditCard.getExpiration());
        method.setSecurityCode(creditCard.getSecurityCode());
        if (creditCard.getDealingsType() != null) {
            method.setDealingsType(creditCard.getDealingsType().code());
            if (DealingsType.INSTALLMENT.equals(creditCard.getDealingsType())) {
                method.setDivideTimes(creditCard.getDivideTimes());
            }
        }
        request.setPayMethod(method);

        // options
        LegacyCardAuthorizeOptions options = new LegacyCardAuthorizeOptions();
        options.setCustManageFlg(creditCard.getToSaveCreditCard());
        options.setCardbrandReturnFlg(returnCardBrand);
        request.setPayOptions(options);

        return client.execute(request);
    }

    @Override
    public SpsResult<CardAuthorizeResponse> reauthorize(PaymentInfo paymentInfo, PayTrackingInfo trackingInfo) {
        SpsValidator.beanValidate(paymentInfo, trackingInfo);
        CardReauthorizeRequest request = client.newRequest(CardReauthorizeRequest.class);

        // tracking id
        request.setTrackingId(trackingInfo.getTrackingId());

        // authorizeExecute info
        request.setCustCode(paymentInfo.getCustomerCode());
        request.setOrderId(paymentInfo.getOrderId());
        request.setItemId(paymentInfo.getItemId());
        request.setItemName(paymentInfo.getItemName());
        request.setTax(paymentInfo.getTax());
        request.setAmount(paymentInfo.getAmount());
        request.setFree1(paymentInfo.getFree1());
        request.setFree2(paymentInfo.getFree2());
        request.setFree3(paymentInfo.getFree3());
        request.setOrderRowno(paymentInfo.getOrderRowNo());
        request.setSpsCustInfoReturnFlg(returnCustomerInfo);

        // item details
        request.setPayDetails(mapItem(paymentInfo.getItems()));

        // method
        if (trackingInfo.getDealingsType() != null) {
            CardAuthorizeMethod method = new CardAuthorizeMethod();
            method.setDealingsType(trackingInfo.getDealingsType().code());
            if (DealingsType.INSTALLMENT.equals(trackingInfo.getDealingsType())) {
                method.setDivideTimes(trackingInfo.getDivideTimes());
            }
            request.setPayMethod(method);
        }

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

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public SpsResult<CardTranLookupResponse> lookup(String trackingId) {
        return lookup(trackingId, CardInfoResponseType.NONE);
    }

    /**
     * {@inheritDoc}
     */
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
        CardInfoMethod method = new CardInfoMethod();
        method.setResrv1(token.getResrv1());
        method.setResrv2(token.getResrv2());
        method.setResrv3(token.getResrv3());
        request.setPayMethod(method);

        // options
        CardInfoOptions options = new CardInfoOptions();
        options.setToken(token.getToken());
        options.setTokenKey(token.getTokenKey());
        options.setCardbrandReturnFlg(returnCardBrand);
        request.setPayOptions(options);

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
        method.setCcNumber(creditCard.getNumber());
        method.setCcExpiration(creditCard.getExpiration());
        method.setSecurityCode(creditCard.getSecurityCode());
        method.setResrv1(creditCard.getResrv1());
        method.setResrv2(creditCard.getResrv2());
        method.setResrv3(creditCard.getResrv3());
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
        CardInfoMethod method = new CardInfoMethod();
        method.setResrv1(token.getResrv1());
        method.setResrv2(token.getResrv2());
        method.setResrv3(token.getResrv3());
        request.setPayMethod(method);

        // options
        CardInfoOptions options = new CardInfoOptions();
        options.setToken(token.getToken());
        options.setTokenKey(token.getTokenKey());
        options.setCardbrandReturnFlg(returnCardBrand);
        request.setPayOptions(options);

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
        method.setCcNumber(creditCard.getNumber());
        method.setCcExpiration(creditCard.getExpiration());
        method.setSecurityCode(creditCard.getSecurityCode());
        method.setResrv1(creditCard.getResrv1());
        method.setResrv2(creditCard.getResrv2());
        method.setResrv3(creditCard.getResrv3());
        request.setPayMethod(method);

        // options
        LegacyCardInfoOptions options = new LegacyCardInfoOptions();
        options.setCardbrandReturnFlg(returnCardBrand);
        request.setPayOptions(options);

        return client.execute(request);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SpsResult<CardInfoDeleteResponse> deleteCard(String customerCode) {
        SpsValidator.assertsNotEmpty("customerCode", customerCode);

        CardInfoDeleteRequest request = client.newRequest(CardInfoDeleteRequest.class);

        // customer info
        request.setCustCode(customerCode);
        request.setSpsCustInfoReturnFlg(returnCustomerInfo);

        return client.execute(request);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SpsResult<CardInfoLookupResponse> lookupCard(String customerCode) {
        return lookupCard(customerCode, CardInfoResponseType.NONE);
    }

    /**
     * {@inheritDoc}
     */
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
}
