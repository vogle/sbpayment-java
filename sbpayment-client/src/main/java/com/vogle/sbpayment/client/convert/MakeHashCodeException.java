package com.vogle.sbpayment.client.convert;

/**
 * If making hash code has errors, throw this exception
 *
 * @author Allan Im
 **/
public class MakeHashCodeException extends RuntimeException {

    private static final long serialVersionUID = -2384707405828252426L;

    public MakeHashCodeException(Throwable cause) {
        super(cause);
    }
}
