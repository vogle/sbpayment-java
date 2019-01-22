package com.vogle.sbpayment.creditcard.params;

import com.vogle.sbpayment.creditcard.DealingsType;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Token information
 *
 * @author Allan Im
 **/
@Data
@Builder
public class ByToken {

    @NotEmpty
    private String token;

    @NotEmpty
    private String tokenKey;

    private DealingsType dealingsType;

    @Size(min = 3, max = 3)
    @Pattern(regexp = "[0-9]{3}")
    private String divideTimes;

    @NotNull
    private boolean savingCreditCard;

    public String getSavingCreditCard() {
        return savingCreditCard ? "1" : "0";
    }
}
