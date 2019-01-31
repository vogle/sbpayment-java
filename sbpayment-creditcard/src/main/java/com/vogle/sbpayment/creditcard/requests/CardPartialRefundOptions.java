package com.vogle.sbpayment.creditcard.requests;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

/**
 * Card Partial Refund Options
 *
 * @author Allan Im
 **/
@Data
public class CardPartialRefundOptions {

    @Max(9999999)
    @Min(1)
    @JacksonXmlProperty(localName = "amount")
    private Integer amount;
}
