package com.vogle.sbpayment.client;

import com.vogle.sbpayment.client.request.CardAuthorizeRequest;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

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
        SpsClientSettings settings = new SpsClientSettings();
        SpsClient client = new DefaultSpsClient(settings);

        assertThat(client).isNotNull();
    }

    @Test
    public void request() throws IllegalAccessException, InvocationTargetException, InstantiationException {
        for (Constructor<?> declaredConstructor : CardAuthorizeRequest.class.getDeclaredConstructors()) {
            declaredConstructor.setAccessible(true);
            declaredConstructor.newInstance();
        }
    }

}