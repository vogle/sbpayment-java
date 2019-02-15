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

package com.vogle.sbpayment.creditcard.responses;

import com.vogle.sbpayment.client.convert.CipherString;
import com.vogle.sbpayment.client.convert.MultiByteString;
import com.vogle.sbpayment.creditcard.CreditCardBrand;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

/**
 * Card lookup method information
 *
 * @author Allan Im
 **/
@Data
public class CardInfoLookupMethodInfo {

    @CipherString
    @JacksonXmlProperty(localName = "cc_number")
    private String ccNumber;

    @CipherString
    @JacksonXmlProperty(localName = "cc_expiration")
    private String ccExpiration;

    @CipherString
    @JacksonXmlProperty(localName = "cardbrand_code")
    private String cardbrandCode;

    @CipherString
    @MultiByteString
    @JacksonXmlProperty(localName = "resrv1")
    private String resrv1;

    @CipherString
    @MultiByteString
    @JacksonXmlProperty(localName = "resrv2")
    private String resrv2;

    @CipherString
    @MultiByteString
    @JacksonXmlProperty(localName = "resrv3")
    private String resrv3;

    /**
     * Gets a {@link CreditCardBrand}
     *
     * @return card brand
     */
    public CreditCardBrand getCreditCardBrand() {
        return CreditCardBrand.brand(cardbrandCode);
    }
}
