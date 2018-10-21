package com.vogle.sbpayment.client.response;

import com.vogle.sbpayment.client.SpsResponse;
import com.vogle.sbpayment.client.convert.CipherString;

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
public class CardTranLookupResponse extends CommonElements implements SpsResponse {

    @JacksonXmlProperty(localName = "res_status")
    private String resStatus;

    @CipherString
    @JacksonXmlProperty(localName = "res_pay_method_info")
    private CardTranLookupMethodInfo payMethodInfo;

    public TransactionStatus mapTransactionStatus() {
        return TransactionStatus.status(resStatus);
    }
}
