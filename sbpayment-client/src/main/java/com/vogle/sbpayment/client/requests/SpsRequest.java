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

package com.vogle.sbpayment.client.requests;

import com.vogle.sbpayment.client.responses.SpsResponse;

/**
 * Request interface
 *
 * @author Allan Im
 **/
public interface SpsRequest<T extends SpsResponse> {

    /**
     * Gets a Features ID
     */
    String getId();

    /**
     * Gets a Merchant ID
     */
    String getMerchantId();

    /**
     * Sets a Merchant ID
     */
    void setMerchantId(String merchantId);

    /**
     * Gets a Service ID
     */
    String getServiceId();

    /**
     * Sets a Service ID
     */
    void setServiceId(String serviceId);

    /**
     * Gets Request Date
     */
    String getRequestDate();

    /**
     * Sets Request Date
     */
    void setRequestDate(String requestDate);

    /**
     * Gets Waiting time
     */
    Integer getLimitSecond();

    /**
     * Sets Waiting time
     */
    void setLimitSecond(Integer limitSecond);

    /**
     * Gets a hash-code
     */
    String getSpsHashcode();

    /**
     * Sets a hash-code
     */
    void setSpsHashcode(String spsHashcode);

    /**
     * Response Class Type
     */
    Class<T> responseClass();
}
