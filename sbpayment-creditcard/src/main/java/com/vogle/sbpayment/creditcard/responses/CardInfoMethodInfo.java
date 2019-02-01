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
public class CardInfoMethodInfo {

    @CipherString
    @JacksonXmlProperty(localName = "cardbrand_code")
    private String cardbrandCode;

    @JacksonXmlProperty(localName = "res_payinfo_key")
    private String payinfoKey;

    /**
     * Gets a {@link CreditCardBrand}
     */
    public CreditCardBrand getCreditCardBrand() {
        return CreditCardBrand.brand(cardbrandCode);
    }
}
