package com.vogle.sbpayment.client;

/**
 * Softbank Payment Client
 *
 * @author Allan Im
 **/
public interface SpsClient {

    /**
     * Gets Settings
     *
     * @return SpsClientSettings
     */
    SpsClientSettings getSettings();

    /**
     * Gets new request object and it has common elements
     *
     * @param clazz request class
     * @return new request
     */
    <T extends SpsRequest> T newRequest(Class<T> clazz);


    /**
     * Execute transmission<br/>
     * 通信実行
     *
     * @param request The sending request object
     * @return The receiving response entity
     */
    <T extends SpsResponse> SpsResponseEntity<T> execute(SpsRequest<T> request);

    /**
     * XML convert to Object
     *
     * @param body        The receiving response body
     * @param objectClass The Converting object
     * @return The converted Object
     */
    <T> T xmlToObject(String body, Class<T> objectClass);

    /**
     * Object convert to Xml
     *
     * @param value The Source object
     * @return The converted XML
     */
    <T> String objectToXml(T value);

    /**
     * Make hash-code by Softbank payment rules
     *
     * @param value The sources object
     * @return Hash Code
     */
    String makeSpsHashCode(Object value);
}
