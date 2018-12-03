package com.vogle.sbpayment.param;

/**
 * The type of return card informationT
 *
 * @author Allan Im
 **/
public enum CardInfoResponseType {

    NONE("0"), All_MASK("1"), LOWER4("2");

    private String code;

    CardInfoResponseType(String code) {
        this.code = code;
    }

    public String code() {
        return code;
    }
}
