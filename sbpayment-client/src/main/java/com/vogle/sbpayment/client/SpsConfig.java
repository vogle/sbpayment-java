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
@ToString
@Builder
public class SpsConfig {

    /**
     * Softbank payment data charset, Default is "Shift_JIS".<br/>
     * 基本"Shift_JIS"で使用
     */
    @Builder.Default
    private Charset charset = Charset.forName("Shift_JIS");

    /**
     * Softbank Payment Server TimeZone, Default is "JST".<br/>
     * "JST"を使用
     */
    @Builder.Default
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
    @Builder.Default
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
    public static SpsConfig from(final Properties properties) {

        SpsConfig.SpsConfigBuilder builder = SpsConfig.builder();
        properties.forEach((keyObject, valueObject) -> {
            if (keyObject.toString().startsWith("sbpayment.")) {
                String key = keyObject.toString().substring("sbpayment.".length());
                String value = (String) valueObject;

                if ("charset".equalsIgnoreCase(key)) {
                    builder.charset(Charset.forName(value));
                } else if ("timeZone".equalsIgnoreCase(key)) {
                    builder.timeZone(TimeZone.getTimeZone(value));
                } else if ("allowableSecondOnRequest".equalsIgnoreCase(key)) {
                    builder.allowableSecondOnRequest(Integer.parseInt(value));
                } else if ("cipherEnabled".equalsIgnoreCase(key)) {
                    builder.cipherEnabled(Boolean.valueOf(value));
                } else {
                    try {
                        Method method = SpsConfigBuilder.class.getMethod(key, String.class);
                        method.invoke(builder, (String) value);
                    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ignored) {
                    }
                }
            }
        });

        return builder.build();
    }

    /**
     * Gets getClient configuration
     */
    public SpsInfo getSpsInfo() {
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
    public ClientInfo getClientInfo() {
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
    public CipherInfo getCipherInfo() {
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
    public static class SpsInfo {
        @NotEmpty
        private String merchantId;

        @NotEmpty
        private String serviceId;
    }

    @Builder
    @Getter
    public static class ClientInfo {

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
    public static class CipherInfo {

        @NotNull
        private Charset charset;

        @NotEmpty
        private String hashKey;

        private boolean cipherEnabled;

        private String desKey;

        private String desInitKey;

    }
}
