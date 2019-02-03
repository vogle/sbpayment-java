package com.vogle.sbpayment.creditcard.requests;

import com.vogle.sbpayment.client.convert.CipherString;
import com.vogle.sbpayment.client.convert.MultiByteString;
import com.vogle.sbpayment.creditcard.params.SaveCreditCard;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.Size;

/**
 * Credit card information
 *
 * @author Allan Im
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class LegacyCardInfoMethod extends CreditCardElements {

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

    /**
     * Sets data from {@link SaveCreditCard}
     *
     * @param creditCard SaveCreditCard Information
     */
    public void setSaveCreditCard(SaveCreditCard creditCard) {
        this.setCcNumber(creditCard.getNumber());
        this.setCcExpiration(creditCard.getExpiration());
        this.setSecurityCode(creditCard.getSecurityCode());
        this.setResrv1(creditCard.getResrv1());
        this.setResrv2(creditCard.getResrv2());
        this.setResrv3(creditCard.getResrv3());
    }
}
