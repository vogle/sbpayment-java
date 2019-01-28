package com.vogle.sbpayment.payeasy.responses;

import java.util.HashMap;
import java.util.Map;

/**
 * PayEasy features lists
 *
 * @author Allan Im
 **/
class FeatureIds {

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
