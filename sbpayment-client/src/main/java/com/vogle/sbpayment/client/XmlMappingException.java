package com.vogle.sbpayment.client;

/**
 * If mapping xml has errors, throw this exception
 *
 * @author Allan Im
 **/
public class XmlMappingException extends RuntimeException {

    private static final long serialVersionUID = 6478950525078875256L;

    /**
     * Constructs a new XmlMappingException with the specified detail message and cause.
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
    public XmlMappingException(String message, Throwable cause) {
        super(message, cause);
    }

}
