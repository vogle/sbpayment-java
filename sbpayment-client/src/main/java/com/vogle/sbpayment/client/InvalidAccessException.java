package com.vogle.sbpayment.client;

/**
 * If The transfer data is invalid, It is occurred
 *
 * @author Allan Im
 */
public class InvalidAccessException extends Exception {

    private static final long serialVersionUID = 4339467173170618163L;

    public InvalidAccessException(String message) {
        super(message);
    }
}
