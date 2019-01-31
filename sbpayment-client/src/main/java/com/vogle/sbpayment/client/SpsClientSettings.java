package com.vogle.sbpayment.client;

import java.util.TimeZone;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Getter;

/**
 * Softbank payment settings. <br/>
 * ソフトバングペイメントの設定値
 *
 * @author Allan Im
 **/
@Getter
@Builder
public class SpsClientSettings {

    /**
     * Softbank Payment Server TimeZone, Default is "JST".<br/>
     * "JST"を使用
     */
    @NotNull
    @Builder.Default
    private TimeZone timeZone = TimeZone.getTimeZone("JST");

    /**
     * API Service URL. <br/>
     * API サビースの接続先
     */
    @NotEmpty
    private String apiUrl;

    /**
     * MerchantId from Softbank Payment.
     */
    @NotEmpty
    private String merchantId;

    /**
     * ServiceId from Softbank Payment.
     */
    @NotEmpty
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


}
