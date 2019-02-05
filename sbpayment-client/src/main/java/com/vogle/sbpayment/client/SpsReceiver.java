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

import com.vogle.sbpayment.client.receivers.SpsReceivedData;

/**
 * Softbank Payment Receiver
 *
 * @author Allan Im
 */
public interface SpsReceiver {

    /**
     * Receive XML
     *
     * @param xml               The XML to convert
     * @param receivedDataClass The Class to be converted
     * @return The received data Object
     * @throws InvalidAccessException This Exception is occurred if The receivers is NOT valid
     */
    <T extends SpsReceivedData> T receive(String xml, Class<T> receivedDataClass) throws InvalidAccessException;

    /**
     * Make a success result
     *
     * @param featureId SPS Feature ID
     * @return Result XML
     */
    String resultSuccessful(String featureId);

    /**
     * Make a failure result
     *
     * @param featureId    SPS Feature ID
     * @param errorMessage Error message to forward
     * @return Result XML
     */
    String resultFailed(String featureId, String errorMessage);


}
