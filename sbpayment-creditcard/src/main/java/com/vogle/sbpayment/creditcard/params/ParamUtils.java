package com.vogle.sbpayment.creditcard.params;

import com.vogle.sbpayment.creditcard.DealingsType;

/**
 * Param Utils
 *
 * @author Allan Im
 */
class ParamUtils {

    static String convertBool(boolean enabled) {
        return enabled ? "1" : "0";
    }

    static DealingsType convertDealingsType(DealingsType dealingsType, Integer divideTimes) {
        if (DealingsType.INSTALLMENT.equals(dealingsType) && (divideTimes == null)) {
            return DealingsType.LUMP_SUM;
        } else {
            return dealingsType;
        }
    }

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
