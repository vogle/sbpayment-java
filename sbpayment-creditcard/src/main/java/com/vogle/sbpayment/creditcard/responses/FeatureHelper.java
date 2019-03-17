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

package com.vogle.sbpayment.creditcard.responses;

import java.util.HashMap;
import java.util.Map;

/**
 * Features lists
 *
 * @author Allan Im
 **/
class FeatureHelper {

    private static Map<String, String> descMap = new HashMap<>();

    static {

        // Card payment features
        descMap.put("ST01-00111-101", "Legacy PayCard Authorize");
        descMap.put("ST01-00131-101", "PayCard Authorize");
        descMap.put("ST02-00101-101", "PayCard Commit");
        descMap.put("ST02-00201-101", "PayCard Capture");
        descMap.put("ST02-00303-101", "PayCard Cancel/Refund");
        descMap.put("ST02-00307-101", "PayCard Partial Refund");
        descMap.put("ST01-00113-101", "Legacy PayCard Reauthorize");
        descMap.put("ST01-00133-101", "PayCard Reauthorize");
        descMap.put("MG01-00101-101", "PayCard Transaction Lookup");

        // Card information save features
        descMap.put("MG02-00101-101", "Legacy StoredCard Save");
        descMap.put("MG02-00131-101", "StoredCard Save");
        descMap.put("MG02-00102-101", "Legacy StoredCard Update");
        descMap.put("MG02-00132-101", "StoredCard Update");
        descMap.put("MG02-00103-101", "StoredCard Delete");
        descMap.put("MG02-00104-101", "StoredCard Lookup");
    }

    private FeatureHelper() {
    }

    static String getDescription(String id) {
        return descMap.get(id);
    }
}
