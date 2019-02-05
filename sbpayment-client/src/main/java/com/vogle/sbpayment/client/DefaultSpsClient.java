/*
 * Copyright 2019 VOGLE Labs.
 *
 * This file is part of sbpayment-java - Sbpayment client.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultSpsClient.class);

    private final SpsConfig config;
    private final HttpClient httpClient;
    private final SpsMapper mapper;

    /**
     * Create Client
     *
     * @param config The Softbank Payment configuration
     * @param mapper The {@link SpsMapper}
     */
    public DefaultSpsClient(SpsConfig config, SpsMapper mapper) {
        Asserts.notNull(config, "The Configuration");
        ValidationHelper.beanValidate(config);

        this.config = config;
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

            request.setMerchantId(config.getMerchantId());
            request.setServiceId(config.getServiceId());
            request.setLimitSecond(config.getAllowableSecondOnRequest());

            DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            dateFormat.setTimeZone(config.getTimeZone());
            request.setRequestDate(dateFormat.format(new Date()));
            return request;
        } catch (InstantiationException | IllegalAccessException ex) {
            LOGGER.error(ex.getMessage());
            throw new IllegalArgumentException(ex);
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
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("SPS Client request object : {}", request);
            }

            // make xml
            String xmlRequest = mapper.requestToXml(request);
            if (LOGGER.isTraceEnabled()) {
                LOGGER.trace("SPS Client request XML : \n{}\n", xmlRequest);
            }

            // HTTP Execute
            HttpPost method = new HttpPost(config.getApiUrl());
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
                if (LOGGER.isTraceEnabled()) {
                    LOGGER.trace("SPS Client response header :{}", headerMap);
                    LOGGER.trace("SPS Client response xml :\n{}\n", body);
                }

                // xml mapping
                T bodyObject = mapper.xmlToObject(body, request.responseClass());

                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info("SPS Client ({}) response : result = {}{}{}",
                            bodyObject.getId() == null ? "FAIL" : bodyObject.getDescription(),
                            bodyObject.getResult(),
                            "OK".equalsIgnoreCase(bodyObject.getResult()) ? "" : ", errCode = "
                                    + bodyObject.getErrCode(),
                            bodyObject.getSpsTransactionId() == null ? "" : ", sps-transaction-id = "
                                    + bodyObject.getSpsTransactionId());
                }

                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("SPS Client response object : {}", bodyObject);
                }

                return new SpsResult<>(statusCode, headerMap, bodyObject);

            } else {
                return failedResult(statusCode);
            }

        } catch (IOException ex) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("SPS Client Internal Error : {}({})",
                        ex.getClass().getSimpleName(), ex.getMessage());
            }
        }
        return new SpsResult<>();
    }

    /**
     * create filed result
     */
    private <T extends SpsResponse> SpsResult<T> failedResult(int statusCode) {
        if (LOGGER.isErrorEnabled()) {
            switch (statusCode) {
                case 401:
                    LOGGER.error("SPS Client connect fail : Either you supplied the wrong credentials,"
                                    + " authId : {}, authPw : {} ",
                            config.getBasicAuthId(), config.getBasicAuthPassword());
                    break;
                case 403:
                    LOGGER.error("SPS Client connect fail : You don't have permission to access {} on this server",
                            config.getApiUrl());
                    break;
                case 404:
                    LOGGER.error("SPS Client connect fail : The requested URL {} was not found on this server",
                            config.getApiUrl());
                    break;
                case 500:
                    LOGGER.error("SPS Client connect fail : Internal server error from {}",
                            config.getApiUrl());
                    break;
                case 503:
                    LOGGER.error("SPS Client connect fail : The server is temporarily unable to service your request"
                            + " due to maintenance downtime");
                    break;
                default:
                    LOGGER.error("SPS Client connect fail : HTTP Status {}", statusCode);
                    break;
            }
        }
        return new SpsResult<>(statusCode);
    }

    /**
     * Create HttpClient
     */
    private HttpClient createHttpClient() {
        // http client
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();

        // basic authorize information
        if (isNotEmpty(config.getBasicAuthId()) && isNotEmpty(config.getBasicAuthPassword())) {
            CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials(AuthScope.ANY,
                    new UsernamePasswordCredentials(config.getBasicAuthId(), config.getBasicAuthPassword()));
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
