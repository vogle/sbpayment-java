package com.vogle.sbpayment.client.request;

import com.vogle.sbpayment.client.SpsRequest;
import com.vogle.sbpayment.client.response.CardTranLookupResponse;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Credit card transaction lookup request<br/>
 * 決済結果参照要求
 *
 * @author Allan Im
 **/
@Data
@JacksonXmlRootElement(localName = "sps-api-request")
public class CardTranLookupRequest implements SpsRequest<CardTranLookupResponse> {

    @JacksonXmlProperty(isAttribute = true)
    private final String id = "MG01-00101-101";

    @NotEmpty
    @Pattern(regexp = "[0-9]{5}")
    @JacksonXmlProperty(localName = "merchant_id")
    private String merchantId;

    @NotEmpty
    @Pattern(regexp = "[0-9]{3}")
    @JacksonXmlProperty(localName = "service_id")
    private String serviceId;

    @Size(max = 32)
    @JacksonXmlProperty(localName = "sps_transaction_id")
    private String spsTransactionId;

    @NotEmpty
    @Size(max = 14)
    @JacksonXmlProperty(localName = "tracking_id")
    private String trackingId;

    @Pattern(regexp = "[012]")
    @JacksonXmlProperty(localName = "response_info_type")
    private String responseInfoType;

    @JacksonXmlProperty(localName = "pay_option_manage")
    private CardTranLookupOptions payOptions;

    @Pattern(regexp = "[01]")
    @JacksonXmlProperty(localName = "encrypted_flg")
    private String encryptedFlg;

    @NotEmpty
    @Pattern(regexp = "(\\d{4}(0[0-9]|1[0-2])([0-2][0-9]|3[0-1])([0-1][0-9]|2[0-4])(([0-5][0-9]){2}))")
    @JacksonXmlProperty(localName = "request_date")
    private String requestDate;

    @Max(9999)
    @JacksonXmlProperty(localName = "limit_second")
    private Integer limitSecond;

    @NotEmpty
    @Size(max = 40)
    @JacksonXmlProperty(localName = "sps_hashcode")
    private String spsHashcode;

    @Override
    public Class<CardTranLookupResponse> responseClass() {
        return CardTranLookupResponse.class;
    }
}