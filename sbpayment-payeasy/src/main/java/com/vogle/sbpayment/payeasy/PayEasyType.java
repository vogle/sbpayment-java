package com.vogle.sbpayment.payeasy;

/**
 * Online or Link<br/>
 * Online：オンライン（ATM）方式<br/>
 * Link：情報リンク方式（インターネットでの支払い）
 */
public enum PayEasyType {
    ONLINE("O"), LINK("L");

    private String code;

    PayEasyType(String code) {
        this.code = code;
    }

    public String code() {
        return code;
    }
}