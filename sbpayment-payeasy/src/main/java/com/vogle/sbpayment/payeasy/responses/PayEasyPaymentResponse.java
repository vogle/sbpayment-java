package com.vogle.sbpayment.payeasy.responses;

import com.vogle.sbpayment.client.convert.CipherString;
import com.vogle.sbpayment.client.responses.CommonElements;
import com.vogle.sbpayment.client.responses.SpsInfo;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * PayEasy payment response
 *
 * @author Allan Im
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JacksonXmlRootElement(localName = "sps-api-response")
public class PayEasyPaymentResponse extends CommonElements {

    @JacksonXmlProperty(localName = "res_tracking_id")
    private String trackingId;

    @CipherString
    @JacksonXmlProperty(localName = "res_pay_method_info")
    private PayEasyInfo payEasyInfo;

    @JacksonXmlProperty(localName = "res_sps_info")
    private SpsInfo spsInfo;

    @Override
    public String getDescription() {
        return FeatureIds.getDescription(this.getId());
    }
}
