package com.vogle.sbpayment.creditcard.responses;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import com.vogle.sbpayment.client.convert.CipherString;
import com.vogle.sbpayment.creditcard.CreditCardBrand;

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

    /**
     * Gets a {@link CreditCardBrand}
     */
    public CreditCardBrand getCreditCardBrand() {
        return CreditCardBrand.brand(cardbrandCode);
    }
}
