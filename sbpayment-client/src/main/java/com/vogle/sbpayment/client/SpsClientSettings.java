package com.vogle.sbpayment.client;

import lombok.Data;

/**
 * Softbank payment settings. <br/>
 * ソフトバングペイメントの設定値
 *
 * @author Allan Im
 **/
@Data
public class SpsClientSettings {

    /**
     * Softbank payment data charset, Default is "Shift_JIS".<br/>
     * 基本"Shift_JIS"で使用
     */
    private String charset = "Shift_JIS";

    /**
     * Softbank Payment Server TimeZone, Default is "JST".<br/>
     */
    private String timeZone = "JST";

    /**
     * API Service URL. <br/>
     * AIP サビースの接続先
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

    private CipherSets cipherSets = new CipherSets();

    @Data
    public static class CipherSets {

        /**
         * Enable Cipher.<br/>
         * 3DES 暗号化使用可否
         */
        private boolean enabled = false;

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
    }
}
