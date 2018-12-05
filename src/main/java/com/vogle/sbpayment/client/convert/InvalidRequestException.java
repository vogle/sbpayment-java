package com.vogle.sbpayment.client.convert;

/**
 * Invalid request object exception
 *
 * @author Allan Im
 */
public class InvalidRequestException extends RuntimeException {

    private static final long serialVersionUID = -7541819458092770124L;

    public InvalidRequestException(Throwable cause) {
        super(cause);
    }

}
