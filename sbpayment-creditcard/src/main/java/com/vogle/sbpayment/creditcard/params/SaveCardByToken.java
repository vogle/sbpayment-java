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

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * Save Card By Token
 *
 * @author Allan Im
 **/
@Getter
@ToString
@EqualsAndHashCode
@Builder(builderClassName = "Builder")
public class SaveCardByToken {

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
}
