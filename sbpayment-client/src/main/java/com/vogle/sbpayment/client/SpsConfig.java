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

package com.vogle.sbpayment.client;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.Properties;
import java.util.TimeZone;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * Softbank payment configuration. <br/>
 * ソフトバングペイメントの設定値
 *
 * @author Allan Im
 **/
@Setter
@ToString
class SpsConfig {

    /**
     * Softbank payment data charset, Default is "Shift_JIS".<br/>
     * 基本"Shift_JIS"で使用
     */
    private Charset charset = Charset.forName("Shift_JIS");

    /**
     * Softbank Payment Server TimeZone, Default is "JST".<br/>
     * "JST"を使用
     */
    private TimeZone timeZone = TimeZone.getTimeZone("JST");

    /**
     * API Service URL. <br/>
     * API サビースの接続先
     */
    private String apiUrl;

    /**
     * MerchantId from Softbank Payment.
     */
    private String merchantId;

    /**
     * ServiceId from Softbank Payment.
     */
    private String serviceId;

    /**
     * Basic authentication ID. <br/>
     * ベージック認証ID
     */
    private String basicAuthId;

    /**
     * Basic authentication password. <br/>
     * ベージック認証パースワード
     */
    private String basicAuthPassword;

    /**
     * Allowable time on request.(Second)<br/>
     * リクエスト時の許容時間(秒)
     */
    private int allowableSecondOnRequest = 600;

    /**
     * Hash key, ハッシュキー
     */
    private String hashKey;

    /**
     * If set enable Cipher, Must set 'desKey' & 'desInitKey'<br/>
     * 3DES 暗号化使用可否
     */
    private boolean cipherEnabled;

    /**
     * 3DES cipher key.<br/>
     * 3DES 暗号化キー
     */
    private String desKey;

    /**
     * 3DES initialization key.<br/>
     * 3DES 初期化キー
     */
    private String desInitKey;

    /**
     * Create a SpsConfig from Properties
     *
     * @param properties The Properties information
     * @return new SpsConfig
     */
    static SpsConfig from(final Properties properties) {

        SpsConfig config = new SpsConfig();
        properties.forEach((key, value) -> {
            if (key.toString().startsWith("sbpayment.")) {
                String name = key.toString().substring("sbpayment.".length());

                if ("charset".equalsIgnoreCase(name)) {
                    config.setCharset(Charset.forName((String) value));
                } else if ("timeZone".equalsIgnoreCase(name)) {
                    config.setTimeZone(TimeZone.getTimeZone((String) value));
                } else if ("allowableSecondOnRequest".equalsIgnoreCase(name)) {
                    config.setAllowableSecondOnRequest((Integer) value);
                } else if ("cipherEnabled".equalsIgnoreCase(name)) {
                    config.setCipherEnabled(Boolean.valueOf((String) value));
                } else {
                    try {
                        Method method = SpsConfig.class.getMethod(getSetterMethod(name), String.class);
                        method.invoke(config, (String) value);
                    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ignored) {
                    }
                }
            }
        });

        return config;
    }

    private static String getSetterMethod(String name) {
        return "set" + name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    /**
     * Gets getClient configuration
     */
    SpsInfo getSpsInfo() {
        SpsInfo spsInfo = SpsInfo.builder()
                .merchantId(this.merchantId)
                .serviceId(this.serviceId)
                .build();

        ValidationHelper.beanValidate(spsInfo);

        return spsInfo;
    }

    /**
     * Gets getClient configuration
     */
    ClientInfo getClientInfo() {
        ClientInfo clientInfo = ClientInfo.builder()
                .timeZone(this.timeZone)
                .apiUrl(this.apiUrl)
                .merchantId(this.merchantId)
                .serviceId(this.serviceId)
                .basicAuthId(this.basicAuthId)
                .basicAuthPassword(this.basicAuthPassword)
                .allowableSecondOnRequest(this.allowableSecondOnRequest)
                .build();

        ValidationHelper.beanValidate(clientInfo);

        return clientInfo;
    }

    /**
     * Gets cipher configuration
     */
    CipherInfo getCipherInfo() {
        CipherInfo cipherInfo = CipherInfo.builder()
                .charset(this.charset).hashKey(this.hashKey)
                .desKey(this.desKey).desInitKey(this.desInitKey)
                .cipherEnabled(this.cipherEnabled)
                .build();

        ValidationHelper.beanValidate(cipherInfo);
        if (this.cipherEnabled) {
            ValidationHelper.assertsNotEmpty("desKey", this.desKey);
            ValidationHelper.assertsNotEmpty("desInitKey", this.desInitKey);
        }

        return cipherInfo;
    }

    @Builder
    @Getter
    static class SpsInfo {
        @NotEmpty
        private String merchantId;

        @NotEmpty
        private String serviceId;
    }

    @Builder
    @Getter
    static class ClientInfo {

        @NotNull
        private TimeZone timeZone;

        @NotEmpty
        private String apiUrl;

        @NotEmpty
        private String merchantId;

        @NotEmpty
        private String serviceId;

        private String basicAuthId;

        private String basicAuthPassword;

        private int allowableSecondOnRequest;

    }

    @Builder
    @Getter
    static class CipherInfo {

        @NotNull
        private Charset charset;

        @NotEmpty
        private String hashKey;

        private boolean cipherEnabled;

        private String desKey;

        private String desInitKey;

    }
}
