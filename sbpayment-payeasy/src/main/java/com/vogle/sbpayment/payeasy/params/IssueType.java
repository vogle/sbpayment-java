package com.vogle.sbpayment.payeasy.params;

/**
 * Issues Type<br/>
 * 請求書発行区分
 *
 * @author Allan Im
 **/
public enum IssueType {

    /**
     * 発行済み
     */
    ISSUED("0"),

    /**
     * 要発行
     */
    REQUIRED_ISSUANCE("1"),

    /**
     * 発行不要
     */
    NO_ISSUE_REQUIRED("2");

    private String code;

    IssueType(String code) {
        this.code = code;
    }

    public String code() {
        return code;
    }
}
