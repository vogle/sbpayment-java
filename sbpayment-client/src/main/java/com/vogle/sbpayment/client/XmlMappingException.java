package com.vogle.sbpayment.client;

/**
 * If mapping xml has errors, throw this exception
 *
 * @author Allan Im
 **/
public class XmlMappingException extends RuntimeException {

    private static final long serialVersionUID = 6478950525078875256L;

    public XmlMappingException(String message, Throwable cause) {
        super(message, cause);
    }

}
