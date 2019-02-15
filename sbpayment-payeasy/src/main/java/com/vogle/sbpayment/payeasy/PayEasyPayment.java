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

package com.vogle.sbpayment.payeasy;

import com.vogle.sbpayment.client.InvalidAccessException;
import com.vogle.sbpayment.client.SpsResult;
import com.vogle.sbpayment.client.params.PaymentInfo;
import com.vogle.sbpayment.payeasy.params.PayEasy;
import com.vogle.sbpayment.payeasy.receivers.PayEasyDepositReceived;
import com.vogle.sbpayment.payeasy.receivers.PayEasyExpiredCancelReceived;
import com.vogle.sbpayment.payeasy.responses.PayEasyPaymentResponse;

/**
 * Pay-Easy Service Payment API<br/>
 * Pay-easy 決済API
 *
 * @author Allan Im
 **/
public interface PayEasyPayment {

    /**
     * ST01-00101-703: Payment by the PayEasy<br/>
     * Pay-easy 決済：決済要求
     *
     * @param paymentInfo The payment information
     * @param payEasy     The PayEasy information
     * @return The responses
     */
    SpsResult<PayEasyPaymentResponse> payment(PaymentInfo paymentInfo, PayEasy payEasy);

    /**
     * NT01-00103-703: Receive the deposit notice<br/>
     * Pay-easy 決済：入金通知
     *
     * @param xml is received request
     * @return The converted object
     * @throws InvalidAccessException is occurred if it is not valid data
     */
    PayEasyDepositReceived receiveDeposit(String xml) throws InvalidAccessException;

    /**
     * Make the successful response for the deposit notice
     *
     * @return The XML
     */
    String successDeposit();

    /**
     * Make the failure response for the deposit notice
     *
     * @param errorMessage add message
     * @return The XML
     */
    String failDeposit(String errorMessage);

    /**
     * NT01-00104-703: Receive the expired cancel notice<br/>
     * Pay-easy 決済：支払期限切れキャンセル通知
     *
     * @param xml is received request
     * @return The converted object
     * @throws InvalidAccessException is occurred if it is not valid data
     */
    PayEasyExpiredCancelReceived receiveExpiredCancel(String xml) throws InvalidAccessException;

    /**
     * Make the successful response for the expired cancel notice
     *
     * @return The XML
     */
    String successExpiredCancel();

    /**
     * Make the failure response for the expired cancel notice
     *
     * @param errorMessage add message
     * @return The XML
     */
    String failExpiredCancel(String errorMessage);
}
