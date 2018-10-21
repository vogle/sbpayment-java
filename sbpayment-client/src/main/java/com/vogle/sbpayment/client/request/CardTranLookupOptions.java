package com.vogle.sbpayment.client.request;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import javax.validation.constraints.Pattern;

/**
 * Card transaction lookup request options
 *
 * @author Allan Im
 **/
@Data
public class CardTranLookupOptions {

    @Pattern(regexp = "[01]")
    @JacksonXmlProperty(localName = "cardbrand_return_flg")
    private String cardbrandReturnFlg;
}
