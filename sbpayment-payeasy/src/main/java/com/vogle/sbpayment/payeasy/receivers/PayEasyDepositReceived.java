package com.vogle.sbpayment.payeasy.receivers;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

import com.vogle.sbpayment.client.convert.CipherString;
import com.vogle.sbpayment.client.receivers.SpsReceivedData;

/**
 * PayEasy deposit receivers
 *
 * @author Allan Im
 **/
@Data
@JacksonXmlRootElement(localName = "sps-api-request")
public class PayEasyDepositReceived implements SpsReceivedData {

    @JacksonXmlProperty(isAttribute = true)
    private String id;

    /**
     * マーチャントID
     */
    @JacksonXmlProperty(localName = "merchant_id")
    private String merchantId;

    /**
     * サービスID
     */
    @JacksonXmlProperty(localName = "service_id")
    private String serviceId;

    /**
     * 処理対象SBPS トランザクションID
     */
    @JacksonXmlProperty(localName = "sps_transaction_id")
    private String spsTransactionId;

    /**
     * 処理対象トラッキングID
     */
    @JacksonXmlProperty(localName = "tracking_id")
    private String trackingId;

    /**
     * 処理日時
     */
    @JacksonXmlProperty(localName = "rec_datetime")
    private String recDatetime;

    /**
     * 入金通知情報
     */
    @CipherString
    @JacksonXmlProperty(localName = "pay_method_info")
    private PayEasyDepositInfo depositInfo;

    /**
     * リクエスト日時
     */
    @JacksonXmlProperty(localName = "request_date")
    private String requestDate;

    /**
     * チェックサム
     */
    @JacksonXmlProperty(localName = "sps_hashcode")
    private String spsHashcode;

}
