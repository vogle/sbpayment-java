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
