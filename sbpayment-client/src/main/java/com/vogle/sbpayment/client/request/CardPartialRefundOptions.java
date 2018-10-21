package com.vogle.sbpayment.client.request;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

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
