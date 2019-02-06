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

/**
 * Softbank payment<br/>
 * It has Mapper, Client & Receiver
 *
 * @author Allan Im
 */
public class DefaultSbpayment implements Sbpayment {
    private final SpsConfig config;

    private SpsMapper mapper;
    private SpsClient client;
    private SpsReceiver receiver;

    /**
     * Create Sbpayment
     *
     * @param config The Softbank Payment Configuration
     */
    public DefaultSbpayment(SpsConfig config) {
        this.config = config;
    }

    /**
     * Gets made mapper
     *
     * @return SpsMapper
     */
    @Override
    public SpsMapper mapper() {
        if (mapper == null) {
            mapper = new DefaultSpsMapper(config);
        }
        return mapper;
    }

    /**
     * Gets made client
     *
     * @return SpsClient
     */
    @Override
    public SpsClient client() {
        if (client == null) {
            client = new DefaultSpsClient(config, mapper());
        }
        return client;
    }

    /**
     * Gets made receiver
     *
     * @return SpsReceiver
     */
    @Override
    public SpsReceiver receiver() {
        if (receiver == null) {
            receiver = new DefaultSpsReceiver(config, mapper());
        }
        return receiver;
    }
}
