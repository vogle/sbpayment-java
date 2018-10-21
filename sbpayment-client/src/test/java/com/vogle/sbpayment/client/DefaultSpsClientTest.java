package com.vogle.sbpayment.client;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link DefaultSpsClient}
 *
 * @author Allan Im
 */
public class DefaultSpsClientTest {

    @Test
    public void client() {
        SpsSettings settings = new SpsSettings();
        SpsClient client = new DefaultSpsClient(settings);

        assertThat(client).isNotNull();
        assertThat(client.getSettings()).isNotNull();
    }

}