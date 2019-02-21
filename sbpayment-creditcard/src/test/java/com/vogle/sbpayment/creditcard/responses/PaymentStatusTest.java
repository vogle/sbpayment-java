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

package com.vogle.sbpayment.creditcard.responses;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link PaymentStatus}
 *
 * @author Allan Im
 */
public class PaymentStatusTest {

    @Test
    public void status() {
        assertThat(PaymentStatus.status("1")).isEqualTo(PaymentStatus.AUTHORIZED);
        assertThat(PaymentStatus.status("2")).isEqualTo(PaymentStatus.CAPTURED);
        assertThat(PaymentStatus.status("3")).isEqualTo(PaymentStatus.CANCELED);
        assertThat(PaymentStatus.status("4")).isEqualTo(PaymentStatus.REFUNDED);
        assertThat(PaymentStatus.status("NONE")).isNull();
    }

    @Test
    public void getPaymentStatusCode() {
        assertThat(PaymentStatus.AUTHORIZED.getPaymentStatusCode()).endsWith("1");
        assertThat(PaymentStatus.CAPTURED.getPaymentStatusCode()).endsWith("2");
        assertThat(PaymentStatus.CANCELED.getPaymentStatusCode()).endsWith("3");
        assertThat(PaymentStatus.REFUNDED.getPaymentStatusCode()).endsWith("4");
    }
}