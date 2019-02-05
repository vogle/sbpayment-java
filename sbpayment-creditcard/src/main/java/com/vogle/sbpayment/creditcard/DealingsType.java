/*
 * Copyright 2019 VOGLE Labs.
 *
 * This file is part of sbpayment-java - Sbpayment client.
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

package com.vogle.sbpayment.creditcard;

/**
 * Credit Card payment Dealings type
 *
 * @author Allan Im
 **/
public enum DealingsType {
    /** lump-sum */
    LUMP_SUM("10"),

    /** monthly installment plan */
    INSTALLMENT("61"),

    /** lump-sum to bonus lump-sum */
    BONUS_LUMP_SUM("21"),

    /** card revolving service */
    REVOLVING("80");

    private String code;

    DealingsType(String code) {
        this.code = code;
    }

    /**
     * Gets a Dealings
     * <p>
     * Code: Dealings
     * "10": lump-sum
     * "61": monthly installment plan
     * "21": lump-sum to bonus lump-sum
     * "80": card revolving service
     * </p>
     *
     * @param code Dealing code
     * @return Dealing Type
     */
    public static DealingsType type(String code) {
        for (DealingsType type : DealingsType.values()) {
            if (code.equalsIgnoreCase(type.code())) {
                return type;
            }
        }
        return LUMP_SUM;
    }

    /**
     * Gets Brand Code
     *
     * @return "10", "61", "21" or "80"
     */
    public String code() {
        return code;
    }
}
