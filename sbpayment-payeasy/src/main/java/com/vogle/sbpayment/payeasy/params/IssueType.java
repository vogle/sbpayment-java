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

/**
 * Issues Type<br/>
 * 請求書発行区分
 *
 * @author Allan Im
 **/
public enum IssueType {

    /**
     * 発行済み
     */
    ISSUED("0"),

    /**
     * 要発行
     */
    REQUIRED_ISSUANCE("1"),

    /**
     * 発行不要
     */
    NO_ISSUE_REQUIRED("2");

    private String code;

    IssueType(String code) {
        this.code = code;
    }

    public String code() {
        return code;
    }
}
