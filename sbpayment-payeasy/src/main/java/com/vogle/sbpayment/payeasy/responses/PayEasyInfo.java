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

package com.vogle.sbpayment.payeasy.responses;

import com.vogle.sbpayment.client.convert.CipherString;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

/**
 * PayEasy Information
 *
 * @author Allan Im
 **/
@Data
public class PayEasyInfo {

    @CipherString
    @JacksonXmlProperty(localName = "invoice_no")
    private String invoiceNo;

    @CipherString
    @JacksonXmlProperty(localName = "bill_date")
    private String billDate;

    @CipherString
    @JacksonXmlProperty(localName = "skno")
    private String skno;

    @CipherString
    @JacksonXmlProperty(localName = "cust_number")
    private String custNumber;

    @CipherString
    @JacksonXmlProperty(localName = "bank_form")
    private String bankForm;

    @CipherString
    @JacksonXmlProperty(localName = "bptn")
    private String bptn;

    @CipherString
    @JacksonXmlProperty(localName = "bill")
    private String bill;

}
