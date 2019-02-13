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

package com.vogle.sbpayment.client.params;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

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
