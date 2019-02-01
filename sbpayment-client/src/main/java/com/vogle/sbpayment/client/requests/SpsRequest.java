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
