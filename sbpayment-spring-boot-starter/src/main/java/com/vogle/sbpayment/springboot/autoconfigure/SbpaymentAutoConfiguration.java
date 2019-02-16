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

import com.vogle.sbpayment.client.DefaultSbpayment;
import com.vogle.sbpayment.client.Sbpayment;
import com.vogle.sbpayment.client.SpsClient;
import com.vogle.sbpayment.client.SpsConfig;
import com.vogle.sbpayment.creditcard.CardPayFeature;
import com.vogle.sbpayment.creditcard.CreditCardPayment;
import com.vogle.sbpayment.creditcard.DefaultCreditCardPayment;
import com.vogle.sbpayment.payeasy.LinkType;
import com.vogle.sbpayment.payeasy.OnlineType;
import com.vogle.sbpayment.payeasy.PayEasyPayment;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.TimeZone;

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
@ConditionalOnProperty(prefix = "sbpayment.client", name = "api-url")
@EnableConfigurationProperties(SbpaymentProperties.class)
public class SbpaymentAutoConfiguration {

    /**
     * {@link Sbpayment} in sbpayment-getClient
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "sbpayment.client", name = {"merchant-id", "service-id", "hash-key"})
    public Sbpayment sbpayment(SbpaymentProperties properties) {
        Client client = properties.getClient();
        return createSbpayment(client);
    }

    private Sbpayment createSbpayment(Client client) {
        return Sbpayment.newInstance(SpsConfig.builder()
                .charset(Charset.forName(client.getCharset()))
                .timeZone(TimeZone.getTimeZone(client.getTimeZone()))
                .apiUrl(client.getApiUrl())
                .merchantId(client.getMerchantId())
                .serviceId(client.getServiceId())
                .basicAuthId(client.getBasicAuthId())
                .basicAuthPassword(client.getBasicAuthPassword())
                .hashKey(client.getHashKey())
                .cipherEnabled(client.getCipherSets().isEnabled())
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
    @ConditionalOnBean(Sbpayment.class)
    @ConditionalOnProperty(prefix = "sbpayment.creditcard", name = "disabled", havingValue = "false",
            matchIfMissing = true)
    public CreditCardPayment creditCardPayment(SbpaymentProperties properties, Sbpayment sbpayment) {
        CreditCard creditCard = properties.getCreditcard();

        Set<CardPayFeature> enableFeatures = new HashSet<>();
        if (creditCard.isCustomerInfoReturn()) {
            enableFeatures.add(CardPayFeature.RETURN_CUSTOMER_INFO);
        }
        if (creditCard.isCardbrandReturn()) {
            enableFeatures.add(CardPayFeature.RETURN_CARD_BRAND);
        }

        if (creditCard.isAlternateClientEnabled()) {
            Sbpayment privatePayment = createSbpayment(creditCard.getAlternateClient());
            return CreditCardPayment.newInstance(privatePayment, enableFeatures.toArray(new CardPayFeature[0]));
        } else {
            return CreditCardPayment.newInstance(sbpayment, enableFeatures.toArray(new CardPayFeature[0]));
        }
    }

    /**
     * {@link PayEasyPayment} in sbpayment-payeasy
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnClass(PayEasyPayment.class)
    @ConditionalOnBean(Sbpayment.class)
    @ConditionalOnProperty(prefix = "sbpayment.payeasy", name = "disabled", havingValue = "false",
            matchIfMissing = true)
    public PayEasyPayment payEasyPayment(SbpaymentProperties properties, Sbpayment sbpayment) {
        PayEasy payEasy = properties.getPayeasy();

        if (payEasy.isAlternateClientEnabled()) {
            Sbpayment privatePayment = createSbpayment(payEasy.getAlternateClient());
            return createPayEasyService(privatePayment, payEasy);
        } else {
            return createPayEasyService(sbpayment, payEasy);
        }
    }

    private PayEasyPayment createPayEasyService(Sbpayment sbpayment, PayEasy payEasy) {
        if (PayEasy.Type.ONLINE.equals(payEasy.getType())) {
            OnlineType onlineType = new OnlineType();
            onlineType.setBillInfo(payEasy.getBillInfo());
            onlineType.setBillInfoKana(payEasy.getBillInfoKana());
            onlineType.setBillLimitDay(payEasy.getBillLimitDay());
            return PayEasyPayment.newInstance(sbpayment, onlineType);
        } else {
            LinkType linkType = new LinkType();
            linkType.setPayCsv(payEasy.getPayCsv());
            linkType.setBillLimitDay(payEasy.getBillLimitDay());
            return PayEasyPayment.newInstance(sbpayment, linkType);
        }
    }
}
