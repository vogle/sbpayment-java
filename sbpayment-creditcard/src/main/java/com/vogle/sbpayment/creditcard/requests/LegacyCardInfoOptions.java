package com.vogle.sbpayment.creditcard.requests;

import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

/**
 * Card info lookup requests options
 *
 * @author Allan Im
 **/
@Data
public class LegacyCardInfoOptions {

    @Pattern(regexp = "[01]")
    @JacksonXmlProperty(localName = "cardbrand_return_flg")
    private String cardbrandReturnFlg;
}
