package com.vogle.sbpayment.creditcard.requests;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import javax.validation.constraints.Pattern;

/**
 * Card transaction lookup requests options
 *
 * @author Allan Im
 **/
@Data
public class CardTranLookupOptions {

    @Pattern(regexp = "[01]")
    @JacksonXmlProperty(localName = "cardbrand_return_flg")
    private String cardbrandReturnFlg;
}
