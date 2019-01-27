package com.vogle.sbpayment.payeasy.params;

/**
 * Terminal Type <br/>
 * 端末種別
 *
 * @author Allan Im
 **/
public enum TerminalValue {

    PC("P"), DOCOMO("D"), SOFTBANK("S"), AU("A"), L_MODE("L");

    private String code;

    TerminalValue(String code) {
        this.code = code;
    }

    public String code() {
        return code;
    }
}
