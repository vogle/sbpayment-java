package com.vogle.sbpayment.creditcard.params;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Data;

import com.vogle.sbpayment.creditcard.DealingsType;

/**
 * Token information
 *
 * @author Allan Im
 **/
@Data
@Builder
public class ByToken {

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
     * 取引区分
     */
    private DealingsType dealingsType;

    /**
     * 分割回数、取引区分が「{@link DealingsType#INSTALLMENT} 分割」の場合は必須です。
     */
    @Min(3)
    @Max(999)
    private Integer divideTimes;

    /**
     * クレジットカード情報の登録/更新を行わ
     */
    @NotNull
    private boolean savingCreditCard;

    public DealingsType getDealingsType() {
        return ParamUtils.convertDealingsType(this.dealingsType, this.divideTimes);
    }

    public String getDivideTimes() {
        return ParamUtils.convertDivideTimes(this.dealingsType, this.divideTimes);
    }

    public String getSavingCreditCard() {
        return ParamUtils.convertBool(savingCreditCard);
    }
}
