package com.vogle.sbpayment.creditcard.responses;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import com.vogle.sbpayment.client.responses.CommonElements;
import com.vogle.sbpayment.client.responses.SpsInfo;

/**
 * Card info common elements
 *
 * @author Allan Im
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CardInfoElements extends CommonElements {

    @JacksonXmlProperty(localName = "res_sps_info")
    private SpsInfo spsInfo;

    @Override
    public String getDescription() {
        return FeatureHelper.getDescription(this.getId());
    }
}
