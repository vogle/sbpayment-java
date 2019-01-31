package com.vogle.sbpayment.client.requests;

import javax.validation.constraints.Max;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import com.vogle.sbpayment.client.convert.MultiByteString;

/**
 * Order details
 *
 * @author Allan Im
 **/
@Data
public class PayDetail {

    @Max(999)
    @JacksonXmlProperty(localName = "dtl_rowno")
    private Integer dtlRowno;

    @Size(max = 20)
    @JacksonXmlProperty(localName = "dtl_item_id")
    private String dtlItemId;

    @Size(max = 40)
    @MultiByteString
    @JacksonXmlProperty(localName = "dtl_item_name")
    private String dtlItemName;

    @Max(999999)
    @JacksonXmlProperty(localName = "dtl_item_count")
    private Integer dtlItemCount;

    @Max(9999999)
    @JacksonXmlProperty(localName = "dtl_tax")
    private Integer dtlTax;

    @Max(9999999)
    @JacksonXmlProperty(localName = "dtl_amount")
    private Integer dtlAmount;

}
