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

package com.vogle.sbpayment.creditcard;

import com.vogle.sbpayment.client.SpsResult;
import com.vogle.sbpayment.client.params.PaymentInfo;
import com.vogle.sbpayment.creditcard.params.ByCreditCard;
import com.vogle.sbpayment.creditcard.params.BySavedCard;
import com.vogle.sbpayment.creditcard.params.ByToken;
import com.vogle.sbpayment.creditcard.params.ByTrackingInfo;
import com.vogle.sbpayment.creditcard.params.CardInfoResponseType;
import com.vogle.sbpayment.creditcard.params.SaveCardByToken;
import com.vogle.sbpayment.creditcard.params.SaveCreditCard;
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
 * Credit Card Payment API<br/>
 * クレジットカード決済API
 *
 * @author Allan Im
 **/
public interface CreditCardPayment {

    /**
     * ST01-00131-101: Authorize with token<br/>
     * 決済要求（トークン利用）<br/>
     *
     * @param paymentInfo The payment information
     * @param token       The SaveCardByToken information
     * @return The responses
     */
    SpsResult<CardAuthorizeResponse> authorize(PaymentInfo paymentInfo, ByToken token);

    /**
     * ST01-00131-101: Authorize with saved card<br/>
     * 決済要求（保存カード利用）
     *
     * @param paymentInfo The payment information
     * @param savedCard   The saved card information
     * @return The responses
     */
    SpsResult<CardAuthorizeResponse> authorize(PaymentInfo paymentInfo, BySavedCard savedCard);

    /**
     * ST01-00111-101: Authorize with credit card<br/>
     * Don't use in production environment, indeed it has been removed from sbpayment API.<br/>
     * 決済要求：本サビスは本番環境では使わないでください、実際sbpaymentのAPIから削除されました。
     *
     * @param paymentInfo The payment information
     * @param creditCard  The credit card information
     * @return The responses
     */
    SpsResult<CardAuthorizeResponse> authorize(PaymentInfo paymentInfo, ByCreditCard creditCard);

    /**
     * ST01-00133-101: Reauthorize<br/>
     * 再与信要求
     *
     * @param paymentInfo  The payment information
     * @param trackingInfo The tracking information
     * @return The responses
     */
    SpsResult<CardAuthorizeResponse> reauthorize(PaymentInfo paymentInfo, ByTrackingInfo trackingInfo);

    /**
     * ST02-00101-101: Commit<br/>
     * 確定要求
     *
     * @param trackingId The tracking id
     * @return The responses
     */
    SpsResult<DefaultResponse> commit(String trackingId);

    /**
     * ST02-00201-101: Capture <br/>
     * 売上要求
     *
     * @param trackingId The tracking id
     * @return The responses
     */
    SpsResult<DefaultResponse> capture(String trackingId);

    /**
     * ST02-00201-101: Capture with amount<br/>
     * 部分売上要求
     *
     * @param trackingId The tracking id
     * @param amount     The capturing amount
     * @return The responses
     */
    SpsResult<DefaultResponse> capture(String trackingId, Integer amount);

    /**
     * ST02-00303-101: Cancel<br/>
     * 取消返金要求
     *
     * @param trackingId The tracking id
     * @return The responses
     */
    SpsResult<DefaultResponse> cancel(String trackingId);

    /**
     * ST02-00303-101: Refund<br/>
     * 取消返金要求
     *
     * @param trackingId The tracking id
     * @return The responses
     */
    SpsResult<DefaultResponse> refund(String trackingId);

    /**
     * ST02-00307-101: Refund with amount <br/>
     * 部分返金要求
     *
     * @param trackingId The tracking id
     * @param amount     The refunding amount
     * @return The responses
     */
    SpsResult<DefaultResponse> refund(String trackingId, Integer amount);

    /**
     * MG01-00101-101: Lookup transaction<br/>
     * 決済結果参照要求
     *
     * @param trackingId The tracking id
     * @return The responses
     */
    SpsResult<CardTranLookupResponse> lookup(String trackingId);

    /**
     * Lookup transaction with card information<br/>
     *
     * @param trackingId The tracking id
     * @param type       The showing type of credit card number
     * @return The responses
     */
    SpsResult<CardTranLookupResponse> lookup(String trackingId, CardInfoResponseType type);

    /**
     * MG02-00131-101: Save card
     *
     * @param customerCode The customer code
     * @param token        The token information
     * @return The responses
     */
    SpsResult<CardInfoSaveResponse> saveCard(String customerCode, SaveCardByToken token);

    /**
     * MG02-00101-101: Save card<br/>
     * Don't use in production environment, indeed it has been removed from sbpayment API.<br/>
     * 本サビスは本番環境では使わないでください、実際sbpaymentのAPIから削除されました。
     *
     * @param customerCode The customer code
     * @param creditCard   The saving credit card information
     * @return The responses
     */
    SpsResult<LegacyCardInfoSaveResponse> saveCard(String customerCode, SaveCreditCard creditCard);

    /**
     * MG02-00132-101: Update card
     *
     * @param customerCode The customer code
     * @param token        The token information
     * @return The responses
     */
    SpsResult<CardInfoUpdateResponse> updateCard(String customerCode, SaveCardByToken token);

    /**
     * MG02-00102-101: Update card<br/>
     * Don't use in production environment, indeed it has been removed from sbpayment API.<br/>
     * 本サビスは本番環境では使わないでください、実際sbpaymentのAPIから削除されました。
     *
     * @param customerCode The customer code
     * @param creditCard   The updating credit card information
     * @return The responses
     */
    SpsResult<LegacyCardInfoUpdateResponse> updateCard(String customerCode, SaveCreditCard creditCard);

    /**
     * MG02-00103-101: Delete card
     *
     * @param customerCode The customer code
     * @return The responses
     */
    SpsResult<CardInfoDeleteResponse> deleteCard(String customerCode);

    /**
     * MG02-00104-101: Lookup card
     *
     * @param customerCode The customer code
     * @return The responses
     */
    SpsResult<CardInfoLookupResponse> lookupCard(String customerCode);

    /**
     * MG02-00104-101: Lookup card
     *
     * @param customerCode The customer code
     * @param type         The type of Return card info
     * @return The responses
     */
    SpsResult<CardInfoLookupResponse> lookupCard(String customerCode, CardInfoResponseType type);
}
