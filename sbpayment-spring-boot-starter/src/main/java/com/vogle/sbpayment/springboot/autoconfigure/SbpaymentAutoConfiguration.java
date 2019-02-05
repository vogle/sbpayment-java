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

package com.vogle.sbpayment.springboot.autoconfigure;

import com.vogle.sbpayment.client.DefaultSpsManager;
import com.vogle.sbpayment.client.SpsClient;
import com.vogle.sbpayment.client.SpsConfig;
import com.vogle.sbpayment.client.SpsManager;
import com.vogle.sbpayment.creditcard.CreditCardPayment;
import com.vogle.sbpayment.creditcard.DefaultCreditCardPayment;
import com.vogle.sbpayment.payeasy.DefaultPayEasyPayment;
import com.vogle.sbpayment.payeasy.PayEasyPayment;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;
import java.util.Set;
import java.util.TimeZone;

import static com.vogle.sbpayment.creditcard.DefaultCreditCardPayment.Feature;
import static com.vogle.sbpayment.springboot.autoconfigure.SbpaymentProperties.Client;
import static com.vogle.sbpayment.springboot.autoconfigure.SbpaymentProperties.CreditCard;
import static com.vogle.sbpayment.springboot.autoconfigure.SbpaymentProperties.PayEasy;

/**
 * Softbank payment service auto configuration
 *
 * @author Allan Im
 **/
@Configuration
@ConditionalOnClass(SpsClient.class)
@ConditionalOnProperty(prefix = "vg.sbpayment.client", name = "api-url")
@EnableConfigurationProperties(SbpaymentProperties.class)
public class SbpaymentAutoConfiguration {

    /**
     * {@link SpsManager} in sbpayment-client
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "vg.sbpayment.client", name = {"merchant-id", "service-id", "hash-key"})
    public SpsManager spsManager(SbpaymentProperties properties) {
        Client client = properties.getClient();
        return createManager(client);
    }

    private SpsManager createManager(Client client) {
        return new DefaultSpsManager(SpsConfig.builder()
                .charset(client.getCharset())
                .timeZone(TimeZone.getTimeZone(client.getTimeZone()))
                .apiUrl(client.getApiUrl())
                .merchantId(client.getMerchantId())
                .serviceId(client.getServiceId())
                .basicAuthId(client.getBasicAuthId())
                .basicAuthPassword(client.getBasicAuthPassword())
                .hashKey(client.getHashKey())
                .enabledCipher(client.getCipherSets().isEnabled())
                .desKey(client.getCipherSets().getDesKey())
                .desInitKey(client.getCipherSets().getDesInitKey())
                .allowableSecondOnRequest(client.getAllowableSecondOnRequest())
                .build());
    }

    /**
     * {@link CreditCardPayment} in sbpayment-creditcard
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnClass(CreditCardPayment.class)
    @ConditionalOnBean(SpsManager.class)
    @ConditionalOnProperty(prefix = "vg.sbpayment.creditcard", name = "disabled", havingValue = "false",
            matchIfMissing = true)
    public CreditCardPayment creditCardPayment(SbpaymentProperties properties, SpsManager manager) {
        CreditCard creditCard = properties.getCreditcard();

        Set<Feature> enableFeatures = new HashSet<>();
        if (creditCard.isCustomerInfoReturn()) {
            enableFeatures.add(Feature.RETURN_CUSTOMER_INFO);
        }
        if (creditCard.isCardbrandReturn()) {
            enableFeatures.add(Feature.RETURN_CARD_BRAND);
        }

        if (creditCard.isAlternateClientEnabled()) {
            SpsManager privateManager = createManager(creditCard.getAlternateClient());
            return new DefaultCreditCardPayment(privateManager, enableFeatures.toArray(new Feature[0]));
        } else {
            return new DefaultCreditCardPayment(manager, enableFeatures.toArray(new Feature[0]));
        }
    }

    /**
     * {@link PayEasyPayment} in sbpayment-payeasy
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnClass(PayEasyPayment.class)
    @ConditionalOnBean(SpsManager.class)
    @ConditionalOnProperty(prefix = "vg.sbpayment.payeasy", name = "disabled", havingValue = "false",
            matchIfMissing = true)
    public PayEasyPayment payEasyPayment(SbpaymentProperties properties, SpsManager manager) {
        PayEasy payEasy = properties.getPayeasy();

        if (payEasy.isAlternateClientEnabled()) {
            SpsManager privateManager = createManager(payEasy.getAlternateClient());

            return createPayEasyService(privateManager, payEasy);
        } else {
            return createPayEasyService(manager, payEasy);
        }
    }

    private PayEasyPayment createPayEasyService(SpsManager manager, PayEasy payEasy) {
        if (PayEasy.Type.ONLINE.equals(payEasy.getType())) {
            return new DefaultPayEasyPayment(manager, payEasy.getBillInfo(), payEasy.getBillInfoKana());
        } else {
            return new DefaultPayEasyPayment(manager, payEasy.getPayCsv());
        }
    }
}
