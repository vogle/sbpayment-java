/*
 * Copyright 2019 VOGLE Labs.
 *
 * This file is part of sbpayment-java - Sbpayment client.
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

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

/**
 * Deposit information<br/>
 * 入金通知情報
 *
 * @author Allan Im
 **/
@Data
public class PayEasyDepositInfo {

    /**
     * 入金種別
     * <pre>
     * 1：速報
     * 2：速報取消
     * 3：確報
     * 4：確報取消
     * </pre>
     */
    @CipherString
    @JacksonXmlProperty(localName = "rec_type")
    private String type;

    /**
     * 入金された金額
     */
    @CipherString
    @JacksonXmlProperty(localName = "rec_amount")
    private String amount;

    /**
     * 入金された累計金額
     */
    @CipherString
    @JacksonXmlProperty(localName = "rec_amountTotal")
    private String amountTotal;

    /**
     * 購入した際に設定したメールアドレス
     */
    @CipherString
    @JacksonXmlProperty(localName = "rec_mail")
    private String mail;

    /**
     * 備考
     */
    @CipherString
    @JacksonXmlProperty(localName = "rec_extra")
    private String extra;
}
