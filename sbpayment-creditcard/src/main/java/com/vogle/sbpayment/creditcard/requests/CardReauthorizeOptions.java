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

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
 * Credit card reauthorize  options
 *
 * @author Allan Im
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CardReauthorizeOptions extends CardAuthorizeOptions {

    @NotEmpty
    @Pattern(regexp = "[BMR]")
    @JacksonXmlProperty(localName = "pay_info_control_type")
    private String payInfoControlType;

    @NotEmpty
    @Pattern(regexp = "[BR]")
    @JacksonXmlProperty(localName = "pay_info_detail_control_type")
    private String payInfoDetailControlType;
}
