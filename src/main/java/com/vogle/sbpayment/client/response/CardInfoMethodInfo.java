package com.vogle.sbpayment.client.response;

import com.vogle.sbpayment.client.CreditCardBrand;
import com.vogle.sbpayment.client.convert.CipherString;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

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

    public CreditCardBrand mapCreditCardBrand() {
        return CreditCardBrand.brand(cardbrandCode);
    }
}
