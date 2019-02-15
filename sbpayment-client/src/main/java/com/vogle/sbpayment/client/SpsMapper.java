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

import com.vogle.sbpayment.client.requests.SpsRequest;

import java.nio.charset.Charset;

/**
 * Softbank payment getMapper
 *
 * @author Allan Im
 */
public interface SpsMapper {

    /**
     * Gets The Charset
     *
     * @return Charset
     */
    Charset getCharset();

    /**
     * Gets The HashKey
     *
     * @return hash key
     */
    String getHashKey();

    /**
     * Convert The XML to The Object
     *
     * @param xml         The XML
     * @param objectClass The converting class
     * @return The Converted object
     */
    <T> T xmlToObject(String xml, Class<T> objectClass);

    /**
     * Convert The Object to The XML
     *
     * @param object The Source
     * @return The Converted XML
     */
    <T> String objectToXml(T object);

    /**
     * Convert The Request to The XML<br/>
     *
     * @param request The Source request
     * @return The Converted XML
     */
    <T extends SpsRequest> String requestToXml(T request);
}
