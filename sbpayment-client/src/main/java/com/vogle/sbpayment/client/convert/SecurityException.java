package com.vogle.sbpayment.client.convert;

/**
 * Security Exception
 *
 * @author Allan Im
 **/
public class SecurityException extends RuntimeException {

    private static final long serialVersionUID = 8140084118825445770L;

    public SecurityException(String message, Throwable cause) {
        super(message, cause);
    }

}
