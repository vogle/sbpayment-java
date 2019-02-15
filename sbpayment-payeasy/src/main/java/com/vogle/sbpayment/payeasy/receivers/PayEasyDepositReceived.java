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

package com.vogle.sbpayment.payeasy.receivers;

import com.vogle.sbpayment.client.convert.CipherString;
import com.vogle.sbpayment.client.receivers.SpsReceivedData;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

/**
 * PayEasy deposit receivers
 *
 * @author Allan Im
 **/
@Data
@JacksonXmlRootElement(localName = "sps-api-request")
public class PayEasyDepositReceived implements SpsReceivedData {

    @JacksonXmlProperty(isAttribute = true)
    private String id;

    /**
     * マーチャントID
     */
    @JacksonXmlProperty(localName = "merchant_id")
    private String merchantId;

    /**
     * サービスID
     */
    @JacksonXmlProperty(localName = "service_id")
    private String serviceId;

    /**
     * 処理対象SBPS トランザクションID
     */
    @JacksonXmlProperty(localName = "sps_transaction_id")
    private String spsTransactionId;

    /**
     * 処理対象トラッキングID
     */
    @JacksonXmlProperty(localName = "tracking_id")
    private String trackingId;

    /**
     * 処理日時
     */
    @JacksonXmlProperty(localName = "rec_datetime")
    private String recDatetime;

    /**
     * 入金通知情報
     */
    @CipherString
    @JacksonXmlProperty(localName = "pay_method_info")
    private PayEasyDepositInfo depositInfo;

    /**
     * リクエスト日時
     */
    @JacksonXmlProperty(localName = "request_date")
    private String requestDate;

    /**
     * チェックサム
     */
    @JacksonXmlProperty(localName = "sps_hashcode")
    private String spsHashcode;

}
