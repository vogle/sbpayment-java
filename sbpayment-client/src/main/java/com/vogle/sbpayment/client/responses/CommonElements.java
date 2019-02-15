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

package com.vogle.sbpayment.client.responses;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

/**
 * Common Response Elements
 *
 * @author Allan Im
 **/
@Data
public abstract class CommonElements implements SpsResponse {

    @JacksonXmlProperty(isAttribute = true)
    private String id;

    @JacksonXmlProperty(localName = "res_result")
    private String result;

    @JacksonXmlProperty(localName = "res_sps_transaction_id")
    private String spsTransactionId;

    @JacksonXmlProperty(localName = "res_process_date")
    private String processDate;

    @JacksonXmlProperty(localName = "res_err_code")
    private String errCode;

    @JacksonXmlProperty(localName = "res_date")
    private String date;

    @Override
    abstract public String getDescription();
}
