package com.vogle.sbpayment.client;

import com.vogle.sbpayment.client.convert.SpsDataConverter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Default Softbank payment client
 *
 * @author Allan Im
 **/
public class DefaultSpsClient extends AbstractSpsHttpClient {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final XmlMapper xmlMapper;
    private final ObjectMapper objectMapper;

    public DefaultSpsClient(SpsSettings settings) {
        super(settings);
        this.xmlMapper = new XmlMapper();
        this.xmlMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        this.xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);

        this.objectMapper = new ObjectMapper();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends SpsResponse> SpsResponseEntity<T> execute(SpsRequest<T> request) {

        String charset = settings.getCharset();
        try {
            if (logger.isDebugEnabled()) {
                logger.debug("SPS Client request object : \n{}\n", request);
            }

            // Make hashcode from xml and setup
            request.setSpsHashcode(makeSpsHashCode(request));

            // DES Encrypt & base64 encode
            String xmlRequest = objectToXml(request);

            if (logger.isDebugEnabled()) {
                logger.debug("SPS Client request XML : \n{}\n", xmlRequest);
            }

            // execute
            HttpPost method = new HttpPost(settings.getApiUrl());
            method.setEntity(new StringEntity(xmlRequest, charset));
            HttpResponse response = httpClient.execute(method);

            // response status
            int statusCode = response.getStatusLine().getStatusCode();

            // success
            if (statusCode == 200) {
                // response header
                Map<String, String> headerMap = new HashMap<>();

                // response body
                String body = EntityUtils.toString(response.getEntity(), settings.getCharset());
                if (logger.isDebugEnabled()) {
                    logger.debug("SPS Client response header :\n{}\n", headerMap);
                    logger.debug("SPS Client response xml :\n{}\n", body);
                }

                // xml mapping
                T bodyObject = xmlToObject(body, request.responseClass());

                if (logger.isInfoEnabled()) {
                    logger.info("SPS Client ({}) response : result = {}{}{}",
                            bodyObject.getId() == null ? "FAIL" : SpsFeatures.getFeatureName(bodyObject.getId()),
                            bodyObject.getResult(),
                            "OK".equalsIgnoreCase(bodyObject.getResult()) ? "" : ", errCode = " + bodyObject.getErrCode(),
                            bodyObject.getSpsTransactionId() == null ? "" : ", sps-transaction-id = "
                                    + bodyObject.getSpsTransactionId());
                }

                if (logger.isDebugEnabled()) {
                    logger.debug("SPS Client response object : \n{}\n", bodyObject);
                }

                return new SpsResponseEntity<>(statusCode, headerMap, bodyObject);

            } else if (statusCode == 401) {
                logger.error("SPS Client connect fail : Either you supplied the wrong credentials,"
                                + " authId : {}, authPw : {} ",
                        settings.getBasicAuthId(), settings.getBasicAuthPassword());
                return new SpsResponseEntity<>(statusCode);
            } else if (statusCode == 403) {
                logger.error("SPS Client connect fail : You don't have permission to access {} on this server",
                        settings.getApiUrl());
                return new SpsResponseEntity<>(statusCode);
            } else if (statusCode == 404) {
                logger.error("SPS Client connect fail : The requested URL {} was not found on this server",
                        settings.getApiUrl());
                return new SpsResponseEntity<>(statusCode);
            } else if (statusCode == 500) {
                logger.error("SPS Client connect fail : Internal server error from {}",
                        settings.getApiUrl());
                return new SpsResponseEntity<>(statusCode);
            } else if (statusCode == 503) {
                logger.error("SPS Client connect fail : The server is temporarily unable to service your request"
                        + " due to maintenance downtime");
                return new SpsResponseEntity<>(statusCode);
            } else {
                logger.error("SPS Client connect fail : HTTP Status {}", statusCode);
                return new SpsResponseEntity<>(statusCode);
            }

        } catch (IOException ignored) {
            logger.error("SPS Client Internal Error : {}({})",
                    ignored.getClass().getSimpleName(), ignored.getMessage());
        }
        return new SpsResponseEntity<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T xmlToObject(String body, Class<T> objectClass) {

        try {
            // xml mapping
            T bodyObject = xmlMapper.readValue(body, objectClass);

            // DES Decrypt
            if (settings.getCipherSets().isEnabled()) {
                SpsDataConverter.decrypt(settings.getCipherSets(), settings.getCharset(), bodyObject);
            }

            return bodyObject;

        } catch (IOException ex) {
            logger.error("SPS xmlToObject Error : {}({})", ex.getClass().getSimpleName(), ex.getMessage());
            throw new XmlMappingException(ex.getMessage(), ex);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> String objectToXml(T value) {
        String charset = settings.getCharset();

        // make xml
        String xml = "<?xml version=\"1.0\" encoding=\"" + charset + "\"?>\n";

        try {
            if (settings.getCipherSets().isEnabled()) {
                // DES Encrypt & base64 encode
                SpsDataConverter.encrypt(settings.getCipherSets(), charset, value);
                SpsDataConverter.encodeWithoutCipherField(charset, value);
            } else {
                // base64 encode
                SpsDataConverter.encode(charset, value);
            }
            xml = xml.concat(xmlMapper.writeValueAsString(value));

            return xml;

        } catch (JsonProcessingException ex) {
            logger.error("SPS objectToXml Error : {}({})", ex.getClass().getSimpleName(), ex.getMessage());
            throw new XmlMappingException(ex.getMessage(), ex);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String makeSpsHashCode(Object value) {
        try {
            byte[] json = objectMapper.writeValueAsBytes(value);

            // hash code
            StringBuilder spsHashCode = new StringBuilder();

            // log data
            String featureId = "";
            StringBuilder logString = new StringBuilder();

            // data
            JsonNode jsonNode = objectMapper.readTree(json);
            Iterator<String> nodeNames = jsonNode.fieldNames();
            while (nodeNames.hasNext()) {
                String filed = nodeNames.next();
                // hash code
                if (!"id".equals(filed) && !"spsHashcode".equals(filed)) {
                    spsHashCode.append(textValue(jsonNode.findValue(filed)));
                }

                // log
                if (logger.isInfoEnabled()) {
                    if ("id".equals(filed) || "custCode".equals(filed) || "orderId".equals(filed)
                            || "trackingId".equals(filed)) {
                        if (logString.length() != 0) {
                            logString.append(", ");
                        }
                        if ("id".equals(filed)) {
                            featureId = SpsFeatures.getFeatureName(textValue(jsonNode.findValue(filed)));
                        } else {
                            logString.append(filed).append(" = ").append(textValue(jsonNode.findValue(filed)));
                        }
                    }
                }
            }

            // sps hash key
            spsHashCode.append(settings.getHashKey());

            // info log
            if (logger.isInfoEnabled()) {
                logger.info("SPS Client ({}) request : {}", featureId, logString);
            }

            return DigestUtils.sha1Hex(spsHashCode.toString().getBytes(settings.getCharset()));

        } catch (IOException ex) {
            throw new MakeHashCodeException(ex);
        }
    }

    private String textValue(JsonNode jsonNode) {
        StringBuilder result = new StringBuilder();

        if (jsonNode.isObject() || jsonNode.isArray()) {
            Iterator<JsonNode> subNode = jsonNode.elements();
            while (subNode.hasNext()) {
                result.append(textValue(subNode.next()));
            }
        } else if (jsonNode.isTextual()) {
            result.append(jsonNode.textValue().trim());
        } else if (jsonNode.isNumber()) {
            result.append(jsonNode.numberValue());
        }
        return result.toString();
    }
}
