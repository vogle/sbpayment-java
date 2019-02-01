package com.vogle.sbpayment.client.requests;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.vogle.sbpayment.client.params.Item;

/**
 * Request mapper
 *
 * @author Allan Im
 **/
public class RequestHelper {

    /**
     * convert Item to Pay detail
     *
     * @param items Item list
     * @return PayDetail list
     */
    public static List<PayDetail> mapItem(List<Item> items) {
        List<PayDetail> details = new ArrayList<>();
        if (items != null && !items.isEmpty()) {
            details = new ArrayList<>();
            int rowNo = 1;
            for (Item item : items) {
                PayDetail detail = new PayDetail();
                detail.setDtlRowno(rowNo++);
                detail.setDtlItemId(item.getItemId());
                detail.setDtlItemName(item.getItemName());
                detail.setDtlItemCount(item.getItemCount());
                detail.setDtlAmount(item.getItemAmount());
                detail.setDtlTax(item.getItemTax());

                details.add(detail);
            }
        }
        return details;
    }

    /**
     * Check flag
     *
     * @param flag true or false
     * @return 1 or 0
     */
    public static String flag(boolean flag) {
        return flag ? "1" : "0";
    }

    /**
     * Gets date from the origin
     *
     * @param origin have to format that is "yyyyMMdd******"
     * @return yyyyMMdd
     */
    public static String dateOnly(String origin) {
        return dateOnly(origin, 0);
    }

    /**
     * Gets date from the origin
     *
     * @param origin    have to format that is "yyyyMMdd******"
     * @param daysToAdd days to add
     * @return yyyyMMdd
     */
    public static String dateOnly(String origin, int daysToAdd) {
        if (origin == null || origin.length() < 8) {
            throw new IllegalArgumentException("The length of the origin must be greater than or equal to eight.");
        }
        int year = Integer.parseInt(origin.substring(0, 4));
        int month = Integer.parseInt(origin.substring(4, 6));
        int day = Integer.parseInt(origin.substring(6, 8));

        LocalDate date = LocalDate.of(year, month, day).plusDays(daysToAdd);
        return date.format(DateTimeFormatter.BASIC_ISO_DATE);
    }

    /**
     * Avoid NULL
     *
     * @param origin       origin data
     * @param defaultValue default value
     * @return return The Default value if origin data is null
     */
    public static String avoidNull(String origin, String defaultValue) {
        return (origin == null) ? defaultValue : origin;
    }
}
