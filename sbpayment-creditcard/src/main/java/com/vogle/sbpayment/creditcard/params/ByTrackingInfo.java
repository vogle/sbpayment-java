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
import javax.validation.constraints.Size;

/**
 * If pay for tracking id, need this information
 *
 * @author Allan Im
 **/
@Getter
@ToString
@EqualsAndHashCode
@Builder(builderClassName = "Builder")
public class ByTrackingInfo {

    /**
     * 決済時に返却された「処理トラッキングID」<br/>
     * 同一マーチャントID とサービスID のトラッキングIDのみ指定可能です。
     */
    @NotEmpty
    @Size(max = 14)
    private String trackingId;

    /**
     * 決済前に生成された決済情報識別ワンタイムトークン<br/>
     * ワンタイムトークン利用時、必須です。
     */
    private String token;

    /**
     * 決済前に生成された決済情報複合化用ワンタイムトークンキー<br/>
     * ワンタイムトークン利用時、必須です。
     */
    private String tokenKey;

    /**
     * クレジットカード情報の登録/更新を行わ
     */
    @NotNull
    private boolean savingCreditCard;

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

    public DealingsType getDealingsType() {
        return ParamUtils.convertDealingsType(this.dealingsType, this.divideTimes);
    }

    public String getDivideTimes() {
        return ParamUtils.convertDivideTimes(this.dealingsType, this.divideTimes);
    }

    public boolean isSavingCreditCard() {
        return savingCreditCard;
    }

    public String getSavingCreditCard() {
        return ParamUtils.convertBool(savingCreditCard);
    }

    /**
     * Returns True if has token & token key
     */
    public boolean hasToken() {
        return token != null && !token.isEmpty() && tokenKey != null && !tokenKey.isEmpty();
    }

    /**
     * Returns True if has DealingsType
     */
    public boolean hasOptions() {
        return dealingsType != null;
    }
}
