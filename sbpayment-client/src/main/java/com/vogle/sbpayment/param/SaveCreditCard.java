package com.vogle.sbpayment.param;

import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.CreditCardNumber;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Credit card information
 *
 * @author Allan Im
 **/
@Data
@Builder
public class SaveCreditCard {

    @NotEmpty
    @CreditCardNumber
    private String number;

    @NotEmpty
    @Pattern(regexp = "\\d{4}(0[0-9]|1[0-2])")
    private String expiration;

    @Size(min = 3, max = 4)
    @Pattern(regexp = "[0-9]{3,4}")
    private String securityCode;

    @Size(max = 20)
    private String resrv1;

    @Size(max = 20)
    private String resrv2;

    @Size(max = 20)
    private String resrv3;
}
