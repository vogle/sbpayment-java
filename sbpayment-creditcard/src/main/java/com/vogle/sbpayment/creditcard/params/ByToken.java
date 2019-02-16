/*
 * Copyright 2019 Vogle Labs.
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
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * Token information
 *
 * @author Allan Im
 **/
@Getter
@ToString
@EqualsAndHashCode
@Builder(builderClassName = "Builder")
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
