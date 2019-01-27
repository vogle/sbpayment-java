package com.vogle.sbpayment.creditcard;

import com.vogle.sbpayment.client.DefaultSpsClient;
import com.vogle.sbpayment.client.SpsClient;
import com.vogle.sbpayment.client.SbpaymentSettings;
import com.vogle.sbpayment.client.SbpaymentSettings.CipherSets;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Random;

/**
 * Test settings
 *
 * @author Allan Im
 **/
abstract class AbstractSettings {

    SbpaymentSettings settings() throws IOException {
        Properties p = new Properties();
        p.load(this.getClass().getClassLoader().getResourceAsStream("it.properties"));

        SbpaymentSettings settings = new SbpaymentSettings();
        settings.setApiUrl(p.getProperty("it1.apiUrl"));
        settings.setMerchantId(p.getProperty("it1.merchantId"));
        settings.setServiceId(p.getProperty("it1.serviceId"));
        settings.setBasicAuthId(p.getProperty("it1.basicAuthId"));
        settings.setBasicAuthPassword(p.getProperty("it1.basicAuthPassword"));
        settings.setHashKey(p.getProperty("it1.hashKey"));

        CipherSets cipherSets = new CipherSets();
        cipherSets.setEnabled(true);
        cipherSets.setDesKey(p.getProperty("it1.desKey"));
        cipherSets.setDesInitKey(p.getProperty("it1.desInitKey"));
        settings.setCipherSets(cipherSets);

        return settings;
    }

    SbpaymentSettings settingsAutoCapture() throws IOException {
        Properties p = new Properties();
        p.load(this.getClass().getClassLoader().getResourceAsStream("it.properties"));

        SbpaymentSettings settings = new SbpaymentSettings();
        settings.setApiUrl(p.getProperty("it2.apiUrl"));
        settings.setMerchantId(p.getProperty("it2.merchantId"));
        settings.setServiceId(p.getProperty("it2.serviceId"));
        settings.setBasicAuthId(p.getProperty("it2.basicAuthId"));
        settings.setBasicAuthPassword(p.getProperty("it2.basicAuthPassword"));
        settings.setHashKey(p.getProperty("it2.hashKey"));

        CipherSets cipherSets = new CipherSets();
        cipherSets.setEnabled(true);
        cipherSets.setDesKey(p.getProperty("it2.desKey"));
        cipherSets.setDesInitKey(p.getProperty("it2.desInitKey"));
        settings.setCipherSets(cipherSets);

        return settings;
    }


    SpsClient client(SbpaymentSettings settings) {
        return new DefaultSpsClient(settings);
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
