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
