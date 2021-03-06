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
 * Tests for {@link CreditCardBrand}
 *
 * @author Allan Im
 */
public class CreditCardBrandTest {

    @Test
    public void brand() {
        assertThat(CreditCardBrand.brand("V")).isEqualTo(CreditCardBrand.VISA);
        assertThat(CreditCardBrand.brand("M")).isEqualTo(CreditCardBrand.MASTER);
        assertThat(CreditCardBrand.brand("J")).isEqualTo(CreditCardBrand.JCB);
        assertThat(CreditCardBrand.brand("A")).isEqualTo(CreditCardBrand.AMEX);
        assertThat(CreditCardBrand.brand("D")).isEqualTo(CreditCardBrand.DINERS);
        assertThat(CreditCardBrand.brand("X")).isEqualTo(CreditCardBrand.OTHER);
        assertThat(CreditCardBrand.brand("NONE")).isEqualTo(CreditCardBrand.OTHER);
    }

    @Test
    public void getSpsCardbrandCode() {
        assertThat(CreditCardBrand.VISA.getSpsCardbrandCode()).endsWith("V");
        assertThat(CreditCardBrand.MASTER.getSpsCardbrandCode()).endsWith("M");
        assertThat(CreditCardBrand.JCB.getSpsCardbrandCode()).endsWith("J");
        assertThat(CreditCardBrand.AMEX.getSpsCardbrandCode()).endsWith("A");
        assertThat(CreditCardBrand.DINERS.getSpsCardbrandCode()).endsWith("D");
        assertThat(CreditCardBrand.OTHER.getSpsCardbrandCode()).endsWith("X");
    }
}