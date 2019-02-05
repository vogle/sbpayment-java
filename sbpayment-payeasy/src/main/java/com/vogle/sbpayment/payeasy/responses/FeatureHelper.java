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

package com.vogle.sbpayment.payeasy.responses;

import java.util.HashMap;
import java.util.Map;

/**
 * PayEasy features lists
 *
 * @author Allan Im
 **/
class FeatureHelper {

    private static Map<String, String> descMap = new HashMap<>();

    static {
        descMap.put("ST01-00101-703", "PayEasy Payment");
        descMap.put("NT01-00103-703", "PayEasy Deposit Notice");
        descMap.put("NT01-00104-703", "PayEasy Expired Notice");
    }

    static String getDescription(String id) {
        return descMap.get(id);
    }
}
