package com.vogle.sbpayment.creditcard.requests;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
 * Card Information options
 *
 * @author Allan Im
 **/
@Data
public class CardInfoOptions {

    @NotEmpty
    @JacksonXmlProperty(localName = "token")
    private String token;

    @NotEmpty
    @JacksonXmlProperty(localName = "token_key")
    private String tokenKey;

    @Pattern(regexp = "[01]")
    @JacksonXmlProperty(localName = "cardbrand_return_flg")
    private String cardbrandReturnFlg;
}
