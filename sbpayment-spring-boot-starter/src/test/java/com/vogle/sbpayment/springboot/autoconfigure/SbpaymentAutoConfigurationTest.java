package com.vogle.sbpayment.springboot.autoconfigure;

import com.vogle.sbpayment.client.SpsClient;
import com.vogle.sbpayment.client.SpsMapper;
import com.vogle.sbpayment.client.SpsReceiver;
import com.vogle.sbpayment.creditcard.CreditCardService;
import com.vogle.sbpayment.payeasy.PayEasyService;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link SbpaymentAutoConfiguration}
 *
 * @author Allan Im
 **/
public class SbpaymentAutoConfigurationTest {

    private static Set<String> required = new HashSet<>();

    static {
        required.add("vg.sbpayment.client.api-url:http://test.vogle.com");
        required.add("vg.sbpayment.client.hash-key:HASH_KEY");
        required.add("vg.sbpayment.client.merchant-id:MERCHANT_ID");
        required.add("vg.sbpayment.client.service-id:SERVICE_ID");
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private AnnotationConfigApplicationContext context;

    @Before
    public void init() {
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
        assertThat(this.context.getBeanNamesForType(SpsClient.class)).hasSize(0);
        assertThat(this.context.getBeanNamesForType(SpsReceiver.class)).hasSize(0);
        assertThat(this.context.getBeanNamesForType(SpsMapper.class)).hasSize(0);
    }

    @Test
    public void checkPropertiesEnabled() {
        TestPropertyValues.of("vg.sbpayment.client.api-url:http://test.vogle.com").applyTo(this.context);
        this.context.register(SbpaymentAutoConfiguration.class);
        this.context.refresh();

        // check bean
        assertThat(this.context.getBeanNamesForType(SbpaymentProperties.class)).hasSize(1);

        assertThat(this.context.getBeanNamesForType(SpsMapper.class)).hasSize(0);
        assertThat(this.context.getBeanNamesForType(SpsClient.class)).hasSize(0);
        assertThat(this.context.getBeanNamesForType(SpsReceiver.class)).hasSize(0);
    }


    @Test
    public void checkSpsMapperEnabled() {
        TestPropertyValues.of(
                "vg.sbpayment.client.api-url:http://test.vogle.com",
                "vg.sbpayment.client.hash-key:HASH_KEY"
        ).applyTo(this.context);
        this.context.register(SbpaymentAutoConfiguration.class);
        this.context.refresh();

        // check bean
        assertThat(this.context.getBeanNamesForType(SbpaymentProperties.class)).hasSize(1);
        assertThat(this.context.getBeanNamesForType(SpsMapper.class)).hasSize(1);

        assertThat(this.context.getBeanNamesForType(SpsClient.class)).hasSize(0);
        assertThat(this.context.getBeanNamesForType(SpsReceiver.class)).hasSize(0);
    }

    @Test
    public void checkClientAndReceiverEnabled() {
        TestPropertyValues.of(required).applyTo(this.context);
        this.context.register(SbpaymentAutoConfiguration.class);
        this.context.refresh();

        // check bean
        assertThat(this.context.getBeanNamesForType(SbpaymentProperties.class)).hasSize(1);
        assertThat(this.context.getBeanNamesForType(SpsMapper.class)).hasSize(1);
        assertThat(this.context.getBeanNamesForType(SpsClient.class)).hasSize(1);
        assertThat(this.context.getBeanNamesForType(SpsReceiver.class)).hasSize(1);
    }


    @Test
    public void checkAllServiceEnabled() {
        TestPropertyValues.of(required).applyTo(this.context);
        this.context.register(SbpaymentAutoConfiguration.class);
        this.context.refresh();

        // check bean
        assertThat(this.context.getBeanNamesForType(CreditCardService.class)).hasSize(1);
        assertThat(this.context.getBeanNamesForType(PayEasyService.class)).hasSize(1);
    }

    @Test
    public void checkCreditCardDisabled() {
        required.add("vg.sbpayment.creditcard.disabled:true");
        TestPropertyValues.of(required).applyTo(this.context);
        this.context.register(SbpaymentAutoConfiguration.class);
        this.context.refresh();

        // check bean
        assertThat(this.context.getBeanNamesForType(CreditCardService.class)).hasSize(0);
    }

    @Test
    public void checkPayEasyDisabled() {
        required.add("vg.sbpayment.payeasy.disabled:true");
        TestPropertyValues.of(required).applyTo(this.context);
        this.context.register(SbpaymentAutoConfiguration.class);
        this.context.refresh();

        // check bean
        assertThat(this.context.getBeanNamesForType(PayEasyService.class)).hasSize(0);
    }

}
