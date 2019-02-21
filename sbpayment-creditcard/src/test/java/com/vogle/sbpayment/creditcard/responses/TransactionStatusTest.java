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
 * Tests for {@link TransactionStatus}
 *
 * @author Allan Im
 */
public class TransactionStatusTest {

    @Test
    public void status() {
        assertThat(TransactionStatus.status("0")).isEqualTo(TransactionStatus.NORMAL);
        assertThat(TransactionStatus.status("1")).isEqualTo(TransactionStatus.ERROR);
        assertThat(TransactionStatus.status("NONE")).isEqualTo(TransactionStatus.ERROR);
    }

    @Test
    public void getTransactionStatusCode() {
        assertThat(TransactionStatus.NORMAL.getTransactionStatusCode()).endsWith("0");
        assertThat(TransactionStatus.ERROR.getTransactionStatusCode()).endsWith("1");
    }
}