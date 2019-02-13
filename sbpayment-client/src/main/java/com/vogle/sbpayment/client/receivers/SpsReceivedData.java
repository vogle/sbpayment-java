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

/**
 * Received data interface
 *
 * @author Allan Im
 **/
public interface SpsReceivedData {

    /**
     * Gets a Features ID
     */
    String getId();

    /**
     * Sets a Features ID
     */
    void setId(String id);

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
     * Gets a SPS-Transaction ID
     */
    String getSpsTransactionId();

    /**
     * Sets a SPS-Transaction ID
     */
    void setSpsTransactionId(String spsTransactionId);

    /**
     * Gets a Tracking ID
     */
    String getTrackingId();

    /**
     * Sets a Tracking ID
     */
    void setTrackingId(String trackingId);

    /**
     * Gets Processing time
     */
    String getRecDatetime();

    /**
     * Sets Processing time
     */
    void setRecDatetime(String recDatetime);

    /**
     * Gets Request Date
     */
    String getRequestDate();

    /**
     * Sets Request Date
     */
    void setRequestDate(String requestDate);

    /**
     * Gets a hash-code
     */
    String getSpsHashcode();

    /**
     * Sets a hash-code
     */
    void setSpsHashcode(String spsHashcode);
}
