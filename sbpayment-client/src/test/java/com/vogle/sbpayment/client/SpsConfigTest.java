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

import javax.validation.ConstraintViolationException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link SpsConfig}
 *
 * @author Allan Im
 */
public class SpsConfigTest {

    @Test
    public void fromProperties() {

        // Given
        Properties properties = new Properties();
        try {
            properties.load(Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream("config_test.properties"));
        } catch (IOException ignored) {
            // ignored
        }

        // when
        SpsConfig config = SpsConfig.from(properties);

        // then
        assertThat(config).isNotNull();
        assertThat(config.getCipherInfo()).isNotNull();
        assertThat(config.getCipherInfo().getHashKey()).isEqualTo("1234567890_HASH_KEY");
        assertThat(config.getClientInfo()).isNotNull();
        assertThat(config.getClientInfo().getApiUrl()).isEqualTo("https://vogle.com");
        assertThat(config.getSpsInfo()).isNotNull();
        assertThat(config.getSpsInfo().getMerchantId()).isEqualTo("XX002");

    }

    @Test(expected = ConstraintViolationException.class)
    public void getSpsInfoWithInvalidData() {
        SpsConfig config = SpsConfig.builder().build();
        config.getSpsInfo();
    }

    @Test(expected = ConstraintViolationException.class)
    public void getClientInfoWithInvalidData() {
        SpsConfig config = SpsConfig.builder().build();
        config.getClientInfo();
    }

    @Test(expected = IllegalArgumentException.class)
    public void getCipherInfoWithInvalidData() {
        SpsConfig config = SpsConfig.builder().hashKey("HASH").cipherEnabled(true).build();
        config.getCipherInfo();
    }
}