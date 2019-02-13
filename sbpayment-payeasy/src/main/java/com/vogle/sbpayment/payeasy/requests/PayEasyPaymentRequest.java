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

package com.vogle.sbpayment.payeasy.requests;

import com.vogle.sbpayment.client.convert.CipherString;
import com.vogle.sbpayment.client.convert.MultiByteString;
import com.vogle.sbpayment.client.requests.PayDetail;
import com.vogle.sbpayment.client.requests.SpsRequest;
import com.vogle.sbpayment.payeasy.responses.PayEasyPaymentResponse;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

import java.util.List;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * PayEasy payment request
 *
 * @author Allan Im
 **/
@Data
@JacksonXmlRootElement(localName = "sps-api-request")
public class PayEasyPaymentRequest implements SpsRequest<PayEasyPaymentResponse> {

    @JacksonXmlProperty(isAttribute = true)
    private final String id = "ST01-00101-703";

    @NotEmpty
    @Pattern(regexp = "[0-9]{5}")
    @JacksonXmlProperty(localName = "merchant_id")
    private String merchantId;

    @NotEmpty
    @Pattern(regexp = "[0-9]{3}")
    @JacksonXmlProperty(localName = "service_id")
    private String serviceId;

    @NotEmpty
    @Size(max = 64)
    @JacksonXmlProperty(localName = "cust_code")
    private String custCode;

    @NotEmpty
    @Size(max = 38)
    @JacksonXmlProperty(localName = "order_id")
    private String orderId;

    @NotEmpty
    @Size(max = 32)
    @JacksonXmlProperty(localName = "item_id")
    private String itemId;

    @Size(max = 40)
    @MultiByteString
    @JacksonXmlProperty(localName = "item_name")
    private String itemName;

    @Max(9_999_999)
    @Min(0)
    @JacksonXmlProperty(localName = "tax")
    private Integer tax;

    @NotNull
    @Max(9_999_999)
    @Min(1)
    @JacksonXmlProperty(localName = "amount")
    private Integer amount;

    @Size(max = 20)
    @MultiByteString
    @JacksonXmlProperty(localName = "free1")
    private String free1;

    @Size(max = 20)
    @MultiByteString
    @JacksonXmlProperty(localName = "free2")
    private String free2;

    @Size(max = 20)
    @MultiByteString
    @JacksonXmlProperty(localName = "free3")
    private String free3;

    @Max(99)
    @JacksonXmlProperty(localName = "order_rowno")
    private Integer orderRowno;

    @MultiByteString
    @JacksonXmlElementWrapper(localName = "dtls")
    @JacksonXmlProperty(localName = "dtl")
    private List<PayDetail> payDetails;

    @NotNull
    @CipherString
    @MultiByteString
    @JacksonXmlProperty(localName = "pay_method_info")
    private PayEasyMethod payEasyMethod;

    @Pattern(regexp = "[01]")
    @JacksonXmlProperty(localName = "encrypted_flg")
    private String encryptedFlg;

    @NotEmpty
    @JacksonXmlProperty(localName = "request_date")
    private String requestDate;

    @Max(9_999)
    @Min(0)
    @JacksonXmlProperty(localName = "limit_second")
    private Integer limitSecond;

    @NotEmpty
    @Size(max = 40)
    @JacksonXmlProperty(localName = "sps_hashcode")
    private String spsHashcode;

    @Override
    public Class<PayEasyPaymentResponse> responseClass() {
        return PayEasyPaymentResponse.class;
    }
}
