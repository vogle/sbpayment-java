package com.vogle.sbpayment.creditcard.requests;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * Card Capture Options
 *
 * @author Allan Im
 **/
@Data
public class CardCaptureOptions {

    @Max(9999999)
    @Min(1)
    @JacksonXmlProperty(localName = "amount")
    private Integer amount;
}
