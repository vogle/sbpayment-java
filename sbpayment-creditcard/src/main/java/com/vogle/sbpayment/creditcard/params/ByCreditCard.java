/*
 * Copyright 2019 VOGLE Labs.
 *
 * This file is part of sbpayment-java - Sbpayment client.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.vogle.sbpayment.creditcard.params;

import com.vogle.sbpayment.creditcard.DealingsType;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

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
