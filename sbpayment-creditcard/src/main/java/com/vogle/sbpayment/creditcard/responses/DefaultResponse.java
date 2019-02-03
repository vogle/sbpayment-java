package com.vogle.sbpayment.creditcard.responses;

import com.vogle.sbpayment.client.responses.CommonElements;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Default transaction response
 *
 * @author Allan Im
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JacksonXmlRootElement(localName = "sps-api-response")
public class DefaultResponse extends CommonElements {

    @Override
    public String getDescription() {
        return FeatureHelper.getDescription(this.getId());
    }
}
