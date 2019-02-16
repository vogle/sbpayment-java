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

package com.vogle.sbpayment.client.receivers;

import com.vogle.sbpayment.client.convert.MultiByteString;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Result for getReceiver
 *
 * @author Allan Im
 **/
@Getter
@ToString
@JacksonXmlRootElement(localName = "sps-api-response")
public class ReceptionResult {

    public static final String SUCCESS = "OK";
    public static final String FAIL = "NG";

    @JacksonXmlProperty(isAttribute = true)
    private String id;

    @JacksonXmlProperty(localName = "res_result")
    private String result;

    @Setter
    @MultiByteString
    @JacksonXmlProperty(localName = "res_err_msg")
    private String errMsg;

    /**
     * Constructs a new successful ReceptionResult
     *
     * @param id a sbpayment features ID
     */
    public ReceptionResult(String id) {
        this.id = id;
        this.result = SUCCESS;
    }

    /**
     * Constructs a new failure ReceptionResult
     *
     * @param id           a sbpayment features ID
     * @param errorMessage sending message
     */
    public ReceptionResult(String id, String errorMessage) {
        this.id = id;
        this.result = FAIL;
        this.errMsg = errorMessage;
    }

}
