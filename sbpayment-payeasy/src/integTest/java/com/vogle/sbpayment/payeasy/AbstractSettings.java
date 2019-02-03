package com.vogle.sbpayment.payeasy;

import com.vogle.sbpayment.client.DefaultSpsManager;
import com.vogle.sbpayment.client.SpsConfig;
import com.vogle.sbpayment.client.SpsManager;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Random;

/**
 * Test config
 *
 * @author Allan Im
 **/
abstract class AbstractSettings {
    private SpsConfig config;
    private String hashKey;
    private String desKey;
    private String desInitKey;

    AbstractSettings() {
        Properties p = new Properties();
        try {
            p.load(this.getClass().getClassLoader().getResourceAsStream("it.properties"));
        } catch (IOException ignored) {
        }

        config = SpsConfig.builder()
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

    SpsConfig getConfig() {
        return config;
    }

    SpsManager manager() {
        return new DefaultSpsManager(config);
    }

    String orderNo() {
        Random random = new Random();
        return "VO" + dayPattern() + random.nextInt(99_999);
    }

    private String dayPattern() {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMddHHmmss");
        long time = System.currentTimeMillis();
        return fmt.format(new Date(time));
    }
}
