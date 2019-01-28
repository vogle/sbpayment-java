package com.vogle.sbpayment.client.convert;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * MultiByteString Annotation<br/>
 * If this annotation is attached to the field, perform Base64 Encoding.<br/>
 * 本アノテーションがフィールドに付いていると、Base64Encodingを実行する。
 *
 * @author Allan Im
 **/
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MultiByteString {
}
