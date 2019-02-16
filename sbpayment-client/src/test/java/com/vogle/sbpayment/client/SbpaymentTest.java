package com.vogle.sbpayment.client;

import org.junit.Test;

import java.io.IOException;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link Sbpayment} & {@link DefaultSbpayment}
 *
 * @author Allan Im
 */
public class SbpaymentTest {

    @Test
    public void newInstance() {
        // when
        Sbpayment sbpayment = Sbpayment.newInstance();

        // then
        assertThat(sbpayment).isNotNull();
        assertThat(sbpayment.getMapper()).isNotNull();
        assertThat(sbpayment.getClient()).isNotNull();
        assertThat(sbpayment.getReceiver()).isNotNull();

        assertThat(sbpayment.toString()).isNotEmpty();
    }

    @Test
    public void newInstanceWithFilePath() {
        // when
        Sbpayment sbpayment = Sbpayment.newInstance("test.properties");

        // then
        assertThat(sbpayment).isNotNull();
        assertThat(sbpayment.getMapper()).isNotNull();
        assertThat(sbpayment.getClient()).isNotNull();
        assertThat(sbpayment.getReceiver()).isNotNull();

        assertThat(sbpayment.toString()).isNotEmpty();
    }

    @Test(expected = IllegalStateException.class)
    public void newInstanceWithNoProperties() {
        // when
        Sbpayment.newInstance("no.properties");
    }

    @Test
    public void newInstanceWithProperties() {
        // Given
        Properties properties = new Properties();
        try {
            properties.load(this.getClass().getClassLoader().getResourceAsStream("test.properties"));
        } catch (IOException ignored) {
        }

        // when
        Sbpayment sbpayment = Sbpayment.newInstance(properties);

        // then
        assertThat(sbpayment).isNotNull();
        assertThat(sbpayment.getMapper()).isNotNull();
        assertThat(sbpayment.getClient()).isNotNull();
        assertThat(sbpayment.getReceiver()).isNotNull();

    }

    @Test
    public void newInstanceWithSpsConfig() {

        // Given
        SpsConfig config = SpsConfig.builder()
                .apiUrl("http://vogle.com")
                .merchantId("XX002").serviceId("BBB")
                .basicAuthId("BASIC").basicAuthPassword("PASSWORD")
                .hashKey("HASH_KEY")
                .cipherEnabled(true)
                .desKey("DES_KEY").desInitKey("DES_INIT_KEY")
                .build();

        // when
        Sbpayment sbpayment = Sbpayment.newInstance(config);

        // then
        assertThat(sbpayment).isNotNull();
        assertThat(sbpayment.getMapper()).isNotNull();
        assertThat(sbpayment.getClient()).isNotNull();
        assertThat(sbpayment.getReceiver()).isNotNull();

    }
}