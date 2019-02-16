package com.vogle.sbpayment.creditcard;

/**
 * Credit-Card payment Features
 */
public enum CardPayFeature {
    /**
     * When sending customer information, return it from Softbank payment.<br/>
     * 顧客コードを送るとき、ソフトバングペイメントから顧客情報を返却する。
     */
    RETURN_CUSTOMER_INFO,

    /**
     * When sending credit-card information, return credit-card brand.<br/>
     * カード情報を送るとき、カードブランド情報を返却する。
     */
    RETURN_CARD_BRAND
}
