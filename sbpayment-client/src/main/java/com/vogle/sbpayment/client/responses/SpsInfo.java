package com.vogle.sbpayment.client.responses;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

/**
 * Sps information elements
 *
 * @author Allan Im
 **/
@Data
public class SpsInfo {

    @JacksonXmlProperty(localName = "res_sps_cust_no")
    private String spsCustNo;

    @JacksonXmlProperty(localName = "res_sps_payment_no")
    private String spsPaymentNo;
}
