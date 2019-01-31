package com.vogle.sbpayment.payeasy.responses;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import com.vogle.sbpayment.client.convert.CipherString;

/**
 * PayEasy Information
 *
 * @author Allan Im
 **/
@Data
public class PayEasyInfo {

    @CipherString
    @JacksonXmlProperty(localName = "invoice_no")
    private String invoiceNo;

    @CipherString
    @JacksonXmlProperty(localName = "bill_date")
    private String billDate;

    @CipherString
    @JacksonXmlProperty(localName = "skno")
    private String skno;

    @CipherString
    @JacksonXmlProperty(localName = "cust_number")
    private String custNumber;

    @CipherString
    @JacksonXmlProperty(localName = "bank_form")
    private String bankForm;

    @CipherString
    @JacksonXmlProperty(localName = "bptn")
    private String bptn;

    @CipherString
    @JacksonXmlProperty(localName = "bill")
    private String bill;

}
