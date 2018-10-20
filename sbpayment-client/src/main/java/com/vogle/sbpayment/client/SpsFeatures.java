package com.vogle.sbpayment.client;

import java.util.HashMap;
import java.util.Map;

/**
 * Features lists
 *
 * @author Allan Im
 **/
class SpsFeatures {

    private static Map<String, String> featureMap = new HashMap<>();

    static {

        // Common features
        featureMap.put("MG01-00101-101", "PayCard Lookup Transaction");

        // Card payment features
        featureMap.put("ST01-00111-101", "PayCard Authorize");
        featureMap.put("ST01-00113-101", "PayCard Reauthorize");
        featureMap.put("ST02-00101-101", "PayCard Commit");
        featureMap.put("ST02-00201-101", "PayCard Capture");
        featureMap.put("ST02-00303-101", "PayCard Cancel/Refund");
        featureMap.put("ST02-00307-101", "PayCard Partial Refund");

        // Card information save features
        featureMap.put("MG02-00101-101", "StoredCard Save");
        featureMap.put("MG02-00102-101", "StoredCard Update");
        featureMap.put("MG02-00103-101", "StoredCard Delete");
        featureMap.put("MG02-00104-101", "StoredCard Lookup");

        // Token features
        featureMap.put("ST01-00131-101", "Token Authorize");

        // PayEasy
        featureMap.put("ST01-00101-703", "PayEasy Payment");
        featureMap.put("NT01-00103-703", "PayEasy Deposit Notice");
        featureMap.put("NT01-00104-703", "PayEasy Expired Notice");
    }

    static String getFeatureName(String id) {
        return featureMap.get(id);
    }
}