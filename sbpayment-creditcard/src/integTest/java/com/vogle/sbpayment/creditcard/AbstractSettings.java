package com.vogle.sbpayment.creditcard;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Random;

import com.vogle.sbpayment.client.DefaultSpsClient;
import com.vogle.sbpayment.client.DefaultSpsMapper;
import com.vogle.sbpayment.client.SpsClient;
import com.vogle.sbpayment.client.SpsClientSettings;

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
                .apiUrl(p.getProperty("it1.apiUrl"))
                .merchantId(p.getProperty("it1.merchantId"))
                .serviceId(p.getProperty("it1.serviceId"))
                .basicAuthId(p.getProperty("it1.basicAuthId"))
                .basicAuthPassword(p.getProperty("it1.basicAuthPassword"))
                .build();

        hashKey = p.getProperty("it1.hashKey");
        desKey = p.getProperty("it1.desKey");
        desInitKey = p.getProperty("it1.desInitKey");
    }

    SpsClient client() {
        return new DefaultSpsClient(settings, new DefaultSpsMapper(hashKey, desKey, desInitKey));
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
