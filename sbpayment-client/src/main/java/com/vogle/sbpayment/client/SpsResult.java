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

package com.vogle.sbpayment.client;

import com.vogle.sbpayment.client.responses.SpsResponse;

import lombok.Getter;
import lombok.ToString;

import java.util.Map;

/**
 * Received data from Softbank Payment
 *
 * @author Allan Im
 **/
@ToString
public class SpsResult<T extends SpsResponse> {

    /**
     * HTTP Status Code
     */
    @Getter
    private final int status;

    /**
     * HTTP Headers
     */
    @Getter
    private final Map<String, String> headers;

    /**
     * HTTP Body
     */
    @Getter
    private final T body;


    /**
     * Construct a default result
     */
    public SpsResult() {
        this(999);
    }

    /**
     * Construct a result with response status code
     *
     * @param status The HTTP Status code
     */
    public SpsResult(int status) {
        this(status, null, null);
    }

    /**
     * Create a new {@code HttpEntity} with the given body and headers.
     *
     * @param status  the entity status
     * @param headers the entity headers
     * @param body    the entity body
     */
    public SpsResult(int status, Map<String, String> headers, T body) {
        this.status = status;
        this.headers = headers;
        this.body = body;
    }

    /**
     * To return true if it is successful response after connecting to sbpayment.
     */
    public boolean isSuccessfulConnection() {
        return 200 == status;
    }

    /**
     * To return true if the response data is successful
     */
    public boolean isSuccess() {
        return isSuccessfulConnection() && body.isSuccess();
    }
}
