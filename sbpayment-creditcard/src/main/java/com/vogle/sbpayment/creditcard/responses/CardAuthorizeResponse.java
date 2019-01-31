package com.vogle.sbpayment.creditcard.responses;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import com.vogle.sbpayment.client.convert.CipherString;
import com.vogle.sbpayment.client.responses.CommonElements;
import com.vogle.sbpayment.client.responses.SpsInfo;

/**
 * Card Authorize Response
 *
 * @author Allan Im
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JacksonXmlRootElement(localName = "sps-api-response")
public class CardAuthorizeResponse extends CommonElements {

    @JacksonXmlProperty(localName = "res_tracking_id")
    private String trackingId;

    @CipherString
    @JacksonXmlProperty(localName = "res_pay_method_info")
    private CardAuthorizeMethodInfo payMethodInfo;

    @JacksonXmlProperty(localName = "res_sps_info")
    private SpsInfo spsInfo;

    @Override
    public String getDescription() {
        return FeatureIds.getDescription(this.getId());
    }

}
