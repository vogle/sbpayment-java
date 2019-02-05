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

package com.vogle.sbpayment.creditcard.params;

import com.vogle.sbpayment.creditcard.DealingsType;

/**
 * Param Utils
 *
 * @author Allan Im
 */
class ParamUtils {

    /**
     * enabled: 1, not enabled: 0
     */
    static String convertBool(boolean enabled) {
        return enabled ? "1" : "0";
    }

    /**
     * Change to LUMP_SUM if the dealingsType is INSTALLMENT and divideTimes is null or less then two
     *
     * @param dealingsType The DealingsType
     * @param divideTimes  Divide Times is grater then one.
     * @return The DealingsType
     */
    static DealingsType convertDealingsType(DealingsType dealingsType, Integer divideTimes) {
        if (DealingsType.INSTALLMENT.equals(dealingsType) && (divideTimes == null || divideTimes < 2)) {
            return DealingsType.LUMP_SUM;
        } else {
            return dealingsType;
        }
    }

    /**
     * Change to divideTimes if the dealingsType is INSTALLMENT and divideTimes is null
     *
     * @param dealingsType The DealingsType
     * @param divideTimes  Divide Times is grater then one.
     * @return divideTimes To String
     */
    static String convertDivideTimes(DealingsType dealingsType, Integer divideTimes) {
        if (DealingsType.INSTALLMENT.equals(convertDealingsType(dealingsType, divideTimes))) {
            StringBuilder value = new StringBuilder(String.valueOf(divideTimes));
            while (value.length() < 3) {
                value.insert(0, "0");
            }
            return value.toString();
        }
        return null;
    }
}
