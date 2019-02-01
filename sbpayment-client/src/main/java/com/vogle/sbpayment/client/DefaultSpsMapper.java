package com.vogle.sbpayment.client;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vogle.sbpayment.client.convert.SpsDataConverter;
import com.vogle.sbpayment.client.requests.SpsRequest;

/**
 * Implements for {@link SpsMapper}
 *
 * @author Allan Im
 */
public class DefaultSpsMapper implements SpsMapper {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultSpsMapper.class);

    private final XmlMapper xmlMapper;

    private final String charset;
    private final String hashKey;
    private final String desKey;
    private final String desInitKey;

    private final boolean enabledCipher;

    /**
     * Create Mapper with hash key & 3DES key
     *
     * @param config The Softbank Payment configuration
     */
    public DefaultSpsMapper(SpsConfig config) {
        this.hashKey = config.getHashKey();
        this.desKey = config.getDesKey();
        this.desInitKey = config.getDesInitKey();
        this.charset = config.getCharset();

        this.enabledCipher = config.isEnabledCipher() && isNotEmpty(desKey) && isNotEmpty(desInitKey);

        this.xmlMapper = new XmlMapper();
        this.xmlMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        this.xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    @Override
    public String getCharset() {
        return this.charset;
    }

    @Override
    public String getHashKey() {
        return this.hashKey;
    }

    private boolean isNotEmpty(String value) {
        return value != null && !value.isEmpty();
    }

    @Override
    public <T> T xmlToObject(String xml, Class<T> objectClass) {
        try {
            // xml mapping
            T bodyObject = xmlMapper.readValue(xml, objectClass);

            // DES Decrypt
            if (enabledCipher) {
                SpsDataConverter.decrypt(desKey, desInitKey, charset, bodyObject);
            }

            return bodyObject;

        } catch (IOException ex) {
            LOGGER.error("SPS xmlToObject Error : {}({})", ex.getClass().getSimpleName(), ex.getMessage());
            throw new XmlMappingException(ex.getMessage(), ex);
        }
    }

    @Override
    public <T> String objectToXml(T object) {

        // make xml
        StringBuilder xml = new StringBuilder("<?xml version=\"1.0\" encoding=\"" + charset + "\"?>\n");

        try {
            if (enabledCipher) {
                // DES Encrypt & base64 encode
                SpsDataConverter.encrypt(desKey, desInitKey, charset, object);
                SpsDataConverter.encodeWithoutCipherString(charset, object);
            } else {
                // base64 encode
                SpsDataConverter.encode(charset, object);
            }
            xml.append(xmlMapper.writeValueAsString(object));

            return xml.toString();

        } catch (JsonProcessingException ex) {
            LOGGER.error("SPS objectToXml Error : {}({})", ex.getClass().getSimpleName(), ex.getMessage());
            throw new XmlMappingException(ex.getMessage(), ex);
        }
    }

    /**
     * {@inheritDoc}
     * It is automatically setting EncryptedFlg & SpsHashCode
     */
    @Override
    public <T extends SpsRequest> String requestToXml(T request) {
        // 1. enable encrypted flag by config
        if (enabledCipher) {
            SpsDataConverter.enableEncryptedFlg(request);
        }

        // 2. Insert a hashcode from request
        String hashCode = SpsDataConverter.makeSpsHashCode(request, hashKey, charset);
        request.setSpsHashcode(hashCode);

        // 3. make xml
        return objectToXml(request);
    }
}
