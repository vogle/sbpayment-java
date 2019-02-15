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

import com.vogle.sbpayment.client.responses.CommonElements;
import com.vogle.sbpayment.client.responses.SpsInfo;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Card info common elements
 *
 * @author Allan Im
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CardInfoElements extends CommonElements {

    @JacksonXmlProperty(localName = "res_sps_info")
    private SpsInfo spsInfo;

    @Override
    public String getDescription() {
        return FeatureHelper.getDescription(this.getId());
    }
}
