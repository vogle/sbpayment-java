package com.vogle.sbpayment.creditcard.requests;

import javax.validation.constraints.Size;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import com.vogle.sbpayment.client.convert.CipherString;
import com.vogle.sbpayment.client.convert.MultiByteString;

/**
 * Credit card information
 *
 * @author Allan Im
 **/
@Data
@AllArgsConstructor
public class CardInfoMethod {

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
