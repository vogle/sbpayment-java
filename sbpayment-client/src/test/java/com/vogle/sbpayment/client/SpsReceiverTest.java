/*
 * Copyright 2019 Vogle Labs.
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

import com.vogle.sbpayment.client.receivers.SpsReceivedData;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link DefaultSpsReceiver}
 *
 * @author Allan Im
 */
public class SpsReceiverTest {

    @Test
    public void receive() throws Exception {

        // given
        String merchant = "MERCHANT";
        String service = "SERVICE";
        String hashKey = "HASH_KEY_2020";
        String hashCode = "423e7bc6ca2207d56563eb35fb27ee335ac07cb9"; // have to make real hash code
        SpsConfig config = config(merchant, service, hashKey);
        SpsReceiver receiver = new DefaultSpsReceiver(config.getSpsInfo(), mapper(config.getCipherInfo()));

        // when
        TestReceive receive = receiver.receive("<sps-api-request id=\"NT01-00103-703\">\n" +
                "<merchant_id>" + merchant + "</merchant_id>\n" +
                "<service_id>" + service + "</service_id>\n" +
                "<sps_transaction_id>xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx</sps_transaction_id>\n" +
                "<tracking_id>12345678901234</tracking_id>\n" +
                "<rec_datetime>20091010</rec_datetime>\n" +
                "<request_date>20091010125959</request_date>\n" +
                "<sps_hashcode>" + hashCode + "</sps_hashcode>\n" +
                "</sps-api-request>", TestReceive.class);

        // then
        assertThat(receive).isNotNull();
        assertThat(receive.getMerchantId()).isEqualTo(merchant);
        assertThat(receive.getServiceId()).isEqualTo(service);
    }

    @Test(expected = InvalidAccessException.class)
    public void receiveWithInvalidMerchantId() throws Exception {

        // given
        String merchant = "MERCHANT";
        String service = "SERVICE";
        String hashKey = "HASH_KEY_2020";
        SpsConfig config = config(merchant, service, hashKey);
        SpsReceiver receiver = new DefaultSpsReceiver(config.getSpsInfo(), mapper(config.getCipherInfo()));

        // when
        receiver.receive("<sps-api-request id=\"NT01-00103-703\">\n" +
                "<merchant_id>wrong</merchant_id>\n" +
                "</sps-api-request>", TestReceive.class);
    }

    @Test(expected = InvalidAccessException.class)
    public void receiveWithInvalidServiceId() throws Exception {

        // given
        String merchant = "MERCHANT";
        String service = "SERVICE";
        String hashKey = "HASH_KEY_2020";
        SpsConfig config = config(merchant, service, hashKey);
        SpsReceiver receiver = new DefaultSpsReceiver(config.getSpsInfo(), mapper(config.getCipherInfo()));

        // when
        receiver.receive("<sps-api-request id=\"NT01-00103-703\">\n" +
                "<merchant_id>" + merchant + "</merchant_id>\n" +
                "<service_id>wrong</service_id>\n" +
                "</sps-api-request>", TestReceive.class);
    }

    @Test(expected = InvalidAccessException.class)
    public void receiveWithInvalidHashCode() throws Exception {

        // given
        String merchant = "MERCHANT";
        String service = "SERVICE";
        String hashKey = "HASH_KEY_2020";
        SpsConfig config = config(merchant, service, hashKey);
        SpsReceiver receiver = new DefaultSpsReceiver(config.getSpsInfo(), mapper(config.getCipherInfo()));

        // when
        receiver.receive("<sps-api-request id=\"NT01-00103-703\">\n" +
                "<merchant_id>" + merchant + "</merchant_id>\n" +
                "<service_id>" + service + "</service_id>\n" +
                "<sps_hashcode>wrong</sps_hashcode>\n" +
                "</sps-api-request>", TestReceive.class);
    }

    @Test
    public void resultSuccessful() {
        // given
        String merchant = "MERCHANT";
        String service = "SERVICE";
        String hashKey = "HASH_KEY_2020";
        SpsConfig config = config(merchant, service, hashKey);
        SpsReceiver receiver = new DefaultSpsReceiver(config.getSpsInfo(), mapper(config.getCipherInfo()));

        // when
        String xml = receiver.resultSuccessful("TEST_ID");

        // then
        assertThat(xml).isNotNull();
        assertThat(xml).contains("TEST_ID");
        assertThat(xml).contains("OK");
    }

    @Test
    public void resultFailed() {
        // given
        String merchant = "MERCHANT";
        String service = "SERVICE";
        String hashKey = "HASH_KEY_2020";
        SpsConfig config = config(merchant, service, hashKey);
        SpsReceiver receiver = new DefaultSpsReceiver(config.getSpsInfo(), mapper(config.getCipherInfo()));

        // when
        String xml = receiver.resultFailed("TEST_ID2", "MESSAGE");

        // then
        assertThat(xml).isNotNull();
        System.out.println(xml);
        assertThat(xml).contains("TEST_ID2");
        assertThat(xml).contains("NG");
        assertThat(xml).contains("res_err_msg");
    }

    private SpsConfig config(String merchant, String service, String hashKey) {
        return SpsConfig.builder()
                .apiUrl("http://vogle.com")
                .merchantId(merchant)
                .serviceId(service)
                .hashKey(hashKey)
                .cipherEnabled(true)
                .desKey("DES_KEY")
                .desInitKey("INIT_KEY")
                .build();
    }

    private SpsMapper mapper(SpsConfig.CipherInfo cipherInfo) {
        return new DefaultSpsMapper(cipherInfo);
    }


    @Data
    private static class TestReceive implements SpsReceivedData {

        @JacksonXmlProperty(isAttribute = true)
        private String id;

        @JacksonXmlProperty(localName = "merchant_id")
        private String merchantId;

        @JacksonXmlProperty(localName = "service_id")
        private String serviceId;

        @JacksonXmlProperty(localName = "sps_transaction_id")
        private String spsTransactionId;

        @JacksonXmlProperty(localName = "tracking_id")
        private String trackingId;

        @JacksonXmlProperty(localName = "rec_datetime")
        private String recDatetime;

        @JacksonXmlProperty(localName = "request_date")
        private String requestDate;

        @JacksonXmlProperty(localName = "sps_hashcode")
        private String spsHashcode;

    }

}