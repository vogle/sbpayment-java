package com.vogle.sbpayment.creditcard.requests;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import com.vogle.sbpayment.client.convert.CipherString;
import com.vogle.sbpayment.client.convert.MultiByteString;
import com.vogle.sbpayment.creditcard.DealingsType;
import com.vogle.sbpayment.creditcard.params.ByCreditCard;

/**
 * Credit card information with payment options
 *
 * @author Allan Im
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class LegacyCardAuthorizeMethod extends CreditCardElements {

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

    public void setCreditCard(ByCreditCard creditCard) {
        this.setCcNumber(creditCard.getNumber());
        this.setCcExpiration(creditCard.getExpiration());
        this.setSecurityCode(creditCard.getSecurityCode());
        if (creditCard.getDealingsType() != null) {
            this.setDealingsType(creditCard.getDealingsType().code());
            if (DealingsType.INSTALLMENT.equals(creditCard.getDealingsType())) {
                this.setDivideTimes(creditCard.getDivideTimes());
            }
        }
    }

}
