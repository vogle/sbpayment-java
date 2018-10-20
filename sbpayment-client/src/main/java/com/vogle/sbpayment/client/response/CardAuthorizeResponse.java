package com.vogle.sbpayment.client.response;

import com.vogle.sbpayment.client.SpsResponse;
import com.vogle.sbpayment.client.convert.CipherString;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Card Authorize Response
 *
 * @author Allan Im
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JacksonXmlRootElement(localName = "sps-api-response")
public class CardAuthorizeResponse extends CommonElements implements SpsResponse {

    @JacksonXmlProperty(localName = "res_tracking_id")
    private String trackingId;

    @CipherString
    @JacksonXmlProperty(localName = "res_pay_method_info")
    private AuthorizeCreditCardInfo authorizeCreditCardInfo;

    @JacksonXmlProperty(localName = "res_sps_info")
    private SpsInfo spsInfo;

}
