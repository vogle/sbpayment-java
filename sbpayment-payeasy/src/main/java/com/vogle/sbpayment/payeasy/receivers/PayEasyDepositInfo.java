package com.vogle.sbpayment.payeasy.receivers;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import com.vogle.sbpayment.client.convert.CipherString;

/**
 * Deposit information<br/>
 * 入金通知情報
 *
 * @author Allan Im
 **/
@Data
public class PayEasyDepositInfo {

    /**
     * 入金種別
     * <pre>
     * 1：速報
     * 2：速報取消
     * 3：確報
     * 4：確報取消
     * </pre>
     */
    @CipherString
    @JacksonXmlProperty(localName = "rec_type")
    private String type;

    /**
     * 入金された金額
     */
    @CipherString
    @JacksonXmlProperty(localName = "rec_amount")
    private String amount;

    /**
     * 入金された累計金額
     */
    @CipherString
    @JacksonXmlProperty(localName = "rec_amountTotal")
    private String amountTotal;

    /**
     * 購入した際に設定したメールアドレス
     */
    @CipherString
    @JacksonXmlProperty(localName = "rec_mail")
    private String mail;

    /**
     * 備考
     */
    @CipherString
    @JacksonXmlProperty(localName = "rec_extra")
    private String extra;
}
