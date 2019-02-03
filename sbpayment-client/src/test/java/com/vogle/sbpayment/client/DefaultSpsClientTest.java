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

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link DefaultSpsClient}
 *
 * @author Allan Im
 */
public class DefaultSpsClientTest {


    @Test
    public void client() {
        // given
        String merchantId = "VOGLE Labs";
        String serviceId = "Allan Im";

        // when
        SpsClient client = client(merchantId, serviceId);

        // then
        assertThat(client).isNotNull();
    }

    @Test
    public void newRequest() {

        // given
        String merchantId = "VOGLE Labs";
        String serviceId = "Allan Im";
        SpsClient client = client(merchantId, serviceId);

        // when
        TestRequest request = client.newRequest(TestRequest.class);

        // then
        assertThat(request.getMerchantId()).isEqualTo(merchantId);
        assertThat(request.getServiceId()).isEqualTo(serviceId);
        assertThat(request.getLimitSecond()).isEqualTo(600);
        assertThat(request.getRequestDate()).isNotEmpty();

    }

    @Test(expected = IllegalArgumentException.class)
    public void newRequestWithFail() {

        // given
        String merchantId = "VOGLE Labs";
        String serviceId = "Allan Im";
        SpsClient client = client(merchantId, serviceId);

        // when
        client.newRequest(SpsRequest.class);

    }

    @Test
    public void execute() throws Exception {

        // given
        String merchantId = "VOGLE Labs";
        String serviceId = "Allan Im";
        SpsClient client = client(merchantId, serviceId);

        // when
        TestRequest request = client.newRequest(TestRequest.class);
        SpsResult responseEntity = client.execute(request);

        // then
        assertThat(responseEntity.getStatus()).isEqualTo(405);

    }

    private SpsConfig settings(String merchantId, String serviceId) {
        return SpsConfig.builder()
                .apiUrl("http://vogle.com")
                .merchantId(merchantId)
                .serviceId(serviceId)
                .basicAuthId("BASIC_ID")
                .basicAuthPassword("BASIC_PASS")
                .hashKey("HASH_KEY")
                .build();
    }

    private SpsClient client(String merchantId, String serviceId) {
        SpsManager manager = new DefaultSpsManager(settings(merchantId, serviceId));
        return manager.client();
    }

    public static class TestRequest implements SpsRequest<SpsResponse> {

        private String merchantId;
        private String serviceId;
        private Integer limitSecond;
        private String requestDate;

        @Override
        public String getId() {
            return null;
        }

        @Override
        public String getMerchantId() {
            return this.merchantId;
        }

        @Override
        public void setMerchantId(String merchantId) {
            this.merchantId = merchantId;
        }

        @Override
        public String getServiceId() {
            return this.serviceId;
        }

        @Override
        public void setServiceId(String serviceId) {
            this.serviceId = serviceId;
        }

        @Override
        public String getRequestDate() {
            return this.requestDate;
        }

        @Override
        public void setRequestDate(String requestDate) {
            this.requestDate = requestDate;
        }

        @Override
        public Integer getLimitSecond() {
            return this.limitSecond;
        }

        @Override
        public void setLimitSecond(Integer limitSecond) {
            this.limitSecond = limitSecond;
        }

        @Override
        public String getSpsHashcode() {
            return null;
        }

        @Override
        public void setSpsHashcode(String spsHashcode) {

        }

        @Override
        public Class<SpsResponse> responseClass() {
            return null;
        }
    }
}