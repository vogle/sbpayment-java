package com.vogle.sbpayment.creditcard;

/**
 * Card Brand Type
 *
 * @author Allan Im
 **/
public enum CreditCardBrand {

    JCB("J"), VISA("V"), MASTER("M"), AMEX("A"), DINERS("D"), OTHER("X");

    private String spsCardbrandCode;

    CreditCardBrand(String spsCardbrandCode) {
        this.spsCardbrandCode = spsCardbrandCode;
    }

    /**
     * Gets a brand
     * <p>
     * Code: Brand
     * "J": JCB
     * "V": VISA
     * "M": MASTER
     * "A": AMEX
     * "D": DINERS
     * </p>
     *
     * @param code brand code
     * @return Card Brand
     */
    public static CreditCardBrand brand(String code) {
        for (CreditCardBrand brand : CreditCardBrand.values()) {
            if (code.equalsIgnoreCase(brand.getSpsCardbrandCode())) {
                return brand;
            }
        }
        return OTHER;
    }

    /**
     * Gets Brand Code
     *
     * @return "J", "V", "M", "A" or "D"
     */
    public String getSpsCardbrandCode() {
        return spsCardbrandCode;
    }
}
