package com.vogle.sbpayment.client;

/**
 * Request interface
 *
 * @author Allan Im
 **/
public interface SpsRequest<T extends SpsResponse> {

    String getId();

    void setMerchantId(String merchantId);

    String getMerchantId();

    void setServiceId(String serviceId);

    String getServiceId();

    void setRequestDate(String requestDate);

    String getRequestDate();

    void setLimitSecond(Integer limitSecond);

    Integer getLimitSecond();

    void setSpsHashcode(String spsHashcode);

    String getSpsHashcode();

    Class<T> responseClass();
}
