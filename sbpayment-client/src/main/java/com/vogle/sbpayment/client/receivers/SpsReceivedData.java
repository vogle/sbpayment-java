package com.vogle.sbpayment.client.receivers;

/**
 * Received data interface
 *
 * @author Allan Im
 **/
public interface SpsReceivedData {

    String getId();

    void setId(String id);

    String getMerchantId();

    void setMerchantId(String merchantId);

    String getServiceId();

    void setServiceId(String serviceId);

    String getSpsTransactionId();

    void setSpsTransactionId(String spsTransactionId);

    String getTrackingId();

    void setTrackingId(String trackingId);

    String getRecDatetime();

    void setRecDatetime(String recDatetime);

    String getRequestDate();

    void setRequestDate(String requestDate);

    String getSpsHashcode();

    void setSpsHashcode(String spsHashcode);
}
