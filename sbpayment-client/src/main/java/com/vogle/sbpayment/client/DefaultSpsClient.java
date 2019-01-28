package com.vogle.sbpayment.client;

import com.vogle.sbpayment.client.requests.SpsRequest;
import com.vogle.sbpayment.client.responses.SpsResponse;

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
import org.apache.http.util.Asserts;
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

/**
 * Default Softbank payment client
 *
 * @author Allan Im
 **/
public class DefaultSpsClient implements SpsClient {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final SpsClientSettings settings;
    private final HttpClient httpClient;
    private final SpsMapper mapper;

    public DefaultSpsClient(SpsClientSettings settings, SpsMapper mapper) {
        Asserts.notNull(settings, "The Settings");
        SpsValidator.beanValidate(settings);

        this.settings = settings;
        this.mapper = mapper;
        this.httpClient = createHttpClient();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends SpsRequest> T newRequest(Class<T> clazz) {
        Asserts.notNull(clazz, "The Class");

        try {
            T request = clazz.newInstance();

            request.setMerchantId(settings.getMerchantId());
            request.setServiceId(settings.getServiceId());
            request.setLimitSecond(settings.getAllowableSecondOnRequest());

            DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            dateFormat.setTimeZone(settings.getTimeZone());
            request.setRequestDate(dateFormat.format(new Date()));
            return request;
        } catch (InstantiationException | IllegalAccessException ex) {
            logger.error(ex.getMessage());
            throw new IllegalArgumentException();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends SpsResponse> SpsResult<T> execute(SpsRequest<T> request) {
        Asserts.notNull(request, "The request");

        String charset = mapper.getCharset();
        try {
            if (logger.isDebugEnabled()) {
                logger.debug("SPS Client request object : {}", request);
            }

            // make xml
            String xmlRequest = mapper.requestToXml(request);
            if (logger.isTraceEnabled()) {
                logger.trace("SPS Client request XML : \n{}\n", xmlRequest);
            }

            // HTTP Execute
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
                String body = EntityUtils.toString(response.getEntity(), mapper.getCharset());
                if (logger.isTraceEnabled()) {
                    logger.trace("SPS Client response header :{}", headerMap);
                    logger.trace("SPS Client response xml :\n{}\n", body);
                }

                // xml mapping
                T bodyObject = mapper.xmlToObject(body, request.responseClass());

                if (logger.isInfoEnabled()) {
                    logger.info("SPS Client ({}) response : result = {}{}{}",
                            bodyObject.getId() == null ? "FAIL" : bodyObject.getDescription(),
                            bodyObject.getResult(),
                            "OK".equalsIgnoreCase(bodyObject.getResult()) ? "" : ", errCode = " + bodyObject.getErrCode(),
                            bodyObject.getSpsTransactionId() == null ? "" : ", sps-transaction-id = "
                                    + bodyObject.getSpsTransactionId());
                }

                if (logger.isDebugEnabled()) {
                    logger.debug("SPS Client response object : {}", bodyObject);
                }

                return new SpsResult<>(statusCode, headerMap, bodyObject);

            } else if (statusCode == 401) {
                logger.error("SPS Client connect fail : Either you supplied the wrong credentials,"
                                + " authId : {}, authPw : {} ",
                        settings.getBasicAuthId(), settings.getBasicAuthPassword());
                return new SpsResult<>(statusCode);
            } else if (statusCode == 403) {
                logger.error("SPS Client connect fail : You don't have permission to access {} on this server",
                        settings.getApiUrl());
                return new SpsResult<>(statusCode);
            } else if (statusCode == 404) {
                logger.error("SPS Client connect fail : The requested URL {} was not found on this server",
                        settings.getApiUrl());
                return new SpsResult<>(statusCode);
            } else if (statusCode == 500) {
                logger.error("SPS Client connect fail : Internal server error from {}",
                        settings.getApiUrl());
                return new SpsResult<>(statusCode);
            } else if (statusCode == 503) {
                logger.error("SPS Client connect fail : The server is temporarily unable to service your request"
                        + " due to maintenance downtime");
                return new SpsResult<>(statusCode);
            } else {
                logger.error("SPS Client connect fail : HTTP Status {}", statusCode);
                return new SpsResult<>(statusCode);
            }

        } catch (IOException ex) {
            logger.error("SPS Client Internal Error : {}({})",
                    ex.getClass().getSimpleName(), ex.getMessage());
        }
        return new SpsResult<>();
    }

    /**
     * Create HttpClient
     */
    private HttpClient createHttpClient() {
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
        String charset = mapper.getCharset();
        if (isEmpty(charset)) {
            charset = "Shift_JIS";
        }
        List<Header> headers = new ArrayList<>();
        headers.add(new BasicHeader("Content-Type", "text/xml; charset=" + charset));
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
