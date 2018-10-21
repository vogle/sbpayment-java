package com.vogle.sbpayment.client.request;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import javax.validation.constraints.Pattern;

/**
 * Credit card brand options
 *
 * @author Allan Im
 **/
@Data
public class CardBrandOptions {

    @Pattern(regexp = "[01]")
    @JacksonXmlProperty(localName = "cardbrand_return_flg")
    private String cardbrandReturnFlg;
}
