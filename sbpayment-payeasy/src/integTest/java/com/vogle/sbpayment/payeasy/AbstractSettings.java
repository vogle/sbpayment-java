package com.vogle.sbpayment.payeasy;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Random;

import com.vogle.sbpayment.client.DefaultSpsClient;
import com.vogle.sbpayment.client.DefaultSpsMapper;
import com.vogle.sbpayment.client.DefaultSpsReceiver;
import com.vogle.sbpayment.client.SpsClient;
import com.vogle.sbpayment.client.SpsClientSettings;
import com.vogle.sbpayment.client.SpsMapper;
import com.vogle.sbpayment.client.SpsReceiver;

/**
 * Test settings
 *
 * @author Allan Im
 **/
abstract class AbstractSettings {
    private SpsClientSettings settings;
    private String hashKey;
    private String desKey;
    private String desInitKey;

    AbstractSettings() {
        Properties p = new Properties();
        try {
            p.load(this.getClass().getClassLoader().getResourceAsStream("it.properties"));
        } catch (IOException ignored) {
        }

        settings = SpsClientSettings.builder()
                .apiUrl(p.getProperty("it2.apiUrl"))
                .merchantId(p.getProperty("it2.merchantId"))
                .serviceId(p.getProperty("it2.serviceId"))
                .basicAuthId(p.getProperty("it2.basicAuthId"))
                .basicAuthPassword(p.getProperty("it2.basicAuthPassword"))
                .build();

        hashKey = p.getProperty("it2.hashKey");
        desKey = p.getProperty("it2.desKey");
        desInitKey = p.getProperty("it2.desInitKey");
    }

    SpsClientSettings getSettings() {
        return settings;
    }

    SpsMapper mapper() {
        return new DefaultSpsMapper(hashKey, desKey, desInitKey);
    }

    SpsClient client() {
        return new DefaultSpsClient(settings, mapper());
    }

    SpsReceiver receiver() {
        return new DefaultSpsReceiver(settings.getMerchantId(), settings.getServiceId(), mapper());
    }

    String orderNo() {
        Random random = new Random();
        return "VO" + dayPattern() + random.nextInt(99999);
    }

    private String dayPattern() {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMddHHmmss");
        long time = System.currentTimeMillis();
        return fmt.format(new Date(time));
    }
}
