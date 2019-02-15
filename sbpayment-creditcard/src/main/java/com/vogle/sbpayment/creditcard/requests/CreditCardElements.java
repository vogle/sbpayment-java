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

package com.vogle.sbpayment.creditcard.requests;

import com.vogle.sbpayment.client.convert.CipherString;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Credit card information
 *
 * @author Allan Im
 **/
@Data
public class CreditCardElements {

    @NotEmpty
    @Pattern(regexp = "[0-9]{13,16}")
    @CipherString
    @JacksonXmlProperty(localName = "cc_number")
    private String ccNumber;

    @NotEmpty
    @Pattern(regexp = "(\\d{4}(0[0-9]|1[0-2]))")
    @CipherString
    @JacksonXmlProperty(localName = "cc_expiration")
    private String ccExpiration;

    @Size(min = 3, max = 4)
    @Pattern(regexp = "[0-9]{3,4}")
    @CipherString
    @JacksonXmlProperty(localName = "security_code")
    private String securityCode;

}
