package com.vogle.sbpayment.param;

import com.vogle.sbpayment.client.DealingsType;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Credit card information
 *
 * @author Allan Im
 **/
@Data
@Builder
public class PayCreditCard {

    @NotEmpty
    @Pattern(regexp = "[0-9]{12,19}")
    private String number;

    @NotEmpty
    @Pattern(regexp = "\\d{4}(0[0-9]|1[0-2])")
    private String expiration;

    @Size(min = 3, max = 4)
    @Pattern(regexp = "[0-9]{3,4}")
    private String securityCode;

    private DealingsType dealingsType;

    @Size(min = 3, max = 3)
    @Pattern(regexp = "[0-9]{3}")
    private String divideTimes;

    @NotNull
    private boolean toSaveCreditCard;

    public String getToSaveCreditCard() {
        return toSaveCreditCard ? "1" : "0";
    }
}
