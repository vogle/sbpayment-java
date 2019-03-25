/*
 * Copyright 2019 Vogle Labs.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.vogle.sbpayment.webapp;

import com.vogle.sbpayment.client.Sbpayment;
import com.vogle.sbpayment.client.SpsConfig;
import com.vogle.sbpayment.creditcard.CardPayFeature;
import com.vogle.sbpayment.creditcard.CreditCardPayment;
import com.vogle.sbpayment.payeasy.OnlineType;
import com.vogle.sbpayment.payeasy.PayEasyPayment;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Make Singleton Sbpayment
 *
 * @author Allan Im
 */
public class SamplePaymentHelper {

    static final String TOKEN_URL = "https://stbtoken.sps-system.com/sbpstoken/com_sbps_system_token.js";
    static final String SAMPLE_CUSTOMER_CODE = "SAMPLE_01";
    static final String SESSION_TRACKING_ID = "TRACKING-ID";
    static final String SESSION_RESULT = "RESULT";

    private static Sbpayment sbpayment1;
    private static Sbpayment sbpayment2;

    static {
        Properties p1 = getProperties("../../config/it1.properties");
        sbpayment1 = Sbpayment.newInstance(p1);
        Properties p2 = getProperties("../../config/it2.properties");
        sbpayment2 = Sbpayment.newInstance(p2);
    }

    private static Properties getProperties(String filePath) {
        Properties p = new Properties();
        String path = System.getProperty("user.dir") + File.separator + filePath;
        try (InputStream inputStream = Files.newInputStream(Paths.get(path))) {
            p.load(inputStream);
        } catch (IOException ignored) {
            // ignored
        }
        return p;
    }

    /**
     * Gets SPS information
     */
    public static SpsConfig.SpsInfo getSpsInfo1() {
        return sbpayment1.getSpsInfo();
    }

    /**
     * Gets {@link CreditCardPayment}
     */
    public static CreditCardPayment getCreditCardPayment() {
        CardPayFeature[] feature = {
            CardPayFeature.RETURN_CARD_BRAND,
            CardPayFeature.RETURN_CUSTOMER_INFO
        };
        return CreditCardPayment.newInstance(sbpayment1, feature);
    }

    /**
     * Gets {@link PayEasyPayment}
     */
    public static PayEasyPayment getPayEasyPayment() {
        OnlineType onlineType = new OnlineType();
        onlineType.setBillInfo("東京");
        onlineType.setBillInfoKana("トウキョウ");
        return PayEasyPayment.newInstance(sbpayment2, onlineType);
    }
}
