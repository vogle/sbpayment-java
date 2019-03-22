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

import java.util.Properties;

/**
 * Softbank payment<br/>
 * It has Mapper, Client & Receiver
 *
 * @author Allan Im
 */
public interface Sbpayment {

    /**
     * Create Default sbpayment with sbpayment.properties in resource
     */
    static Sbpayment newInstance() {
        return new DefaultSbpayment();
    }

    /**
     * Create Default sbpayment with the file path in resource
     *
     * @param filePath properties file path in resource
     */
    static Sbpayment newInstance(String filePath) {
        return new DefaultSbpayment(filePath);
    }

    /**
     * Create Default sbpayment with properties object
     *
     * @param properties The {@link Properties}
     */
    static Sbpayment newInstance(Properties properties) {
        return new DefaultSbpayment(SpsConfig.from(properties));
    }

    /**
     * Create Default sbpayment with config object
     *
     * @param config The {@link SpsConfig}
     */
    static Sbpayment newInstance(SpsConfig config) {
        return new DefaultSbpayment(config);
    }

    /**
     * SPS Information
     */
    SpsConfig.SpsInfo getSpsInfo();


    /**
     * Gets made getMapper
     *
     * @return SpsMapper
     */
    SpsMapper getMapper();

    /**
     * Gets made getClient
     *
     * @return SpsClient
     */
    SpsClient getClient();

    /**
     * Gets made getReceiver
     *
     * @return SpsReceiver
     */
    SpsReceiver getReceiver();
}
