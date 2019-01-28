package com.vogle.sbpayment.springboot.autoconfigure;

import com.vogle.sbpayment.client.DefaultSpsClient;
import com.vogle.sbpayment.client.DefaultSpsMapper;
import com.vogle.sbpayment.client.DefaultSpsReceiver;
import com.vogle.sbpayment.client.SpsClient;
import com.vogle.sbpayment.client.SpsClientSettings;
import com.vogle.sbpayment.client.SpsMapper;
import com.vogle.sbpayment.client.SpsReceiver;
import com.vogle.sbpayment.creditcard.CreditCardService;
import com.vogle.sbpayment.creditcard.DefaultCreditCardService;
import com.vogle.sbpayment.payeasy.DefaultPayEasyService;
import com.vogle.sbpayment.payeasy.PayEasyService;

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

import static com.vogle.sbpayment.creditcard.DefaultCreditCardService.Feature;
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
     * sbpayment-client: Mapper
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "vg.sbpayment.client", name = "hash-key")
    public SpsMapper spsMapper(SbpaymentProperties properties) {
        Client client = properties.getClient();
        return createSpsMapper(client);
    }

    private SpsMapper createSpsMapper(Client client) {
        Client.CipherSets cipher = client.getCipherSets();
        if (cipher.isEnabled()) {
            return new DefaultSpsMapper(client.getHashKey(), cipher.getDesKey(), cipher.getDesInitKey());
        } else {
            return new DefaultSpsMapper(client.getHashKey());
        }
    }

    /**
     * sbpayment-client: Client
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(SpsMapper.class)
    @ConditionalOnProperty(prefix = "vg.sbpayment.client", name = {"merchant-id", "service-id"})
    public SpsClient spsClient(SbpaymentProperties properties, SpsMapper spsMapper) {
        SpsClientSettings settings = mapSettings(properties.getClient());
        return new DefaultSpsClient(settings, spsMapper);
    }

    /**
     * sbpayment-client: Receiver
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(SpsMapper.class)
    @ConditionalOnProperty(prefix = "vg.sbpayment.client", name = {"merchant-id", "service-id"})
    public SpsReceiver spsReceiver(SbpaymentProperties properties, SpsMapper spsMapper) {
        Client client = properties.getClient();
        return new DefaultSpsReceiver(client.getMerchantId(), client.getServiceId(), spsMapper);
    }

    private SpsClientSettings mapSettings(Client client) {
        return SpsClientSettings.builder()
                .timeZone(TimeZone.getTimeZone(client.getTimeZone()))
                .apiUrl(client.getApiUrl())
                .merchantId(client.getMerchantId())
                .serviceId(client.getServiceId())
                .basicAuthId(client.getBasicAuthId())
                .basicAuthPassword(client.getBasicAuthPassword())
                .allowableSecondOnRequest(client.getAllowableSecondOnRequest())
                .build();
    }

    /**
     * sbpayment-creditcard
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnClass(CreditCardService.class)
    @ConditionalOnBean(SpsClient.class)
    @ConditionalOnProperty(prefix = "vg.sbpayment.creditcard", name = "disabled", havingValue = "false",
            matchIfMissing = true)
    public CreditCardService creditCardService(SbpaymentProperties properties, SpsClient client) {
        CreditCard creditCard = properties.getCreditcard();

        Set<Feature> enableFeatures = new HashSet<>();
        if (creditCard.isCustomerInfoReturn()) {
            enableFeatures.add(Feature.RETURN_CUSTOMER_INFO);
        }
        if (creditCard.isCardbrandReturn()) {
            enableFeatures.add(Feature.RETURN_CARD_BRAND);
        }

        if (creditCard.isAlternateClientEnabled()) {
            SpsMapper privateMapper = createSpsMapper(creditCard.getAlternateClient());
            SpsClient privateClient = new DefaultSpsClient(mapSettings(creditCard.getAlternateClient()), privateMapper);
            return new DefaultCreditCardService(privateClient, enableFeatures.toArray(new Feature[0]));
        } else {
            return new DefaultCreditCardService(client, enableFeatures.toArray(new Feature[0]));
        }
    }

    /**
     * sbpayment-payeasy
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnClass(PayEasyService.class)
    @ConditionalOnBean({SpsClient.class, SpsReceiver.class})
    @ConditionalOnProperty(prefix = "vg.sbpayment.payeasy", name = "disabled", havingValue = "false",
            matchIfMissing = true)
    public PayEasyService payEasyService(SbpaymentProperties properties, SpsClient client, SpsReceiver receiver) {
        PayEasy payEasy = properties.getPayeasy();

        if (payEasy.isAlternateClientEnabled()) {
            SpsMapper privateMapper = createSpsMapper(payEasy.getAlternateClient());
            SpsClientSettings privateSettings = mapSettings(payEasy.getAlternateClient());
            SpsClient privateClient = new DefaultSpsClient(privateSettings, privateMapper);
            SpsReceiver privateReceiver = new DefaultSpsReceiver(privateSettings.getMerchantId(),
                    privateSettings.getServiceId(), privateMapper);

            return createPayEasyService(privateClient, privateReceiver, payEasy);
        } else {
            return createPayEasyService(client, receiver, payEasy);
        }
    }

    private PayEasyService createPayEasyService(SpsClient client, SpsReceiver receiver, PayEasy payEasy) {
        if (PayEasy.Type.ONLINE.equals(payEasy.getType())) {
            return new DefaultPayEasyService(client, receiver, payEasy.getBillInfo(), payEasy.getBillInfoKana());
        } else {
            return new DefaultPayEasyService(client, receiver, payEasy.getPayCsv());
        }
    }
}
