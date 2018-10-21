package com.vogle.sbpayment.client.request;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * Card Payment Amount Options
 *
 * @author Allan Im
 **/
@Data
public class CardAmountOptions {

    @Max(9999999)
    @Min(1)
    @JacksonXmlProperty(localName = "amount")
    private Integer amount;
}
