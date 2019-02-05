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
 * Softbank payment Manager<br/>
 * It has Mapper, Client & Receiver
 *
 * @author Allan Im
 */
public class DefaultSpsManager implements SpsManager {

    private final SpsMapper mapper;
    private final SpsClient client;
    private final SpsReceiver receiver;

    /**
     * Create Manager
     *
     * @param config The Softbank Payment Configuration
     */
    public DefaultSpsManager(SpsConfig config) {
        this.mapper = new DefaultSpsMapper(config);
        this.client = new DefaultSpsClient(config, this.mapper);
        this.receiver = new DefaultSpsReceiver(config, this.mapper);
    }

    /**
     * Gets made mapper
     *
     * @return SpsMapper
     */
    @Override
    public SpsMapper mapper() {
        return this.mapper;
    }

    /**
     * Gets made client
     *
     * @return SpsClient
     */
    @Override
    public SpsClient client() {
        return this.client;
    }

    /**
     * Gets mad receiver
     *
     * @return SpsReceiver
     */
    @Override
    public SpsReceiver receiver() {
        return this.receiver;
    }
}
