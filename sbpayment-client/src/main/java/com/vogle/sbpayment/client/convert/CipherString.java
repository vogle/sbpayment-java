package com.vogle.sbpayment.client.convert;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * CipherString annotation<br/>
 * If this annotation is attached to the field, perform 3DES Cipher.<br/>
 * 本アノテーションがフィールドに付いていると、3DES暗号化を実行する。
 *
 * @author Allan Im
 **/
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CipherString {

    /**
     * If it is iterable, you should be set true
     */
    boolean isIterable() default false;

}
