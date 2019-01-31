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
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final XmlMapper xmlMapper;

    private String charset = "Shift_JIS";
    private String hashKey;
    private String desKey;
    private String desInitKey;


    /**
     * Create Mapper with hash key
     *
     * @param hashKey from Softbank payment system
     */
    public DefaultSpsMapper(String hashKey) {
        this.hashKey = hashKey;

        this.xmlMapper = new XmlMapper();
        this.xmlMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        this.xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    /**
     * Create Mapper with hash key & 3DES key
     *
     * @param hashKey    from Softbank payment system
     * @param desKey     from Softbank payment system that length is 24
     * @param desInitKey from Softbank payment system that length is 8
     */
    public DefaultSpsMapper(String hashKey, String desKey, String desInitKey) {
        this(hashKey);
        this.desKey = desKey;
        this.desInitKey = desInitKey;
    }

    public void updateCharset(String charset) {
        this.charset = charset;
    }

    @Override
    public String getCharset() {
        return this.charset;
    }

    @Override
    public String getHashKey() {
        return this.hashKey;
    }

    private boolean cipherEnabled() {
        return isNotEmpty(desKey) && isNotEmpty(desInitKey);
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
            if (cipherEnabled()) {
                SpsDataConverter.decrypt(desKey, desInitKey, charset, bodyObject);
            }

            return bodyObject;

        } catch (IOException ex) {
            logger.error("SPS xmlToObject Error : {}({})", ex.getClass().getSimpleName(), ex.getMessage());
            throw new XmlMappingException(ex.getMessage(), ex);
        }
    }

    @Override
    public <T> String objectToXml(T object) {

        // make xml
        StringBuilder xml = new StringBuilder("<?xml version=\"1.0\" encoding=\"" + charset + "\"?>\n");

        try {
            if (cipherEnabled()) {
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
            logger.error("SPS objectToXml Error : {}({})", ex.getClass().getSimpleName(), ex.getMessage());
            throw new XmlMappingException(ex.getMessage(), ex);
        }
    }

    /**
     * {@inheritDoc}
     * It is automatically setting EncryptedFlg & SpsHashCode
     */
    @Override
    public <T extends SpsRequest> String requestToXml(T request) {
        // 1. enable encrypted flag by settings
        if (cipherEnabled()) {
            SpsDataConverter.enableEncryptedFlg(request);
        }

        // 2. Insert a hashcode from request
        String hashCode = SpsDataConverter.makeSpsHashCode(request, hashKey, charset);
        request.setSpsHashcode(hashCode);

        // 3. make xml
        return objectToXml(request);
    }
}
