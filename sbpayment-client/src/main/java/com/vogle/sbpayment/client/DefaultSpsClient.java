package com.vogle.sbpayment.client;

import com.vogle.sbpayment.client.convert.SpsDataConverter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

/**
 * Default Softbank payment client
 *
 * @author Allan Im
 **/
public class DefaultSpsClient implements SpsClient {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected final SpsClientSettings settings;
    protected final HttpClient httpClient;
    protected final XmlMapper xmlMapper;
    protected final ObjectMapper objectMapper;

    public DefaultSpsClient(SpsClientSettings settings) {
        this.settings = settings;

        this.httpClient = httpClient(settings);

        this.xmlMapper = new XmlMapper();
        this.xmlMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        this.xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);

        this.objectMapper = new ObjectMapper();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SpsClientSettings getSettings() {
        return this.settings;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCommonElementsTo(SpsRequest request) {
        request.setMerchantId(settings.getMerchantId());
        request.setServiceId(settings.getServiceId());
        request.setLimitSecond(settings.getAllowableSecondOnRequest());

        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        dateFormat.setTimeZone(TimeZone.getTimeZone(settings.getTimeZone()));
        request.setRequestDate(dateFormat.format(new Date()));
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

            // enable encrypted flag
            if (settings.getCipherSets().isEnabled()) {
                SpsDataConverter.enableEncryptedFlg(request, request.getClass());
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

    /**
     * Create HttpClient
     */
    protected HttpClient httpClient(SpsClientSettings settings) {
        // http client
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();

        // basic authorize information
        if (isNotEmpty(settings.getBasicAuthId()) && isNotEmpty(settings.getBasicAuthPassword())) {
            CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials(AuthScope.ANY,
                    new UsernamePasswordCredentials(settings.getBasicAuthId(), settings.getBasicAuthPassword()));
            httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
        }

        // headers
        String charset = settings.getCharset();
        if (isEmpty(charset)) {
            charset = "Shift_JIS";
        }
        List<Header> headers = new ArrayList<>();
        headers.add(new BasicHeader("Content-Type", "text/xml; charset=".concat(charset)));
        httpClientBuilder.setDefaultHeaders(headers);

        return httpClientBuilder.build();
    }


    protected boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    protected boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    protected String textValue(JsonNode jsonNode) {
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
