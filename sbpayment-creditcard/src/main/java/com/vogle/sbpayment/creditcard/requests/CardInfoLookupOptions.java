package com.vogle.sbpayment.creditcard.requests;

import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

/**
 * Card information lookup requests options
 *
 * @author Allan Im
 **/
@Data
public class CardInfoLookupOptions {

    @Pattern(regexp = "[01]")
    @JacksonXmlProperty(localName = "cardbrand_return_flg")
    private String cardbrandReturnFlg;
}
