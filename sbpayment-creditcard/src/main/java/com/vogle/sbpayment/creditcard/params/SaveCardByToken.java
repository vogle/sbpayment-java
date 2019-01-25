package com.vogle.sbpayment.creditcard.params;

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

    /**
     * トークン
     */
    @NotEmpty
    private String token;

    /**
     * トークンキー
     */
    @NotEmpty
    private String tokenKey;

    /**
     * 備考欄１
     */
    @Size(max = 20)
    private String resrv1;

    /**
     * 備考欄２
     */
    @Size(max = 20)
    private String resrv2;

    /**
     * 備考欄３
     */
    @Size(max = 20)
    private String resrv3;
}
