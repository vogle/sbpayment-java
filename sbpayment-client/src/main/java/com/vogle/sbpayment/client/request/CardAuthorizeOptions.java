package com.vogle.sbpayment.client.request;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
 * Card authorize options
 *
 * @author Allan Im
 **/
@Data
public class CardAuthorizeOptions {

    @NotEmpty
    @JacksonXmlProperty(localName = "token")
    private String token;

    @NotEmpty
    @JacksonXmlProperty(localName = "token_key")
    private String tokenKey;

    @NotEmpty
    @Pattern(regexp = "[01]")
    @JacksonXmlProperty(localName = "cust_manage_flg")
    private String custManageFlg;

    @Pattern(regexp = "[01]")
    @JacksonXmlProperty(localName = "cardbrand_return_flg")
    private String cardbrandReturnFlg;
}