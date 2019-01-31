package com.vogle.sbpayment.creditcard.requests;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

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

    public CardInfoOptions() {
    }

    public CardInfoOptions(String token, String tokenKey, String cardbrandReturnFlg) {
        this.token = token;
        this.tokenKey = tokenKey;
        this.cardbrandReturnFlg = cardbrandReturnFlg;
    }
}
