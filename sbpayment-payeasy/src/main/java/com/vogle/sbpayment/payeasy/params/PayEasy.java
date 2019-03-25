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

package com.vogle.sbpayment.payeasy.params;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
 * PayEasy payments information
 *
 * @author Allan Im
 **/
@Getter
@ToString
@EqualsAndHashCode
@Builder(builderClassName = "Builder")
public class PayEasy {

    /**
     * 請求書発行区分
     */
    @Default
    private IssueType issueType = IssueType.ISSUED;

    /**
     * 顧客姓（全角）
     */
    @NotEmpty
    @Pattern(regexp = "[^ -~\uff61-\uff9f]{1,10}", message = "Please enter up to 10 full-width characters")
    private String lastName;

    /**
     * 顧客名（全角）
     */
    @NotEmpty
    @Pattern(regexp = "[^ -~\uff61-\uff9f]{1,10}", message = "Please enter up to 10 full-width characters")
    private String firstName;

    /**
     * 顧客姓カナ（全角カナのみ）
     */
    @NotEmpty
    @Pattern(regexp = "[^[\u30a1-\u30fc]+$]{1,10}", message = "Please enter up to 10 characters in full-width kana")
    private String lastNameKana;

    /**
     * 顧客姓カナ（全角カナのみ）
     */
    @NotEmpty
    @Pattern(regexp = "[^[\u30a1-\u30fc]+$]{1,10}", message = "Please enter up to 10 characters in full-width kana")
    private String firstNameKana;

    /**
     * 郵便番号１（郵便番号の上3 桁：000～999　数字のみ）
     */
    @Pattern(regexp = "[0-9]{3}")
    private String firstZip;

    /**
     * 郵便番号２（郵便番号の下4 桁：0000～9999　数字のみ）
     */
    @Pattern(regexp = "[0-9]{4}")
    private String secondZip;

    /**
     * 都道府県（全角）
     */
    @Pattern(regexp = "[^ -~\uff61-\uff9f]{1,25}", message = "Please enter up to 25 full-width japanese characters")
    private String add1;

    /**
     * 市区町村、番地（全角）
     */
    @Pattern(regexp = "[^ -~\uff61-\uff9f]{1,25}", message = "Please enter up to 25 full-width japanese characters")
    private String add2;

    /**
     * マンション・ビル（全角）
     */
    @Pattern(regexp = "[^ -~\uff61-\uff9f]{1,50}", message = "Please enter up to 50 full-width japanese characters")
    private String add3;

    /**
     * 電話番号：9 桁～11 桁（ハイフンなし、数値のみ）
     */
    @NotEmpty
    @Pattern(regexp = "[0-9]{9,11}")
    private String tel;

    /**
     * e-mail
     */
    @NotEmpty
    @Email
    private String mail;

    /**
     * 端末種別
     */
    @Default
    private TerminalValue terminalValue = TerminalValue.PC;
}
