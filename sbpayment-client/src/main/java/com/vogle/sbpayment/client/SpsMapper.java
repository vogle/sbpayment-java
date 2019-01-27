package com.vogle.sbpayment.client;

/**
 * Softbank payment mapper
 *
 * @author Allan Im
 */
public interface SpsMapper {

    <T> T xmlToObject(String xml, Class<T> objectClass);

    <T> String objectToXml(T value);
}
