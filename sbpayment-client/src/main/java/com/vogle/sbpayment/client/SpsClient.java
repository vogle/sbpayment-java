package com.vogle.sbpayment.client;

import com.vogle.sbpayment.client.requests.SpsRequest;
import com.vogle.sbpayment.client.responses.SpsResponse;

/**
 * Softbank Payment Client
 *
 * @author Allan Im
 **/
public interface SpsClient {

    /**
     * Gets new request object and it has common elements, Those are merchant_id, service_id, limit_second
     * and request_date
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
     * @return The received response entity
     */
    <T extends SpsResponse> SpsResult<T> execute(SpsRequest<T> request);

}
