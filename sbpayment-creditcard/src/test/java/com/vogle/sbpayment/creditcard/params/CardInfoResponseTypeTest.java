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

package com.vogle.sbpayment.creditcard.params;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link CardInfoResponseType}
 *
 * @author Allan Im
 */
public class CardInfoResponseTypeTest {

    @Test
    public void code() {
        assertThat(CardInfoResponseType.NONE.code()).endsWith("0");
        assertThat(CardInfoResponseType.ALL_MASK.code()).endsWith("1");
        assertThat(CardInfoResponseType.LOWER4.code()).endsWith("2");
    }
}