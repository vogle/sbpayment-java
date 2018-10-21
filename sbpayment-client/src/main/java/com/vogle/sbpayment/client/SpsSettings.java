package com.vogle.sbpayment.client;

import lombok.Data;

/**
 * Softbank payment settings. <br/>
 * ソフトバングペイメントの設定値
 *
 * @author Allan Im
 **/
@Data
public class SpsSettings {

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

    /**
     * When sending customer information, return it from Softbank payment.<br/>
     * 顧客コードを送るとき、ソフトバングペイメントから顧客情報を返却する。
     */
    private boolean returnCustomerInfo = false;

    /**
     * When sending credit-card information, return credit-card brand.<br/>
     * カード情報を送るとき、カードブランド情報を返却する。
     */
    private boolean returnCardBrand = false;

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

    private PayEasy payEasy = new PayEasy();

    // TODO
    @Data
    public static class PayEasy {

        private PayEasyType type = PayEasyType.ONLINE;

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
         * 本日は「0」を基準として、日数を加算。
         */
        private int billLimitDay = 3;
    }

    // TODO
    public enum PayEasyType {
        ONLINE("O"), LINK("L");

        private String code;

        PayEasyType(String code) {
            this.code = code;
        }

        public String code() {
            return code;
        }
    }
}
