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

package com.vogle.sbpayment.client.requests;

import com.vogle.sbpayment.client.convert.MultiByteString;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Size;

/**
 * Order details
 *
 * @author Allan Im
 **/
@Data
public class PayDetail {

    @Max(999)
    @JacksonXmlProperty(localName = "dtl_rowno")
    private Integer dtlRowno;

    @Size(max = 20)
    @JacksonXmlProperty(localName = "dtl_item_id")
    private String dtlItemId;

    @Size(max = 40)
    @MultiByteString
    @JacksonXmlProperty(localName = "dtl_item_name")
    private String dtlItemName;

    @Max(999_999)
    @JacksonXmlProperty(localName = "dtl_item_count")
    private Integer dtlItemCount;

    @Max(9_999_999)
    @JacksonXmlProperty(localName = "dtl_tax")
    private Integer dtlTax;

    @Max(9_999_999)
    @JacksonXmlProperty(localName = "dtl_amount")
    private Integer dtlAmount;

}
