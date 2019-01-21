package com.vogle.sbpayment.creditcard.requests;

import com.vogle.sbpayment.client.SpsRequest;
import com.vogle.sbpayment.client.convert.CipherString;
import com.vogle.sbpayment.client.convert.MultiByteString;
import com.vogle.sbpayment.creditcard.responses.LegacyCardInfoSaveResponse;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Card information save request <br/>
 * Don't use in production environment, indeed it has been removed from sbpayment API.<br/>
 * クレジットカード情報登録要求：本サビスは本番環境では使わないでください、実際sbpaymentのAPIから削除されました。
 *
 * @author Allan Im
 **/
@Data
@JacksonXmlRootElement(localName = "sps-api-request")
public class LegacyCardInfoSaveRequest implements SpsRequest<LegacyCardInfoSaveResponse> {

    @JacksonXmlProperty(isAttribute = true)
    private final String id = "MG02-00101-101";

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
    private LegacyCardInfoMethod payMethod;

    @JacksonXmlProperty(localName = "pay_option_manage")
    private LegacyCardInfoOptions payOptions;

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
    public Class<LegacyCardInfoSaveResponse> responseClass() {
        return LegacyCardInfoSaveResponse.class;
    }
}
