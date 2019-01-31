package com.vogle.sbpayment.creditcard.params;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Builder;
import lombok.Data;

import com.vogle.sbpayment.creditcard.DealingsType;

/**
 * Credit card information
 *
 * @author Allan Im
 **/
@Data
@Builder
public class ByCreditCard {

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
