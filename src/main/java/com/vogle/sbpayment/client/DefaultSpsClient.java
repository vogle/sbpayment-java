package com.vogle.sbpayment.client;

import com.vogle.sbpayment.client.convert.SpsDataConverter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
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

    private final SpsClientSettings settings;
    private final HttpClient httpClient;
    private final XmlMapper xmlMapper;
    private final ObjectMapper objectMapper;

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
    public <T extends SpsRequest> T newRequest(Class<T> clazz) {
        try {
            T request = clazz.newInstance();

            request.setMerchantId(settings.getMerchantId());
            request.setServiceId(settings.getServiceId());
            request.setLimitSecond(settings.getAllowableSecondOnRequest());

            DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            dateFormat.setTimeZone(TimeZone.getTimeZone(settings.getTimeZone()));
            request.setRequestDate(dateFormat.format(new Date()));
            return request;
        } catch (InstantiationException | IllegalAccessException ex) {
            logger.error(ex.getMessage());
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends SpsResponse> SpsResponseEntity<T> execute(SpsRequest<T> request) {

        String charset = settings.getCharset();
        try {
            if (logger.isDebugEnabled()) {
                logger.debug("SPS Client request object : {}", request);
            }

            // 1. enable encrypted flag by settings
            if (settings.getCipherSets().isEnabled()) {
                SpsDataConverter.enableEncryptedFlg(request, request.getClass());
            }

            // 2. Insert a hashcode from request
            request.setSpsHashcode(SpsDataConverter.makeSpsHashCode(request, settings.getHashKey(),
                    settings.getCharset()));

            // 3. DES Encrypt & base64 encode
            String xmlRequest = objectToXml(request);

            if (logger.isTraceEnabled()) {
                logger.trace("SPS Client request XML : \n{}\n", xmlRequest);
            }

            // 4. HTTP Execute
            HttpPost method = new HttpPost(settings.getApiUrl());
            method.setEntity(new StringEntity(xmlRequest, charset));
            HttpResponse response = httpClient.execute(method);

            // Gets response status
            int statusCode = response.getStatusLine().getStatusCode();

            // success
            if (statusCode == 200) {
                // response header
                Map<String, String> headerMap = new HashMap<>();

                // response body
                String body = EntityUtils.toString(response.getEntity(), settings.getCharset());
                if (logger.isTraceEnabled()) {
                    logger.trace("SPS Client response header :{}", headerMap);
                    logger.trace("SPS Client response xml :\n{}\n", body);
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
                    logger.debug("SPS Client response object : {}", bodyObject);
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

        } catch (IOException ex) {
            logger.error("SPS Client Internal Error : {}({})",
                    ex.getClass().getSimpleName(), ex.getMessage());
        }
        return new SpsResponseEntity<>();
    }

    /**
     * XML convert to Object
     *
     * @param body        The receiving response body
     * @param objectClass The Converting object
     * @return The converted Object
     */
    private <T> T xmlToObject(String body, Class<T> objectClass) {

        try {
            // xml mapping
            T bodyObject = xmlMapper.readValue(body, objectClass);

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

    /**
     * Object convert to Xml
     *
     * @param value The Source object
     * @return The converted XML
     */
    private <T> String objectToXml(T value) {
        String charset = settings.getCharset();

        // make xml
        String xml = "<?xml version=\"1.0\" encoding=\"" + charset + "\"?>\n";

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
            xml = xml.concat(xmlMapper.writeValueAsString(value));

            return xml;

        } catch (JsonProcessingException ex) {
            logger.error("SPS objectToXml Error : {}({})", ex.getClass().getSimpleName(), ex.getMessage());
            throw new XmlMappingException(ex.getMessage(), ex);
        }
    }

    /**
     * Create HttpClient
     */
    private HttpClient httpClient(SpsClientSettings settings) {
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


    private boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    private boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

}
