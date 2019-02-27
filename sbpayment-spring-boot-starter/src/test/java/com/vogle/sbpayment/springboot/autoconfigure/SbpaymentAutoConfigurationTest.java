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

package com.vogle.sbpayment.springboot.autoconfigure;

import com.vogle.sbpayment.client.Sbpayment;
import com.vogle.sbpayment.creditcard.CreditCardPayment;
import com.vogle.sbpayment.payeasy.PayEasyPayment;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link SbpaymentAutoConfiguration}
 *
 * @author Allan Im
 **/
public class SbpaymentAutoConfigurationTest {

    private Set<String> required = new HashSet<>();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private AnnotationConfigApplicationContext context;

    @Before
    public void init() {
        required.add("sbpayment.client.api-url:http://test.vogle.com");
        required.add("sbpayment.client.hash-key:HASH_KEY");
        required.add("sbpayment.client.merchant-id:MERCHANT_ID");
        required.add("sbpayment.client.service-id:SERVICE_ID");

        this.context = new AnnotationConfigApplicationContext();
    }

    @After
    public void close() {
        if (this.context != null) {
            this.context.close();
        }
    }

    @Test
    public void checkEmptyBean() {
        this.context.register(SbpaymentAutoConfiguration.class);
        this.context.refresh();

        // check bean
        assertThat(this.context.getBeanNamesForType(SbpaymentProperties.class)).hasSize(0);
        assertThat(this.context.getBeanNamesForType(Sbpayment.class)).hasSize(0);
    }

    @Test
    public void checkPropertiesEnabled() {
        TestPropertyValues.of("sbpayment.client.api-url:http://test.vogle.com").applyTo(this.context);
        this.context.register(SbpaymentAutoConfiguration.class);
        this.context.refresh();

        // check bean
        assertThat(this.context.getBeanNamesForType(SbpaymentProperties.class)).hasSize(1);

        assertThat(this.context.getBeanNamesForType(Sbpayment.class)).hasSize(0);
    }

    @Test
    public void checkSbpaymentEnabled() {
        TestPropertyValues.of(required).applyTo(this.context);
        this.context.register(SbpaymentAutoConfiguration.class);
        this.context.refresh();

        // check bean
        assertThat(this.context.getBeanNamesForType(SbpaymentProperties.class)).hasSize(1);
        assertThat(this.context.getBeanNamesForType(Sbpayment.class)).hasSize(1);
    }


    @Test
    public void checkAllServiceEnabled() {
        TestPropertyValues.of(required).applyTo(this.context);
        this.context.register(SbpaymentAutoConfiguration.class);
        this.context.refresh();

        // check bean
        assertThat(this.context.getBeanNamesForType(CreditCardPayment.class)).hasSize(1);
        assertThat(this.context.getBeanNamesForType(PayEasyPayment.class)).hasSize(1);
    }

    @Test
    public void checkCreditCardDisabled() {
        required.add("sbpayment.creditcard.enabled:false");
        TestPropertyValues.of(required).applyTo(this.context);
        this.context.register(SbpaymentAutoConfiguration.class);
        this.context.refresh();

        // check bean
        assertThat(this.context.getBeanNamesForType(CreditCardPayment.class)).hasSize(0);
    }

    @Test
    public void checkCreditCardAddOptions() {
        required.add("sbpayment.creditcard.customerInfoReturn:true");
        required.add("sbpayment.creditcard.cardbrandReturn:true");
        required.add("sbpayment.creditcard.alternateClientEnabled:true");
        required.add("sbpayment.creditcard.alternateClient.api-url:http://test.vogle.com");
        required.add("sbpayment.creditcard.alternateClient.hash-key:HASH_KEY");
        required.add("sbpayment.creditcard.alternateClient.merchant-id:MERCHANT_ID");
        required.add("sbpayment.creditcard.alternateClient.service-id:SERVICE_ID");
        TestPropertyValues.of(required).applyTo(this.context);
        this.context.register(SbpaymentAutoConfiguration.class);
        this.context.refresh();

        // check bean
        assertThat(this.context.getBeanNamesForType(CreditCardPayment.class)).hasSize(1);
    }

    @Test
    public void checkPayEasyDisabled() {
        required.add("sbpayment.payeasy.enabled:false");
        TestPropertyValues.of(required).applyTo(this.context);
        this.context.register(SbpaymentAutoConfiguration.class);
        this.context.refresh();

        // check bean
        assertThat(this.context.getBeanNamesForType(PayEasyPayment.class)).hasSize(0);
    }

    @Test
    public void checkPayEasyAddOptions() {
        required.add("sbpayment.payeasy.type:LINK");
        required.add("sbpayment.payeasy.pay-csv:0000");
        required.add("sbpayment.payeasy.alternateClientEnabled:true");
        required.add("sbpayment.payeasy.alternateClient.api-url:http://test.vogle.com");
        required.add("sbpayment.payeasy.alternateClient.hash-key:HASH_KEY");
        required.add("sbpayment.payeasy.alternateClient.merchant-id:MERCHANT_ID");
        required.add("sbpayment.payeasy.alternateClient.service-id:SERVICE_ID");
        TestPropertyValues.of(required).applyTo(this.context);
        this.context.register(SbpaymentAutoConfiguration.class);
        this.context.refresh();

        // check bean
        assertThat(this.context.getBeanNamesForType(CreditCardPayment.class)).hasSize(1);
    }

}
