package com.vogle.sbpayment.creditcard.responses;

import com.vogle.sbpayment.client.convert.CipherString;
import com.vogle.sbpayment.client.responses.CommonElements;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Card Transaction lookup response
 *
 * @author Allan Im
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JacksonXmlRootElement(localName = "sps-api-response")
public class CardTranLookupResponse extends CommonElements {

    @JacksonXmlProperty(localName = "res_status")
    private String resStatus;

    @CipherString
    @JacksonXmlProperty(localName = "res_pay_method_info")
    private CardTranLookupMethodInfo payMethodInfo;

    public TransactionStatus mapTransactionStatus() {
        return TransactionStatus.status(resStatus);
    }

    @Override
    public String getDescription() {
        return FeatureIds.getDescription(this.getId());
    }
}
