package com.vogle.sbpayment.creditcard;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Random;

import com.vogle.sbpayment.client.DefaultSpsManager;
import com.vogle.sbpayment.client.SpsConfig;
import com.vogle.sbpayment.client.SpsManager;

/**
 * Test config
 *
 * @author Allan Im
 **/
abstract class AbstractSettings {
    private SpsConfig config;

    AbstractSettings() {
        Properties p = new Properties();
        try {
            p.load(this.getClass().getClassLoader().getResourceAsStream("it.properties"));
        } catch (IOException ignored) {
        }

        config = SpsConfig.builder()
                .apiUrl(p.getProperty("it1.apiUrl"))
                .merchantId(p.getProperty("it1.merchantId"))
                .serviceId(p.getProperty("it1.serviceId"))
                .basicAuthId(p.getProperty("it1.basicAuthId"))
                .basicAuthPassword(p.getProperty("it1.basicAuthPassword"))
                .hashKey(p.getProperty("it1.hashKey"))
                .desKey(p.getProperty("it1.desKey"))
                .desInitKey(p.getProperty("it1.desInitKey"))
                .build();

    }

    SpsManager manager() {
        return new DefaultSpsManager(config);
    }

    String orderNo() {
        Random random = new Random();
        return "VO" + dayPattern() + random.nextInt(99999);
    }

    String customerCode() {
        Random random = new Random();
        return "VC" + dayPattern() + random.nextInt(99999);
    }

    private String dayPattern() {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMddHHmmss");
        long time = System.currentTimeMillis();
        return fmt.format(new Date(time));
    }
}
