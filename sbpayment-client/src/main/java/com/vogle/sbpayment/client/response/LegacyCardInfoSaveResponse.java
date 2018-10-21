package com.vogle.sbpayment.client.response;

import com.vogle.sbpayment.client.SpsResponse;
import com.vogle.sbpayment.client.convert.CipherString;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Card info save response
 *
 * @author Allan Im
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JacksonXmlRootElement(localName = "sps-api-response")
public class LegacyCardInfoSaveResponse extends CardInfoElements implements SpsResponse {

    @CipherString
    @JacksonXmlProperty(localName = "res_pay_method_info")
    private LegacyCardInfoMethodInfo payMethodInfo;

}
