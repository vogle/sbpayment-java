package com.vogle.sbpayment.client.response;

/**
 * Payment Status
 *
 * @author Allan Im
 **/
public enum PaymentStatus {

    AUTHORIZED("1"), CAPTURED("2"), CANCELED("3"), REFUNDED("4");

    private String statusCode;

    PaymentStatus(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getPaymentStatusCode() {
        return statusCode;
    }

    public static PaymentStatus status(String code) {
        for (PaymentStatus status : PaymentStatus.values()) {
            if (code.equalsIgnoreCase(status.getPaymentStatusCode())) {
                return status;
            }
        }
        return null;
    }

}
