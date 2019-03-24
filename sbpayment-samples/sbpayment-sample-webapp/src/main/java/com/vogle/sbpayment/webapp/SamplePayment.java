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
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Make Singleton Sbpayment
 *
 * @author Allan Im
 */
public class SamplePayment {

    static final String TOKEN_URL = "https://stbtoken.sps-system.com/sbpstoken/com_sbps_system_token.js";
    static final String SAMPLE_CUSTOMER_CODE = "SAMPLE_01";
    static final String SESSION_TRACKING_ID = "TRACKING-ID";
    static final String SESSION_RESULT = "RESULT";

    private static Sbpayment sbpayment1;
    private static Sbpayment sbpayment2;

    private SamplePayment() {
    }

    private static Sbpayment getSbpayment1() {
        if (sbpayment1 == null) {
            Properties p = getProperties("../../config/it1.properties");
            sbpayment1 = Sbpayment.newInstance(p);
        }
        return sbpayment1;
    }

    private static Sbpayment getSbpayment2() {
        if (sbpayment2 == null) {
            Properties p = getProperties("../../config/it2.properties");
            sbpayment2 = Sbpayment.newInstance(p);
        }
        return sbpayment2;
    }

    private static Properties getProperties(String filePath) {
        Properties p = new Properties();
        try {
            p.load(new FileInputStream(System.getProperty("user.dir") + File.separator
                + filePath));
        } catch (IOException ignored) {
            // ignored
        }
        return p;
    }

    public static SpsConfig.SpsInfo getSpsInfo1() {
        return getSbpayment1().getSpsInfo();
    }

    public static CreditCardPayment getCreditCardPayment() {
        CardPayFeature[] feature = {
            CardPayFeature.RETURN_CARD_BRAND,
            CardPayFeature.RETURN_CUSTOMER_INFO
        };
        return CreditCardPayment.newInstance(getSbpayment1(), feature);
    }

    public static PayEasyPayment getPayEasyPayment() {
        OnlineType onlineType = new OnlineType();
        onlineType.setBillInfo("東京");
        onlineType.setBillInfoKana("トウキョウ");
        return PayEasyPayment.newInstance(getSbpayment2(), onlineType);
    }
}
