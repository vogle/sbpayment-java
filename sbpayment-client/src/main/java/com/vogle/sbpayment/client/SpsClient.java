package com.vogle.sbpayment.client;

/**
 * Softbank Payment Client
 *
 * @author Allan Im
 **/
public interface SpsClient {

    /**
     * Gets new request object and it has common elements
     *
     * @param clazz request class
     * @return new request
     */
    <T extends SpsRequest> T newRequest(Class<T> clazz);


    /**
     * Execute transmission<br/>
     * 通信実行
     *
     * @param request The sending request object
     * @return The receiving response entity
     */
    <T extends SpsResponse> SpsResult<T> execute(SpsRequest<T> request);


}
