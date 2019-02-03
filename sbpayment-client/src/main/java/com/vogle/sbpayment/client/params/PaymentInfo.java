package com.vogle.sbpayment.client.params;

import lombok.Builder;
import lombok.Data;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Payment information
 *
 * @author Allan Im
 **/
@Data
@Builder
public class PaymentInfo {

    /**
     * 加盟店にて管理されている顧客ID
     */
    @NotEmpty
    @Size(max = 64)
    private String customerCode;

    /**
     * 加盟店にて管理されている購入ID
     */
    @NotEmpty
    @Size(max = 38)
    @Pattern(regexp = "[0-9a-zA-Z_-]{1,38}")
    private String orderId;

    /**
     * 加盟店にて管理されている商品ID
     */
    @NotEmpty
    @Size(max = 32)
    @Pattern(regexp = "[0-9a-zA-Z_-]{1,32}")
    private String itemId;

    /**
     * 任意の商品名
     */
    @Size(max = 40)
    private String itemName;

    /**
     * 税額
     */
    @Max(9_999_999)
    @Min(0)
    private Integer tax;

    /**
     * 金額（税込）
     */
    @NotNull
    @Max(9_999_999)
    @Min(1)
    private Integer amount;

    /**
     * 自由欄１
     */
    @Size(max = 20)
    private String free1;

    /**
     * 自由欄２
     */
    @Size(max = 20)
    private String free2;

    /**
     * 自由欄３
     */
    @Size(max = 20)
    private String free3;

    /**
     * 請求番号枝番<br/>
     * 再入力時に1 ずつインクリメント
     */
    @Builder.Default
    @Max(99)
    private Integer orderRowNo = 1;

    /**
     * 明細商品
     */
    @Valid
    private List<Item> items;

}
