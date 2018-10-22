package com.vogle.sbpayment;

import com.vogle.sbpayment.client.SpsResponseEntity;
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

/**
 * Credit Card Payment API<br/>
 * クレジットカード決済API
 *
 * @author Allan Im
 **/
public interface CardService {

    /**
     * Authorize with token<br/>
     * 決済要求（トークン利用）
     *
     * @param paymentInfo The payment information
     * @param token       The SaveCardByToken information
     * @return The response
     */
    SpsResponseEntity<CardAuthorizeResponse> authorize(PaymentInfo paymentInfo, PayToken token);

    /**
     * Authorize with saved card<br/>
     * 決済要求（保存カード利用）
     *
     * @param paymentInfo The payment information
     * @param savedCard   The saved card information
     * @return The response
     */
    SpsResponseEntity<CardAuthorizeResponse> authorize(PaymentInfo paymentInfo, PaySavedCard savedCard);

    /**
     * Authorize with credit card<br/>
     * Don't use in production environment, indeed it has been removed from sbpayment API.<br/>
     * 決済要求：本サビスは本番環境では使わないでください、実際sbpaymentのAPIから削除されました。
     *
     * @param paymentInfo The payment information
     * @param creditCard  The credit card information
     * @return The response
     */
    SpsResponseEntity<CardAuthorizeResponse> authorize(PaymentInfo paymentInfo, PayCreditCard creditCard);

    /**
     * Reauthorize<br/>
     * 再与信要求
     *
     * @param paymentInfo  The payment information
     * @param trackingInfo The tracking information
     * @return The response
     */
    SpsResponseEntity<CardAuthorizeResponse> reauthorize(PaymentInfo paymentInfo, PayTrackingInfo trackingInfo);

    /**
     * Commit<br/>
     * 確定要求
     *
     * @param trackingId The tracking id
     * @return The response
     */
    SpsResponseEntity<DefaultResponse> commit(String trackingId);

    /**
     * Capture <br/>
     * 売上要求
     *
     * @param trackingId The tracking id
     * @return The response
     */
    SpsResponseEntity<DefaultResponse> capture(String trackingId);

    /**
     * Capture with amount<br/>
     * 部分売上要求
     *
     * @param trackingId The tracking id
     * @param amount     The capturing amount
     * @return The response
     */
    SpsResponseEntity<DefaultResponse> capture(String trackingId, Integer amount);

    /**
     * Cancel or Refund<br/>
     * 取消返金要求
     *
     * @param trackingId The tracking id
     * @return The response
     */
    SpsResponseEntity<DefaultResponse> cancelOrRefund(String trackingId);

    /**
     * Refund with amount <br/>
     * 部分返金要求
     *
     * @param trackingId The tracking id
     * @param amount     The refunding amount
     * @return The response
     */
    SpsResponseEntity<DefaultResponse> refund(String trackingId, Integer amount);

    /**
     * Lookup transaction<br/>
     * 決済結果参照要求
     *
     * @param trackingId The tracking id
     * @return The response
     */
    SpsResponseEntity<CardTranLookupResponse> lookup(String trackingId);

    /**
     * Lookup transaction with card information<br/>
     *
     * @param trackingId The tracking id
     * @param type       The showing type of credit card number
     * @return The response
     */
    SpsResponseEntity<CardTranLookupResponse> lookup(String trackingId, CardInfoResponseType type);

    /**
     * Save card
     *
     * @param customerCode The customer code
     * @param token        The token information
     * @return The response
     */
    SpsResponseEntity<CardInfoSaveResponse> saveCard(String customerCode, SaveCardByToken token);

    /**
     * Save card
     * Don't use in production environment, indeed it has been removed from sbpayment API.<br/>
     * 本サビスは本番環境では使わないでください、実際sbpaymentのAPIから削除されました。
     *
     * @param customerCode The customer code
     * @param creditCard   The saving credit card information
     * @return The response
     */
    SpsResponseEntity<LegacyCardInfoSaveResponse> saveCard(String customerCode, SaveCreditCard creditCard);

    /**
     * Update card
     *
     * @param customerCode The customer code
     * @param token        The token information
     * @return The response
     */
    SpsResponseEntity<CardInfoUpdateResponse> updateCard(String customerCode, SaveCardByToken token);

    /**
     * Update card
     * Don't use in production environment, indeed it has been removed from sbpayment API.<br/>
     * 本サビスは本番環境では使わないでください、実際sbpaymentのAPIから削除されました。
     *
     * @param customerCode The customer code
     * @param creditCard   The updating credit card information
     * @return The response
     */
    SpsResponseEntity<LegacyCardInfoUpdateResponse> updateCard(String customerCode, SaveCreditCard creditCard);

    /**
     * Delete card
     *
     * @param customerCode The customer code
     * @return The response
     */
    SpsResponseEntity<CardInfoDeleteResponse> deleteCard(String customerCode);

    /**
     * Lookup card
     *
     * @param customerCode The customer code
     * @return The response
     */
    SpsResponseEntity<CardInfoLookupResponse> lookupCard(String customerCode);

    /**
     * Lookup card
     *
     * @param customerCode The customer code
     * @param type         The type of Return card info
     * @return The response
     */
    SpsResponseEntity<CardInfoLookupResponse> lookupCard(String customerCode, CardInfoResponseType type);
}
