/*
 * Copyright 2019 VOGLE Labs.
 *
 * This file is part of sbpayment-java - Sbpayment client.
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
import lombok.ToString;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
@Builder
@ToString
public class SpsConfig {

    /**
     * Softbank payment data charset, Default is "Shift_JIS".<br/>
     * 基本"Shift_JIS"で使用
     */
    @NotEmpty
    @Getter
    @Builder.Default
    private String charset = "Shift_JIS";

    /**
     * Softbank Payment Server TimeZone, Default is "JST".<br/>
     * "JST"を使用
     */
    @NotNull
    @Getter
    @Builder.Default
    private TimeZone timeZone = TimeZone.getTimeZone("JST");

    /**
     * API Service URL. <br/>
     * API サビースの接続先
     */
    @NotEmpty
    @Getter
    private String apiUrl;

    /**
     * MerchantId from Softbank Payment.
     */
    @NotEmpty
    @Getter
    private String merchantId;

    /**
     * ServiceId from Softbank Payment.
     */
    @NotEmpty
    @Getter
    private String serviceId;

    /**
     * Basic authentication ID. <br/>
     * ベージック認証ID
     */
    @Getter
    private String basicAuthId;

    /**
     * Basic authentication password. <br/>
     * ベージック認証パースワード
     */
    @Getter
    private String basicAuthPassword;

    /**
     * Allowable time on request.(Second)<br/>
     * リクエスト時の許容時間(秒)
     */
    @Getter
    @Builder.Default
    private int allowableSecondOnRequest = 600;

    /**
     * Hash key, ハッシュキー
     */
    @NotEmpty
    @Getter
    private String hashKey;

    /**
     * If set enable Cipher, Must set 'desKey' & 'desInitKey'<br/>
     * 3DES 暗号化使用可否
     */
    @Getter
    private boolean enabledCipher;

    /**
     * 3DES cipher key.<br/>
     * 3DES 暗号化キー
     */
    @Getter
    private String desKey;

    /**
     * 3DES initialization key.<br/>
     * 3DES 初期化キー
     */
    @Getter
    private String desInitKey;

    /**
     * Create a SpsConfig from Properties
     *
     * @param properties The Properties information
     * @return new SpsConfig
     */
    public static SpsConfig from(final Properties properties) {

        SpsConfigBuilder builder = builder();
        properties.forEach((key, value) -> {
            if (key.toString().startsWith("sbpayment.")) {
                String name = key.toString().substring("sbpayment.".length());

                if ("timeZone".equalsIgnoreCase(name)) {
                    builder.timeZone(TimeZone.getTimeZone((String) value));
                } else if ("allowableSecondOnRequest".equalsIgnoreCase(name)) {
                    builder.allowableSecondOnRequest((Integer) value);
                } else {
                    try {
                        Method method = SpsConfigBuilder.class.getMethod(name, String.class);
                        method.invoke(builder, (String) value);
                    } catch (NoSuchMethodException ex) {
                        throw new IllegalArgumentException("Don't have the config field: " + key, ex);
                    } catch (IllegalAccessException | InvocationTargetException ex) {
                        throw new IllegalArgumentException("Failed to invoke field: " + key, ex);
                    }
                }
            }
        });

        return builder.build();
    }

}
