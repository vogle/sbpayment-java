package com.vogle.sbpayment.creditcard.responses;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import com.vogle.sbpayment.client.convert.CipherString;

/**
 * Card info update response
 *
 * @author Allan Im
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JacksonXmlRootElement(localName = "sps-api-response")
public class LegacyCardInfoUpdateResponse extends CardInfoElements {

    @CipherString
    @JacksonXmlProperty(localName = "res_pay_method_info")
    private LegacyCardInfoMethodInfo payMethodInfo;
}
