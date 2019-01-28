package com.vogle.sbpayment.client.receivers;

import com.vogle.sbpayment.client.convert.MultiByteString;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Result for receiver
 *
 * @author Allan Im
 **/
@Getter
@ToString
@JacksonXmlRootElement(localName = "sps-api-response")
public class ReceptionResult {

    public static final String SUCCESS = "OK";
    public static final String FAIL = "NG";

    @JacksonXmlProperty(isAttribute = true)
    private String id;

    @JacksonXmlProperty(localName = "res_result")
    private String result;

    @Setter
    @MultiByteString
    @JacksonXmlProperty(localName = "res_err_msg")
    private String errMsg;

    public ReceptionResult(String id) {
        this.id = id;
        this.result = SUCCESS;
    }

    public ReceptionResult(String id, String errorMessage) {
        this.id = id;
        this.result = FAIL;
        this.errMsg = errorMessage;
    }

}
