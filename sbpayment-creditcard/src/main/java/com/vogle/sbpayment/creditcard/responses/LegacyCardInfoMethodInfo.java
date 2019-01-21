package com.vogle.sbpayment.creditcard.responses;

import com.vogle.sbpayment.client.convert.CipherString;
import com.vogle.sbpayment.creditcard.CreditCardBrand;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

/**
 * CardInfo method info
 *
 * @author Allan Im
 **/
@Data
public class LegacyCardInfoMethodInfo {

    @CipherString
    @JacksonXmlProperty(localName = "cardbrand_code")
    private String cardbrandCode;

    public CreditCardBrand mapCreditCardBrand() {
        return CreditCardBrand.brand(cardbrandCode);
    }
}
