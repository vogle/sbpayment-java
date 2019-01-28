package com.vogle.sbpayment.client;

import com.vogle.sbpayment.client.requests.SpsRequest;

/**
 * Softbank payment mapper
 *
 * @author Allan Im
 */
public interface SpsMapper {

    /**
     * Gets The Charset
     *
     * @return Charset
     */
    String getCharset();

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
