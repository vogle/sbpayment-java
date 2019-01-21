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

    public String code() {
        return code;
    }

    public static DealingsType type(String code) {
        for (DealingsType type : DealingsType.values()) {
            if (code.equalsIgnoreCase(type.code())) {
                return type;
            }
        }
        return LUMP_SUM;
    }
}
