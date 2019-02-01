package com.vogle.sbpayment.client.receivers;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.vogle.sbpayment.client.convert.MultiByteString;

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

    /**
     * Constructs a new successful ReceptionResult
     *
     * @param id a sbpayment features ID
     */
    public ReceptionResult(String id) {
        this.id = id;
        this.result = SUCCESS;
    }

    /**
     * Constructs a new failure ReceptionResult
     *
     * @param id           a sbpayment features ID
     * @param errorMessage sending message
     */
    public ReceptionResult(String id, String errorMessage) {
        this.id = id;
        this.result = FAIL;
        this.errMsg = errorMessage;
    }

}
