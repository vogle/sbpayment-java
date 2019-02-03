/*
 * Copyright 2019 VOGLE Labs.
 *
 * This file is part of sbpayment-java - Sbpayment client.
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
