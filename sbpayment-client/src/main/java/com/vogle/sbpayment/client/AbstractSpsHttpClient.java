package com.vogle.sbpayment.client;

import org.apache.http.Header;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Creates Http Client and Sets SpsSettings
 *
 * @author Allan Im
 **/
public abstract class AbstractSpsHttpClient implements SpsClient {

    protected final SpsSettings settings;
    protected final HttpClient httpClient;

    protected AbstractSpsHttpClient(SpsSettings settings) {
        this.settings = settings;
        this.httpClient = httpClient(settings);
    }

    /**
     * Create HttpClient
     */
    private HttpClient httpClient(SpsSettings settings) {
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

    /**
     * {@inheritDoc}
     */
    @Override
    public SpsSettings getSettings() {
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

    private boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    private boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }
}
