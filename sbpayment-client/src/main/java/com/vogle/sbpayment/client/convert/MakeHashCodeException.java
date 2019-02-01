package com.vogle.sbpayment.client.convert;

/**
 * If making hash code has errors, throw this exception
 *
 * @author Allan Im
 **/
public class MakeHashCodeException extends RuntimeException {

    private static final long serialVersionUID = -2384707405828252426L;

    /**
     * Constructs a new MakeHashCodeException with the specified cause and a detail
     * message of <tt>(cause==null ? null : cause.toString())</tt> (which typically
     * contains the class and detail message of <tt>cause</tt>).  This constructor is
     * useful for runtime exceptions that are little more than wrappers for other
     * throwables.
     *
     * @param cause the cause (which is saved for later retrieval by the {@link #getCause()}
     *              method).  (A <tt>null</tt> value is permitted, and indicates that the
     *              cause is nonexistent or unknown.)
     */
    public MakeHashCodeException(Throwable cause) {
        super(cause);
    }
}
