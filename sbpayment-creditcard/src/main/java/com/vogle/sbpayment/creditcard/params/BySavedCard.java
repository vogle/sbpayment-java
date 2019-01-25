package com.vogle.sbpayment.creditcard.params;

import com.vogle.sbpayment.creditcard.DealingsType;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * If pay for saved card, need this information
 *
 * @author Allan Im
 **/
@Data
@Builder
public class BySavedCard {

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

    public DealingsType getDealingsType() {
        return ParamUtils.convertDealingsType(this.dealingsType, this.divideTimes);
    }

    public String getDivideTimes() {
        return ParamUtils.convertDivideTimes(this.dealingsType, this.divideTimes);
    }
}
