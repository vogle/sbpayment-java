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

package com.vogle.sbpayment.client.responses;

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

    /**
     * To return true if the response data is successful
     */
    default boolean isSuccess() {
        return "OK".equalsIgnoreCase(getResult());
    }
}
