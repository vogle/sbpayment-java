package com.vogle.sbpayment.creditcard.requests;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

import com.vogle.sbpayment.client.convert.CipherString;
import com.vogle.sbpayment.client.convert.MultiByteString;
import com.vogle.sbpayment.client.requests.SpsRequest;
import com.vogle.sbpayment.creditcard.responses.CardInfoSaveResponse;

/**
 * Card information save request <br/>
 * クレジットカード情報登録要求
 *
 * @author Allan Im
 **/
@Data
@JacksonXmlRootElement(localName = "sps-api-request")
public class CardInfoSaveRequest implements SpsRequest<CardInfoSaveResponse> {

    @JacksonXmlProperty(isAttribute = true)
    private final String id = "MG02-00131-101";

    @NotEmpty
    @Pattern(regexp = "[0-9]{5}")
    @JacksonXmlProperty(localName = "merchant_id")
    private String merchantId;

    @NotEmpty
    @Pattern(regexp = "[0-9]{3}")
    @JacksonXmlProperty(localName = "service_id")
    private String serviceId;

    @NotEmpty
    @Size(max = 64)
    @JacksonXmlProperty(localName = "cust_code")
    private String custCode;

    @Pattern(regexp = "[01]")
    @JacksonXmlProperty(localName = "sps_cust_info_return_flg")
    private String spsCustInfoReturnFlg;

    @NotNull
    @CipherString
    @MultiByteString
    @JacksonXmlProperty(localName = "pay_method_info")
    private CardInfoMethod payMethod;

    @JacksonXmlProperty(localName = "pay_option_manage")
    private CardInfoOptions payOptions;

    @Pattern(regexp = "[01]")
    @JacksonXmlProperty(localName = "encrypted_flg")
    private String encryptedFlg;

    @NotEmpty
    @JacksonXmlProperty(localName = "request_date")
    private String requestDate;

    @Max(9_999)
    @JacksonXmlProperty(localName = "limit_second")
    private Integer limitSecond;

    @NotEmpty
    @Size(max = 40)
    @JacksonXmlProperty(localName = "sps_hashcode")
    private String spsHashcode;

    @Override
    public Class<CardInfoSaveResponse> responseClass() {
        return CardInfoSaveResponse.class;
    }
}
