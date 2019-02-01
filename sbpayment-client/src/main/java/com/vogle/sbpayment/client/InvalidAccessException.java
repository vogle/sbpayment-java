package com.vogle.sbpayment.client;

/**
 * If The transfer data is invalid, to use this
 *
 * @author Allan Im
 */
public class InvalidAccessException extends Exception {

    private static final long serialVersionUID = 4339467173170618163L;

    /**
     * Constructs a new InvalidAccessException with the specified detail message.
     *
     * @param message the detail message. The detail message is saved for later
     *                retrieval by the {@link #getMessage()} method.
     */
    public InvalidAccessException(String message) {
        super(message);
    }

}
