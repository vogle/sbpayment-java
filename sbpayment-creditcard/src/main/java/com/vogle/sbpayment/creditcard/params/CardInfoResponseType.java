package com.vogle.sbpayment.creditcard.params;

/**
 * The type of return card information
 *
 * @author Allan Im
 **/
public enum CardInfoResponseType {

    NONE("0"), ALL_MASK("1"), LOWER4("2");

    private String code;

    CardInfoResponseType(String code) {
        this.code = code;
    }

    public String code() {
        return code;
    }
}
