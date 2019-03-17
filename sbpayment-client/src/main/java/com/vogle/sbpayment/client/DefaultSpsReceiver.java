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

import com.vogle.sbpayment.client.convert.SpsDataConverter;
import com.vogle.sbpayment.client.receivers.ReceptionResult;
import com.vogle.sbpayment.client.receivers.SpsReceivedData;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implements for {@link SpsReceiver}
 *
 * @author Allan Im
 */
public class DefaultSpsReceiver implements SpsReceiver {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultSpsReceiver.class);

    private final String merchantId;
    private final String serviceId;

    private final SpsMapper mapper;

    /**
     * Create Receiver
     *
     * @param config The Softbank Payment configuration
     * @param mapper The {@link SpsMapper}
     */
    public DefaultSpsReceiver(SpsConfig.SpsInfo config, SpsMapper mapper) {
        this.merchantId = config.getMerchantId();
        this.serviceId = config.getServiceId();
        this.mapper = mapper;
    }

    /**
     * Receive the XML
     *
     * @param xml               The XML to convert
     * @param receivedDataClass The Class to be converted
     * @return The received data Object
     * @throws InvalidAccessException This Exception is occurred if The receivers is NOT valid
     */
    @Override
    public <T extends SpsReceivedData> T receive(String xml, Class<T> receivedDataClass)
        throws InvalidAccessException {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("SPS Receiver data : {}", xml);
        }

        T receivedData = mapper.xmlToObject(xml, receivedDataClass);

        // Check merchant id
        if (!merchantId.equals(receivedData.getMerchantId())) {
            throw new InvalidAccessException("The merchant id is wrong: " + receivedData.getMerchantId());
        }

        // Check service id
        if (!serviceId.equals(receivedData.getServiceId())) {
            throw new InvalidAccessException("The service id is wrong: " + receivedData.getServiceId());
        }

        // Check hash code
        String hashcode = SpsDataConverter.makeSpsHashCode(receivedData, mapper.getHashKey(), mapper.getCharset());
        if (!hashcode.equals(receivedData.getSpsHashcode())) {
            throw new InvalidAccessException("The hashcode is wrong: " + receivedData.getSpsHashcode());
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("SPS Received data : {}", receivedData);
        }

        return receivedData;
    }

    /**
     * Make a success result
     *
     * @param featureId SPS Feature ID
     * @return Result XML
     */
    @Override
    public String resultSuccessful(String featureId) {
        ValidationHelper.assertsNotEmpty(featureId, "featureId");
        return mapper.objectToXml(new ReceptionResult(featureId));
    }

    /**
     * Make a failure result
     *
     * @param featureId    SPS Feature ID
     * @param errorMessage Error message to forward
     * @return Result XML
     */
    @Override
    public String resultFailed(String featureId, String errorMessage) {
        ValidationHelper.assertsNotEmpty(featureId, "featureId");
        ValidationHelper.assertsNotEmpty(errorMessage, "errorMessage");
        return mapper.objectToXml(new ReceptionResult(featureId, errorMessage));
    }

}
