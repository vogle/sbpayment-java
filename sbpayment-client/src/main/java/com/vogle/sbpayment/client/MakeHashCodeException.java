package com.vogle.sbpayment.client;

/**
 * If making hash code has errors, throw this exception
 *
 * @author Allan Im
 **/
public class MakeHashCodeException extends RuntimeException {

    public MakeHashCodeException(Throwable cause) {
        super(cause);
    }
}
