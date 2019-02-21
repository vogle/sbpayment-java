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

package com.vogle.sbpayment.creditcard;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link DealingsType}
 *
 * @author Allan Im
 */
public class DealingsTypeTest {

    @Test
    public void type() {
        assertThat(DealingsType.type("10")).isEqualTo(DealingsType.LUMP_SUM);
        assertThat(DealingsType.type("61")).isEqualTo(DealingsType.INSTALLMENT);
        assertThat(DealingsType.type("21")).isEqualTo(DealingsType.BONUS_LUMP_SUM);
        assertThat(DealingsType.type("80")).isEqualTo(DealingsType.REVOLVING);
        assertThat(DealingsType.type("NONE")).isEqualTo(DealingsType.LUMP_SUM);
    }

    @Test
    public void code() {
        assertThat(DealingsType.LUMP_SUM.code()).endsWith("10");
        assertThat(DealingsType.INSTALLMENT.code()).endsWith("61");
        assertThat(DealingsType.BONUS_LUMP_SUM.code()).endsWith("21");
        assertThat(DealingsType.REVOLVING.code()).endsWith("80");
    }
}