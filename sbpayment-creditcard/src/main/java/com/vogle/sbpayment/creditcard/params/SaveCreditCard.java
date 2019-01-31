package com.vogle.sbpayment.creditcard.params;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Builder;
import lombok.Data;

/**
 * Credit card information
 *
 * @author Allan Im
 **/
@Data
@Builder
public class SaveCreditCard {

    /**
     * クレジットカード番号
     */
    @NotEmpty
    @Pattern(regexp = "[0-9]{12,19}")
    private String number;

    /**
     * クレジットカード有効期限
     */
    @NotEmpty
    @Size(min = 6, max = 6)
    @Pattern(regexp = "([12][0-9]{3})(0[1-9]|1[0-2])")
    private String expiration;

    /**
     * セキュリティコード
     */
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
