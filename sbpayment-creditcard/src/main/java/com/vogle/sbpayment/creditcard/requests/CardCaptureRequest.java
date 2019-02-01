package com.vogle.sbpayment.creditcard.requests;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

import com.vogle.sbpayment.client.requests.SpsRequest;
import com.vogle.sbpayment.creditcard.responses.DefaultResponse;

/**
 * Credit card capture request<br/>
 * 売上要求
 *
 * @author Allan Im
 **/
@Data
@JacksonXmlRootElement(localName = "sps-api-request")
public class CardCaptureRequest implements SpsRequest<DefaultResponse> {

    @JacksonXmlProperty(isAttribute = true)
    private final String id = "ST02-00201-101";

    @NotEmpty
    @Pattern(regexp = "[0-9]{5}")
    @JacksonXmlProperty(localName = "merchant_id")
    private String merchantId;

    @NotEmpty
    @Pattern(regexp = "[0-9]{3}")
    @JacksonXmlProperty(localName = "service_id")
    private String serviceId;

    @Size(max = 32)
    @JacksonXmlProperty(localName = "sps_transaction_id")
    private String spsTransactionId;

    @NotEmpty
    @Size(max = 14)
    @JacksonXmlProperty(localName = "tracking_id")
    private String trackingId;

    @NotEmpty
    @Pattern(regexp = "(\\d{4}(0[0-9]|1[0-2])([0-2][0-9]|3[0-1])([0-1][0-9]|2[0-4])(([0-5][0-9]){2}))")
    @JacksonXmlProperty(localName = "processing_datetime")
    private String processingDatetime;

    @JacksonXmlProperty(localName = "pay_option_manage")
    private CardCaptureOptions payOptions;

    @NotEmpty
    @JacksonXmlProperty(localName = "request_date")
    private String requestDate;

    @Max(9_999)
    @JacksonXmlProperty(localName = "limit_second")
    private Integer limitSecond;

    @NotEmpty
    @Size(max = 40)
    @JacksonXmlProperty(localName = "sps_hashcode")
    private String spsHashcode;

    @Override
    public Class<DefaultResponse> responseClass() {
        return DefaultResponse.class;
    }
}
