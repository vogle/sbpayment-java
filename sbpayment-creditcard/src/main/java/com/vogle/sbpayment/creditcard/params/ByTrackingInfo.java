package com.vogle.sbpayment.creditcard.params;

import com.vogle.sbpayment.creditcard.DealingsType;

import lombok.Builder;
import lombok.Data;

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
@Data
@Builder
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

    public String getSavingCreditCard() {
        return ParamUtils.convertBool(savingCreditCard);
    }
    
    public boolean hasToken() {
        return token != null && !token.isEmpty() && tokenKey != null && !tokenKey.isEmpty();
    }

    public boolean hasOptions() {
        return dealingsType != null;
    }
}
