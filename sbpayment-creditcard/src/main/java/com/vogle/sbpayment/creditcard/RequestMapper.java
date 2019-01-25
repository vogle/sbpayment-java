package com.vogle.sbpayment.creditcard;

import com.vogle.sbpayment.creditcard.params.Item;
import com.vogle.sbpayment.creditcard.requests.PayDetail;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Request mapper
 *
 * @author Allan Im
 **/
public class RequestMapper {

    /**
     * Item convert to Pay detail
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
     * Add date
     *
     * @param payDate The date
     * @param addDay  Add day
     * @return PayDate  + AddDay
     */
    public static String addDay(String payDate, int addDay) {
        int year = Integer.parseInt(payDate.substring(0, 4));
        int month = Integer.parseInt(payDate.substring(4, 6)) - 1;
        int day = Integer.parseInt(payDate.substring(6, 8));

        Calendar c = Calendar.getInstance();
        c.set(year, month, day);
        c.add(Calendar.DATE, addDay);

        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
        return fmt.format(c.getTime());
    }
}
