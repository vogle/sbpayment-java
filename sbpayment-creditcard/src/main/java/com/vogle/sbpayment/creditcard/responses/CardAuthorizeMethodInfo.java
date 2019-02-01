package com.vogle.sbpayment.creditcard.responses;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import com.vogle.sbpayment.client.convert.CipherString;
import com.vogle.sbpayment.creditcard.CreditCardBrand;

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

    /**
     * Gets a {@link CreditCardBrand}
     *
     * @return Card Brand
     */
    public CreditCardBrand getCreditCardBrand() {
        return CreditCardBrand.brand(cardbrandCode);
    }
}
