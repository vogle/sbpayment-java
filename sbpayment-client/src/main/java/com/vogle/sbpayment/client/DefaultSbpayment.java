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

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Softbank payment<br/>
 * It has Mapper, Client & Receiver
 *
 * @author Allan Im
 */
public class DefaultSbpayment implements Sbpayment {

    private static final String PROPERTY_FILE = "sbpayment.properties";
    private final SpsConfig config;

    private SpsMapper mapper;
    private SpsClient client;
    private SpsReceiver receiver;

    /**
     * Create Default sbpayment with sbpayment.properties in resource
     */
    protected DefaultSbpayment() {
        this(PROPERTY_FILE);
    }

    /**
     * Create Default sbpayment with the file path in resource
     *
     * @param filePath properties file path in resource
     */
    protected DefaultSbpayment(String filePath) {
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);
        if (inputStream == null) {
            throw new IllegalArgumentException("Must be existed '" + filePath + "' file in Resource");
        }

        Properties properties = new Properties();
        try {
            properties.load(inputStream);
        } catch (IOException ignored) {
            // ignored
        }
        this.config = SpsConfig.from(properties);
    }

    /**
     * Create Default sbpayment with config object
     *
     * @param config The {@link SpsConfig}
     */
    protected DefaultSbpayment(SpsConfig config) {
        this.config = config;
    }

    /**
     * SPS Information
     */
    @Override
    public SpsConfig.SpsInfo getSpsInfo() {
        return config.getSpsInfo();
    }

    /**
     * Gets made getMapper
     *
     * @return SpsMapper
     */
    @Override
    public SpsMapper getMapper() {
        if (mapper == null) {
            mapper = new DefaultSpsMapper(config.getCipherInfo());
        }
        return new DefaultSpsMapper(config.getCipherInfo());
    }

    /**
     * Gets made getClient
     *
     * @return SpsClient
     */
    @Override
    public SpsClient getClient() {
        if (client == null) {
            client = new DefaultSpsClient(config.getClientInfo(), getMapper());
        }
        return client;
    }

    /**
     * Gets made getReceiver
     *
     * @return SpsReceiver
     */
    @Override
    public SpsReceiver getReceiver() {
        if (receiver == null) {
            receiver = new DefaultSpsReceiver(config.getSpsInfo(), getMapper());
        }
        return receiver;
    }

    /**
     * return config information
     */
    @Override
    public String toString() {
        return config.toString();
    }
}
