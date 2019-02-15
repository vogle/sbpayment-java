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

/**
 * Online or Link<br/>
 * Online：オンライン（ATM）方式<br/>
 * Link：情報リンク方式（インターネットでの支払い）
 */
public enum PayEasyType {
    ONLINE("O"), LINK("L");

    private String code;

    PayEasyType(String code) {
        this.code = code;
    }

    public String code() {
        return code;
    }
}