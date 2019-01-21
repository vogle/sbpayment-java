package com.vogle.sbpayment.creditcard.requests;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
 * Credit card reauthorize  options
 *
 * @author Allan Im
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CardReauthorizeOptions extends CardAuthorizeOptions {

    @NotEmpty
    @Pattern(regexp = "[BMR]")
    @JacksonXmlProperty(localName = "pay_info_control_type")
    private String payInfoControlType;

    @NotEmpty
    @Pattern(regexp = "[BR]")
    @JacksonXmlProperty(localName = "pay_info_detail_control_type")
    private String payInfoDetailControlType;
}
