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

package com.vogle.sbpayment.springboot.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import javax.validation.constraints.Size;

/**
 * Softbank payment system properties
 *
 * @author Allan Im
 **/
@Data
@ConfigurationProperties("sbpayment")
public class SbpaymentProperties {

    /**
     * Softbank payment system information
     */
    private final Client client = new Client();
    /**
     * Credit Card options
     */
    private final CreditCard creditcard = new CreditCard();
    /**
     * Pay-Easy Options
     */
    private final PayEasy payeasy = new PayEasy();

    /**
     * Softbank getClient configuration
     */
    @Data
    public static class Client {
        /**
         * Cipher settings
         */
        private final CipherSets cipherSets = new CipherSets();
        /**
         * Softbank payment data charset, Default is "Shift_JIS".<br/>
         * "Shift_JIS"を使用
         */
        private String charset = "Shift_JIS";
        /**
         * Softbank Payment Server TimeZone, Default is "JST".<br/>
         * "JST"を使用
         */
        private String timeZone = "JST";
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
         * Hash key, ハッシュキー
         */
        private String hashKey;
        /**
         * Allowable time on request.(Second)<br/>
         * リクエスト時の許容時間(秒)
         */
        private int allowableSecondOnRequest = 600;

        /**
         * Cipher configuration
         */
        @Data
        public static class CipherSets {

            /**
             * Enable Cipher.<br/>
             * 3DES 暗号化使用可否
             */
            private boolean enabled;

            /**
             * 3DES cipher key.<br/>
             * 3DES 暗号化キー
             */
            @Size(min = 24, max = 24)
            private String desKey;

            /**
             * 3DES initialization key.<br/>
             * 3DES 初期化キー
             */
            @Size(min = 8, max = 8)
            private String desInitKey;
        }
    }

    /**
     * CreditCard configuration
     */
    @Data
    public static class CreditCard {

        /**
         * Alternate getClient properties
         */
        @NestedConfigurationProperty
        private final Client alternateClient = new Client();
        private boolean disabled;
        /**
         * 顧客コードを送るとき、SBPS 顧客情報を返却する
         */
        private boolean customerInfoReturn;
        /**
         * カード情報を送るとき、カードブランド情報を返却する。
         */
        private boolean cardbrandReturn;
        /**
         * Alternate getClient enabled, テスト以外はFalseにしてください。<br/>
         * Trueにしたら、本alternateClientの情報を利用する。
         */
        private boolean alternateClientEnabled;

    }

    /**
     * PayEasy configuration
     */
    @Data
    public static class PayEasy {

        /**
         * Alternate getClient properties
         */
        @NestedConfigurationProperty
        private final Client alternateClient = new Client();
        private boolean disabled;
        private Type type = Type.ONLINE;
        /**
         * 金融機関コード、情報リンク方式の場合のみ必須です。ただし、電算システムを利用の場合は不要です。
         */
        private String payCsv;
        /**
         * 請求内容カナ、ATM 等に表示されます。（全角英数カナ）
         */
        private String billInfoKana;
        /**
         * 請求内容漢字、ATM 等に表示されます。（全角）
         */
        private String billInfo;
        /**
         * 支払期限、受注日時からデフォルトの支払期限の設定値内での指定が可能です。
         * ウェルネットを利用されている加盟店の場合、支払期限は当日指定が可能です。
         * 本日は「0」に基準として、日数を加算。
         */
        private int billLimitDay = 3;
        /**
         * Alternate getClient enabled, テスト以外はFalseにしてください。<br/>
         * Trueにしたら、本alternateClientの情報を利用する。
         */
        private boolean alternateClientEnabled;

        /**
         * PayEasy Type
         */
        enum Type {
            ONLINE, LINK
        }
    }
}
