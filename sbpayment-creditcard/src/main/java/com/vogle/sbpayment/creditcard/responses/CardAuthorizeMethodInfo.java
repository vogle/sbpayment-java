package com.vogle.sbpayment.creditcard.responses;

import com.vogle.sbpayment.client.convert.CipherString;
import com.vogle.sbpayment.creditcard.CreditCardBrand;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

/**
 * Authorized Credit card information
 *
 * @author Allan Im
 **/
@Data
public class CardAuthorizeMethodInfo {

    @CipherString
    @JacksonXmlProperty(localName = "cc_company_code")
    private String ccCompanyCode;

    @CipherString
    @JacksonXmlProperty(localName = "cardbrand_code")
    private String cardbrandCode;

    @CipherString
    @JacksonXmlProperty(localName = "recognized_no")
    private String recognizedNo;

    public CreditCardBrand mapCreditCardBrand() {
        return CreditCardBrand.brand(cardbrandCode);
    }
}
