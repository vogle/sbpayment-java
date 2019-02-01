package com.vogle.sbpayment.client.params;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import lombok.Builder;
import lombok.Data;

/**
 * Item details
 *
 * @author Allan Im
 **/
@Data
@Builder
public class Item {

    /**
     * 明細商品ID
     */
    @Size(max = 20)
    private String itemId;

    /**
     * 明細商品名称
     */
    @Size(max = 40)
    private String itemName;

    /**
     * 明細数量
     */
    @Max(999_999)
    @Min(1)
    private Integer itemCount;

    /**
     * 明細税額
     */
    @Max(9_999_999)
    @Min(0)
    private Integer itemTax;

    /**
     * 明細金額（税込）
     */
    @Max(9_999_999)
    @Min(0)
    private Integer itemAmount;

}
