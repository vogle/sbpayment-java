package com.vogle.sbpayment.client;

/**
 * Response interface
 *
 * @author Allan Im
 **/
public interface SpsResponse {

    /**
     * Features ID
     */
    String getId();

    /**
     * Response description
     */
    String getDescription();

    /**
     * Response result
     */
    String getResult();

    /**
     * if has error, get error code
     */
    String getErrCode();

    /**
     * Transaction id
     */
    String getSpsTransactionId();
}
