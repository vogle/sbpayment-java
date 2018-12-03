package com.vogle.sbpayment.param;

import com.vogle.sbpayment.client.DealingsType;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * SaveCardByToken information
 *
 * @author Allan Im
 **/
@Data
@Builder
public class PayToken {

    @NotEmpty
    private String token;

    @NotEmpty
    private String tokenKey;

    private DealingsType dealingsType;

    @Size(min = 3, max = 3)
    @Pattern(regexp = "[0-9]{3}")
    private String divideTimes;

    @NotNull
    private boolean saveCreditCard;

    public String getSaveCreditCard() {
        return saveCreditCard ? "1" : "0";
    }
}
