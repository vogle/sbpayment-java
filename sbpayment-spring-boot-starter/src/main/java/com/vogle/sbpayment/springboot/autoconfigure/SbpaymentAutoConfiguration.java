package com.vogle.sbpayment.springboot.autoconfigure;

import static com.vogle.sbpayment.creditcard.DefaultCreditCardPayment.Feature;
import static com.vogle.sbpayment.springboot.autoconfigure.SbpaymentProperties.Client;
import static com.vogle.sbpayment.springboot.autoconfigure.SbpaymentProperties.CreditCard;
import static com.vogle.sbpayment.springboot.autoconfigure.SbpaymentProperties.PayEasy;

import java.util.HashSet;
import java.util.Set;
import java.util.TimeZone;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.vogle.sbpayment.client.DefaultSpsManager;
import com.vogle.sbpayment.client.SpsClient;
import com.vogle.sbpayment.client.SpsConfig;
import com.vogle.sbpayment.client.SpsManager;
import com.vogle.sbpayment.client.SpsReceiver;
import com.vogle.sbpayment.creditcard.CreditCardPayment;
import com.vogle.sbpayment.creditcard.DefaultCreditCardPayment;
import com.vogle.sbpayment.payeasy.DefaultPayEasyPayment;
import com.vogle.sbpayment.payeasy.PayEasyPayment;

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
     * sbpayment-creditcard
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
     * sbpayment-payeasy
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
