package com.vogle.sbpayment.creditcard.params;

import com.vogle.sbpayment.creditcard.DealingsType;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * If pay for saved card, need this information
 *
 * @author Allan Im
 **/
@Data
@Builder
public class PaySavedCard {

    private DealingsType dealingsType;

    @Size(min = 3, max = 3)
    @Pattern(regexp = "[0-9]{3}")
    private String divideTimes;

}
