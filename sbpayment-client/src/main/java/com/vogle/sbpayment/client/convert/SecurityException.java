package com.vogle.sbpayment.client.convert;

/**
 * Security Exception
 *
 * @author Allan Im
 **/
public class SecurityException extends RuntimeException {

    private static final long serialVersionUID = 8140084118825445770L;

    /**
     * Constructs a new SecurityException with the specified detail message and cause.
     * <p>Note that the detail message associated with {@code cause} is <i>not</i> automatically
     * incorporated in this exception's detail message.</p>
     *
     * @param message the detail message (which is saved for later retrieval
     *                by the {@link #getMessage()} method).
     * @param cause   the cause (which is saved for later retrieval by the
     *                {@link #getCause()} method).  (A <tt>null</tt> value is
     *                permitted, and indicates that the cause is nonexistent or
     *                unknown.)
     */
    public SecurityException(String message, Throwable cause) {
        super(message, cause);
    }

}
