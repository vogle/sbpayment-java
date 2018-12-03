package com.vogle.sbpayment;

import com.vogle.sbpayment.client.DealingsType;
import com.vogle.sbpayment.client.SpsClient;
import com.vogle.sbpayment.client.SpsResponseEntity;
import com.vogle.sbpayment.client.request.CardAuthorizeMethod;
import com.vogle.sbpayment.client.request.CardAuthorizeOptions;
import com.vogle.sbpayment.client.request.CardAuthorizeRequest;
import com.vogle.sbpayment.client.request.CardCancelRequest;
import com.vogle.sbpayment.client.request.CardCaptureOptions;
import com.vogle.sbpayment.client.request.CardCaptureRequest;
import com.vogle.sbpayment.client.request.CardCommitRequest;
import com.vogle.sbpayment.client.request.CardInfoDeleteRequest;
import com.vogle.sbpayment.client.request.CardInfoLookupOptions;
import com.vogle.sbpayment.client.request.CardInfoLookupRequest;
import com.vogle.sbpayment.client.request.CardInfoMethod;
import com.vogle.sbpayment.client.request.CardInfoOptions;
import com.vogle.sbpayment.client.request.CardInfoSaveRequest;
import com.vogle.sbpayment.client.request.CardInfoUpdateRequest;
import com.vogle.sbpayment.client.request.CardPartialRefundOptions;
import com.vogle.sbpayment.client.request.CardPartialRefundRequest;
import com.vogle.sbpayment.client.request.CardReauthorizeOptions;
import com.vogle.sbpayment.client.request.CardReauthorizeRequest;
import com.vogle.sbpayment.client.request.CardTranLookupOptions;
import com.vogle.sbpayment.client.request.CardTranLookupRequest;
import com.vogle.sbpayment.client.request.LegacyCardAuthorizeMethod;
import com.vogle.sbpayment.client.request.LegacyCardAuthorizeOptions;
import com.vogle.sbpayment.client.request.LegacyCardAuthorizeRequest;
import com.vogle.sbpayment.client.request.LegacyCardInfoMethod;
import com.vogle.sbpayment.client.request.LegacyCardInfoOptions;
import com.vogle.sbpayment.client.request.LegacyCardInfoSaveRequest;
import com.vogle.sbpayment.client.request.LegacyCardInfoUpdateRequest;
import com.vogle.sbpayment.client.response.CardAuthorizeResponse;
import com.vogle.sbpayment.client.response.CardInfoDeleteResponse;
import com.vogle.sbpayment.client.response.CardInfoLookupResponse;
import com.vogle.sbpayment.client.response.CardInfoSaveResponse;
import com.vogle.sbpayment.client.response.CardInfoUpdateResponse;
import com.vogle.sbpayment.client.response.CardTranLookupResponse;
import com.vogle.sbpayment.client.response.DefaultResponse;
import com.vogle.sbpayment.client.response.LegacyCardInfoSaveResponse;
import com.vogle.sbpayment.client.response.LegacyCardInfoUpdateResponse;
import com.vogle.sbpayment.param.CardInfoResponseType;
import com.vogle.sbpayment.param.PayCreditCard;
import com.vogle.sbpayment.param.PaySavedCard;
import com.vogle.sbpayment.param.PayToken;
import com.vogle.sbpayment.param.PayTrackingInfo;
import com.vogle.sbpayment.param.PaymentInfo;
import com.vogle.sbpayment.param.SaveCardByToken;
import com.vogle.sbpayment.param.SaveCreditCard;

import static com.vogle.sbpayment.RequestMapper.mapItem;

/**
 * Credit Card service implements
 *
 * @author Allan Im
 */
public class DefaultCardService implements CardService {

    protected final SpsClient client;
    protected String returnCustomerInfo = "0";
    protected String returnCardBrand = "0";

    public DefaultCardService(SpsClient client) {
        this.client = client;
    }

    /**
     * When sending customer information, return it from Softbank payment.<br/>
     * 顧客コードを送るとき、ソフトバングペイメントから顧客情報を返却する。
     */
    public DefaultCardService enabledReturnCustomerInfo(boolean enabled) {
        this.returnCustomerInfo = enabled ? "1" : "0";
        return this;
    }

    /**
     * When sending credit-card information, return credit-card brand.<br/>
     * カード情報を送るとき、カードブランド情報を返却する。
     */
    public DefaultCardService enabledReturnCardBrand(boolean enabled) {
        this.returnCardBrand = enabled ? "1" : "0";
        return this;
    }

    @Override
    public SpsResponseEntity<CardAuthorizeResponse> authorize(PaymentInfo paymentInfo, PayToken token) {
        ParamValidator.beanValidate(paymentInfo, token);
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
    public SpsResponseEntity<CardAuthorizeResponse> authorize(PaymentInfo paymentInfo, PaySavedCard savedCard) {
        ParamValidator.beanValidate(paymentInfo, savedCard);
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
    public SpsResponseEntity<CardAuthorizeResponse> authorize(PaymentInfo paymentInfo, PayCreditCard creditCard) {
        ParamValidator.beanValidate(paymentInfo, creditCard);
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
    public SpsResponseEntity<CardAuthorizeResponse> reauthorize(PaymentInfo paymentInfo, PayTrackingInfo trackingInfo) {
        ParamValidator.beanValidate(paymentInfo, trackingInfo);
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
    public SpsResponseEntity<DefaultResponse> commit(String trackingId) {
        ParamValidator.assertNotEmpty("trackingId", trackingId);
        CardCommitRequest request = client.newRequest(CardCommitRequest.class);

        // set transaction information
        request.setTrackingId(trackingId);
        request.setProcessingDatetime(request.getRequestDate());

        return client.execute(request);
    }

    @Override
    public SpsResponseEntity<DefaultResponse> capture(String trackingId) {
        ParamValidator.assertNotEmpty("trackingId", trackingId);
        CardCaptureRequest request = client.newRequest(CardCaptureRequest.class);

        // set transaction information
        request.setTrackingId(trackingId);
        request.setProcessingDatetime(request.getRequestDate());

        return client.execute(request);
    }

    @Override
    public SpsResponseEntity<DefaultResponse> capture(String trackingId, Integer amount) {
        ParamValidator.assertNotEmpty("trackingId", trackingId);
        ParamValidator.assertAmount(amount);
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
    public SpsResponseEntity<DefaultResponse> cancelOrRefund(String trackingId) {
        ParamValidator.assertNotEmpty("trackingId", trackingId);
        CardCancelRequest request = client.newRequest(CardCancelRequest.class);

        // set transaction information
        request.setTrackingId(trackingId);
        request.setProcessingDatetime(request.getRequestDate());

        return client.execute(request);
    }

    @Override
    public SpsResponseEntity<DefaultResponse> refund(String trackingId, Integer amount) {
        ParamValidator.assertNotEmpty("trackingId", trackingId);
        ParamValidator.assertAmount(amount);
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
    public SpsResponseEntity<CardTranLookupResponse> lookup(String trackingId) {
        return lookup(trackingId, CardInfoResponseType.NONE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SpsResponseEntity<CardTranLookupResponse> lookup(String trackingId, CardInfoResponseType type) {
        ParamValidator.assertNotEmpty("trackingId", trackingId);
        ParamValidator.assertNotNull("type", type);

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
    public SpsResponseEntity<CardInfoSaveResponse> saveCard(String customerCode, SaveCardByToken token) {
        ParamValidator.assertNotEmpty("customerCode", customerCode);
        ParamValidator.beanValidate(token);

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
    public SpsResponseEntity<LegacyCardInfoSaveResponse> saveCard(String customerCode, SaveCreditCard creditCard) {
        ParamValidator.assertNotEmpty("customerCode", customerCode);
        ParamValidator.beanValidate(creditCard);

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
    public SpsResponseEntity<CardInfoUpdateResponse> updateCard(String customerCode, SaveCardByToken token) {
        ParamValidator.assertNotEmpty("customerCode", customerCode);
        ParamValidator.beanValidate(token);

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
    public SpsResponseEntity<LegacyCardInfoUpdateResponse> updateCard(String customerCode, SaveCreditCard creditCard) {
        ParamValidator.assertNotEmpty("customerCode", customerCode);
        ParamValidator.beanValidate(creditCard);

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
    public SpsResponseEntity<CardInfoDeleteResponse> deleteCard(String customerCode) {
        ParamValidator.assertNotEmpty("customerCode", customerCode);

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
    public SpsResponseEntity<CardInfoLookupResponse> lookupCard(String customerCode) {
        return lookupCard(customerCode, CardInfoResponseType.NONE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SpsResponseEntity<CardInfoLookupResponse> lookupCard(String customerCode, CardInfoResponseType type) {
        ParamValidator.assertNotEmpty("customerCode", customerCode);
        ParamValidator.assertNotNull("type", type);

        CardInfoLookupRequest request = client.newRequest(CardInfoLookupRequest.class);

        // customer info
        request.setCustCode(customerCode);
        request.setSpsCustInfoReturnFlg(returnCustomerInfo);

        // card response type
        request.setResponseInfoType(type.code());

        // options
        CardInfoLookupOptions options = new CardInfoLookupOptions();
        options.setCardbrandReturnFlg(returnCardBrand);
        request.setPayOptions(options);

        return client.execute(request);
    }
}
