package com.vogle.sbpayment.creditcard.requests;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

/**
 * Card Capture Options
 *
 * @author Allan Im
 **/
@Data
public class CardCaptureOptions {

    @Max(9_999_999)
    @Min(1)
    @JacksonXmlProperty(localName = "amount")
    private Integer amount;
}
