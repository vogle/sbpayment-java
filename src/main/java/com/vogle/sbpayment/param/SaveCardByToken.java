package com.vogle.sbpayment.param;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * Save Card By Token
 *
 * @author Allan Im
 **/
@Data
@Builder
public class SaveCardByToken {

    @NotEmpty
    private String token;

    @NotEmpty
    private String tokenKey;

    @Size(max = 20)
    private String resrv1;

    @Size(max = 20)
    private String resrv2;

    @Size(max = 20)
    private String resrv3;
}
