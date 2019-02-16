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

import org.junit.Test;

import java.io.IOException;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link Sbpayment} & {@link DefaultSbpayment}
 *
 * @author Allan Im
 */
public class SbpaymentTest {

    @Test
    public void newInstance() {
        // when
        Sbpayment sbpayment = Sbpayment.newInstance();

        // then
        assertThat(sbpayment).isNotNull();
        assertThat(sbpayment.getMapper()).isNotNull();
        assertThat(sbpayment.getClient()).isNotNull();
        assertThat(sbpayment.getReceiver()).isNotNull();

        assertThat(sbpayment.toString()).isNotEmpty();
    }

    @Test
    public void newInstanceWithFilePath() {
        // when
        Sbpayment sbpayment = Sbpayment.newInstance("test.properties");

        // then
        assertThat(sbpayment).isNotNull();
        assertThat(sbpayment.getMapper()).isNotNull();
        assertThat(sbpayment.getClient()).isNotNull();
        assertThat(sbpayment.getReceiver()).isNotNull();

        assertThat(sbpayment.toString()).isNotEmpty();
    }

    @Test(expected = IllegalStateException.class)
    public void newInstanceWithNoProperties() {
        // when
        Sbpayment.newInstance("no.properties");
    }

    @Test
    public void newInstanceWithProperties() {
        // Given
        Properties properties = new Properties();
        try {
            properties.load(this.getClass().getClassLoader().getResourceAsStream("test.properties"));
        } catch (IOException ignored) {
        }

        // when
        Sbpayment sbpayment = Sbpayment.newInstance(properties);

        // then
        assertThat(sbpayment).isNotNull();
        assertThat(sbpayment.getMapper()).isNotNull();
        assertThat(sbpayment.getClient()).isNotNull();
        assertThat(sbpayment.getReceiver()).isNotNull();

    }

    @Test
    public void newInstanceWithSpsConfig() {

        // Given
        SpsConfig config = SpsConfig.builder()
                .apiUrl("http://vogle.com")
                .merchantId("XX002").serviceId("BBB")
                .basicAuthId("BASIC").basicAuthPassword("PASSWORD")
                .hashKey("HASH_KEY")
                .cipherEnabled(true)
                .desKey("DES_KEY").desInitKey("DES_INIT_KEY")
                .build();

        // when
        Sbpayment sbpayment = Sbpayment.newInstance(config);

        // then
        assertThat(sbpayment).isNotNull();
        assertThat(sbpayment.getMapper()).isNotNull();
        assertThat(sbpayment.getClient()).isNotNull();
        assertThat(sbpayment.getReceiver()).isNotNull();

    }
}