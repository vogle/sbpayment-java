package com.vogle.sbpayment.client.response;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

/**
 * Common Response Elements
 *
 * @author Allan Im
 **/
@Data
public class CommonElements {

    @JacksonXmlProperty(isAttribute = true)
    private String id;

    @JacksonXmlProperty(localName = "res_result")
    private String result;

    @JacksonXmlProperty(localName = "res_sps_transaction_id")
    private String spsTransactionId;

    @JacksonXmlProperty(localName = "res_process_date")
    private String processDate;

    @JacksonXmlProperty(localName = "res_err_code")
    private String errCode;

    @JacksonXmlProperty(localName = "res_date")
    private String date;

    public boolean isSuccess() {
        return "OK".equalsIgnoreCase(result);
    }
}
