package com.vogle.sbpayment.creditcard.requests;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import com.vogle.sbpayment.client.convert.CipherString;
import com.vogle.sbpayment.client.convert.MultiByteString;

/**
 * Credit card options
 *
 * @author Allan Im
 **/
@Data
public class CardAuthorizeMethod {

    @Pattern(regexp = "10|21|61|80")
    @CipherString
    @JacksonXmlProperty(localName = "dealings_type")
    private String dealingsType;

    @Pattern(regexp = "^\\d{1,3}")
    @CipherString
    @JacksonXmlProperty(localName = "divide_times")
    private String divideTimes;

    @Size(max = 20)
    @CipherString
    @MultiByteString
    @JacksonXmlProperty(localName = "resrv1")
    private String resrv1;

    @Size(max = 20)
    @CipherString
    @MultiByteString
    @JacksonXmlProperty(localName = "resrv2")
    private String resrv2;

    @Size(max = 20)
    @CipherString
    @MultiByteString
    @JacksonXmlProperty(localName = "resrv3")
    private String resrv3;

}
