package com.vogle.sbpayment.client;

/**
 * If mapping xml has errors, throw this exception
 *
 * @author Allan Im
 **/
public class XmlMappingException extends RuntimeException {

    public XmlMappingException(String message, Throwable cause) {
        super(message, cause);
    }

}
