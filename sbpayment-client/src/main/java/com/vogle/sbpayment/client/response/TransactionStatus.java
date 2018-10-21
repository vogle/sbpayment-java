package com.vogle.sbpayment.client.response;

/**
 * Transaction Status, '0' is Ok, '1' is Error
 *
 * @author Allan Im
 **/
public enum TransactionStatus {

    NORMAL("0"), ERROR("1");

    private String statusCode;

    TransactionStatus(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getTransactionStatusCode() {
        return statusCode;
    }

    public static TransactionStatus status(String code) {
        for (TransactionStatus status : TransactionStatus.values()) {
            if (code.equalsIgnoreCase(status.getTransactionStatusCode())) {
                return status;
            }
        }
        return ERROR;
    }

}
