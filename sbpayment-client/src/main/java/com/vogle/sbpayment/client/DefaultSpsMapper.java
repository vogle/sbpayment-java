package com.vogle.sbpayment.client;

import com.vogle.sbpayment.client.convert.SpsDataConverter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Implements for {@link SpsMapper}
 *
 * @author Allan Im
 */
public class DefaultSpsMapper implements SpsMapper {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final SbpaymentSettings settings;
    private final XmlMapper xmlMapper;

    public DefaultSpsMapper(SbpaymentSettings settings) {
        this.settings = settings;

        this.xmlMapper = new XmlMapper();
        this.xmlMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        this.xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    @Override
    public <T> T xmlToObject(String xml, Class<T> objectClass) {
        try {
            // xml mapping
            T bodyObject = xmlMapper.readValue(xml, objectClass);

            // DES Decrypt
            if (settings.getCipherSets().isEnabled()) {
                SpsDataConverter.decrypt(
                        settings.getCipherSets().getDesKey(),
                        settings.getCipherSets().getDesInitKey(),
                        settings.getCharset(), bodyObject);
            }

            return bodyObject;

        } catch (IOException ex) {
            logger.error("SPS xmlToObject Error : {}({})", ex.getClass().getSimpleName(), ex.getMessage());
            throw new XmlMappingException(ex.getMessage(), ex);
        }
    }

    @Override
    public <T> String objectToXml(T value) {
        String charset = settings.getCharset();

        // make xml
        StringBuilder xml = new StringBuilder("<?xml version=\"1.0\" encoding=\"" + charset + "\"?>\n");

        try {
            if (settings.getCipherSets().isEnabled()) {
                // DES Encrypt & base64 encode
                SpsDataConverter.encrypt(
                        settings.getCipherSets().getDesKey(),
                        settings.getCipherSets().getDesInitKey(),
                        charset, value);
                SpsDataConverter.encodeWithoutCipherString(charset, value);
            } else {
                // base64 encode
                SpsDataConverter.encode(charset, value);
            }
            xml.append(xmlMapper.writeValueAsString(value));

            return xml.toString();

        } catch (JsonProcessingException ex) {
            logger.error("SPS objectToXml Error : {}({})", ex.getClass().getSimpleName(), ex.getMessage());
            throw new XmlMappingException(ex.getMessage(), ex);
        }
    }
}
