package com.vogle.sbpayment.param;

import com.vogle.sbpayment.client.DealingsType;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * If pay for tracking id, need this information
 *
 * @author Allan Im
 **/
@Data
@Builder
public class PayTrackingInfo {

    @NotEmpty
    @Size(max = 14)
    private String trackingId;

    private DealingsType dealingsType;

    @Size(min = 3, max = 3)
    @Pattern(regexp = "[0-9]{3}")
    private String divideTimes;

}
