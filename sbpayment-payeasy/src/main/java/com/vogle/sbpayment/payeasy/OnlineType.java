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

package com.vogle.sbpayment.payeasy;

import lombok.Data;

/**
 * Online：オンライン（ATM）方式
 *
 * @author Allan Im
 */
@Data
public class OnlineType {

    /**
     * 請求内容漢字、ATM 等に表示されます。（全角）
     */
    private String billInfo;

    /**
     * 請求内容カナ、ATM 等に表示されます。（全角英数カナ）
     */
    private String billInfoKana;

    /**
     * 支払期限、受注日時からデフォルトの支払期限の設定値内での指定が可能です。
     * ウェルネットを利用されている加盟店の場合、支払期限は当日指定が可能です。
     * 本日は「0」を基準として、日数を加算。デフォルトは「３日」
     */
    private int billLimitDay = 3;

}
