package com.vogle.sbpayment.client;

/**
 * Request interface
 *
 * @author Allan Im
 **/
public interface SpsRequest<T extends SpsResponse> {

    String getId();

    String getMerchantId();

    void setMerchantId(String merchantId);

    String getServiceId();

    void setServiceId(String serviceId);

    String getRequestDate();

    void setRequestDate(String requestDate);

    Integer getLimitSecond();

    void setLimitSecond(Integer limitSecond);

    String getSpsHashcode();

    void setSpsHashcode(String spsHashcode);

    Class<T> responseClass();
}
