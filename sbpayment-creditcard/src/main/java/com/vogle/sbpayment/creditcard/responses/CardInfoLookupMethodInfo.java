package com.vogle.sbpayment.creditcard.responses;

import com.vogle.sbpayment.client.convert.CipherString;
import com.vogle.sbpayment.client.convert.MultiByteString;
import com.vogle.sbpayment.creditcard.CreditCardBrand;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

/**
 * Card lookup method information
 *
 * @author Allan Im
 **/
@Data
public class CardInfoLookupMethodInfo {

    @CipherString
    @JacksonXmlProperty(localName = "cc_number")
    private String ccNumber;

    @CipherString
    @JacksonXmlProperty(localName = "cc_expiration")
    private String ccExpiration;

    @CipherString
    @JacksonXmlProperty(localName = "cardbrand_code")
    private String cardbrandCode;

    @CipherString
    @MultiByteString
    @JacksonXmlProperty(localName = "resrv1")
    private String resrv1;

    @CipherString
    @MultiByteString
    @JacksonXmlProperty(localName = "resrv2")
    private String resrv2;

    @CipherString
    @MultiByteString
    @JacksonXmlProperty(localName = "resrv3")
    private String resrv3;

    public CreditCardBrand mapCreditCardBrand() {
        return CreditCardBrand.brand(cardbrandCode);
    }
}
