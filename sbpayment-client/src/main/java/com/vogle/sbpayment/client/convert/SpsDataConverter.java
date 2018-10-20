package com.vogle.sbpayment.client.convert;

import com.vogle.sbpayment.client.SpsSettings.CipherSets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Data Converter by Softbank payment rules
 *
 * @author Allan Im
 **/
public class SpsDataConverter {

    private static final String PREFIX_GET = "get";
    private static final String PREFIX_SET = "set";
    private static final String ITERATOR = "iterator";

    private static Logger logger = LoggerFactory.getLogger(SpsDataConverter.class);

    /**
     * If the filed has {@link MultiByteString}, It is doing Base64Encoding<br/>
     * フィールドへ{@link MultiByteString}が付いている場合、Base64Encodingする。
     *
     * @param source      Target source
     * @param charsetName charset name (Shift_JIS)
     */
    public static <T> void encode(String charsetName, T source) {
        base64Encode(charsetName, false, source);
    }

    /**
     * If the filed has {@link MultiByteString}, It is doing Base64Encoding<br/>
     * As a condition, when {@link CipherString} is used, Exclude it.<br/>
     * フィールドへ{@link MultiByteString}が付いている場合、Base64Encodingする。<br/>
     * 条件として、暗号化を使う時（アノテーション{@link CipherString}も一緒に付いている場合）は除きます。
     *
     * @param source      Target source
     * @param charsetName charset name (Shift_JIS)
     */
    public static <T> void encodeWithoutCipherField(String charsetName, T source) {
        base64Encode(charsetName, true, source);
    }

    private static <T> void base64Encode(String charsetName, boolean enableCipher, T source) {
        if (source == null) {
            return;
        }
        base64Encode(charsetName, enableCipher, source, source.getClass());
    }

    private static <T> void base64Encode(String charsetName, boolean enableCipher, Object source, Class<T> clazz) {
        if (source == null) {
            return;
        }

        // for supper class
        for (Class<?> currentClass : getClassTree(clazz)) {

            // for filed
            for (Field field : currentClass.getDeclaredFields()) {
                try {
                    if (field.isAnnotationPresent(MultiByteString.class) && field.getType().equals(String.class)) {
                        // without subCipherString field
                        if (enableCipher && field.isAnnotationPresent(CipherString.class)) {
                            continue;
                        }

                        // get value
                        String value = (String) currentClass.getMethod(getterName(field.getName())).invoke(source);

                        // encode value then set
                        if (value != null) {
                            String base64Encode = Base64.getEncoder().encodeToString(value.getBytes(charsetName));
                            currentClass.getMethod(setterName(field.getName()), String.class)
                                    .invoke(source, base64Encode);
                        }
                    } else if (field.isAnnotationPresent(MultiByteString.class)) {
                        if (field.getAnnotation(MultiByteString.class).isIterable()) {
                            Iterable iterable = (Iterable) currentClass.getMethod(getterName(field.getName()))
                                    .invoke(source);
                            if (iterable != null) {
                                Iterator iterator = (Iterator) field.getType().getMethod(ITERATOR).invoke(iterable);
                                while (iterator.hasNext()) {
                                    base64Encode(charsetName, enableCipher, iterator.next());
                                }
                            }
                        } else {
                            base64Encode(charsetName, enableCipher,
                                    currentClass.getMethod(getterName(field.getName())).invoke(source));
                        }
                    }
                } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException ignored) {
                    logger.warn("Check Getter, Setter by field name '{}' ", field.getName());
                } catch (UnsupportedEncodingException ignored) {
                    logger.warn("Unsupported encoding '{}' ", charsetName);
                }
            }
        }
    }

    /**
     * Encrypt the source
     *
     * @param cipherSets  Sps cipherSets settings
     * @param charsetName Character Set name
     * @param source      The source
     * @param <T>         String or Iterable object
     */
    public static <T> void encrypt(CipherSets cipherSets, String charsetName, T source) {
        if (source == null || !cipherSets.isEnabled()) {
            return;
        }
        encrypt(cipherSets, charsetName, source, source.getClass());
    }

    private static <T> void encrypt(CipherSets cipherSets, String charsetName, Object source, Class<T> clazz) {
        if (source == null || !cipherSets.isEnabled()) {
            return;
        }

        // for supper class
        for (Class<?> currentClass : getClassTree(clazz)) {

            // for filed
            for (Field field : currentClass.getDeclaredFields()) {
                try {
                    // Encrypt 3DES
                    if (field.isAnnotationPresent(CipherString.class) && field.getType().equals(String.class)) {
                        // Get value
                        String value = (String) currentClass.getMethod(getterName(field.getName())).invoke(source);

                        // Encrypt 3DES and get value then setup
                        if (value != null) {
                            String encryptValue = SpsSecurity.encrypt(cipherSets, charsetName, value);
                            currentClass.getMethod(setterName(field.getName()), String.class)
                                    .invoke(source, encryptValue);
                        }
                    } else if (field.isAnnotationPresent(CipherString.class)) {
                        if (field.getAnnotation(CipherString.class).isIterable()) {
                            Iterable iterable = (Iterable) currentClass.getMethod(getterName(field.getName()))
                                    .invoke(source);
                            if (iterable != null) {
                                Iterator iterator = (Iterator) field.getType().getMethod(ITERATOR).invoke(iterable);
                                while (iterator.hasNext()) {
                                    encrypt(cipherSets, charsetName, iterator.next());
                                }
                            }
                        } else {
                            encrypt(cipherSets, charsetName,
                                    currentClass.getMethod(getterName(field.getName())).invoke(source));
                        }
                    }

                } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException ignored) {
                    logger.warn("Check Getter, Setter by field name '{}', And you have to return type is String ",
                            field.getName());
                }
            }
        }
    }

    /**
     * Decrypt the source
     *
     * @param cipherSets  Sps cipherSets settings
     * @param charsetName Character Set name
     * @param source      The source
     * @param <T>         String or Iterable object
     */
    public static <T> void decrypt(CipherSets cipherSets, String charsetName, T source) {
        if (source == null || !cipherSets.isEnabled()) {
            return;
        }
        decrypt(cipherSets, charsetName, source, source.getClass());
    }

    private static <T> void decrypt(CipherSets cipherSets, String charsetName, Object source, Class<T> clazz) {

        if (source == null || !cipherSets.isEnabled()) {
            return;
        }

        // for supper class
        for (Class<?> currentClass : getClassTree(clazz)) {

            // for filed
            for (Field field : currentClass.getDeclaredFields()) {
                try {
                    // Decrypt 3DES
                    if (field.isAnnotationPresent(CipherString.class) && field.getType().equals(String.class)) {
                        // get value
                        String value = (String) currentClass.getMethod(getterName(field.getName())).invoke(source);

                        // Decrypt 3DES and get value then setup
                        if (value != null) {
                            String decryptValue = SpsSecurity.decrypt(cipherSets, charsetName, value);
                            currentClass.getMethod(setterName(field.getName()), String.class)
                                    .invoke(source, decryptValue);
                        }
                    } else if (field.isAnnotationPresent(CipherString.class)) {
                        if (field.getAnnotation(CipherString.class).isIterable()) {
                            Iterable iterable = (Iterable) currentClass.getMethod(getterName(field.getName()))
                                    .invoke(source);
                            if (iterable != null) {
                                Iterator iterator = (Iterator) field.getType().getMethod(ITERATOR).invoke(iterable);
                                while (iterator.hasNext()) {
                                    decrypt(cipherSets, charsetName, iterator.next());
                                }
                            }
                        } else {
                            decrypt(cipherSets, charsetName,
                                    currentClass.getMethod(getterName(field.getName())).invoke(source));
                        }
                    }

                } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException ignored) {
                    logger.warn("Check Getter, Setter by field name '{}' ", field.getName());
                }
            }
        }
    }

    private static List<Class<?>> getClassTree(final Class<?> clazz) {
        // check supper class
        List<Class<?>> classList = new ArrayList<>();
        Class<?> checkClass = clazz;
        classList.add(checkClass);
        while (checkClass.getSuperclass() != null && !checkClass.getSuperclass().equals(Object.class)) {
            checkClass = checkClass.getSuperclass();
            classList.add(checkClass);
        }

        // reverse
        Collections.reverse(classList);

        return classList;
    }

    private static String getterName(String fieldName) {
        return PREFIX_GET + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
    }

    private static String setterName(String fieldName) {
        return PREFIX_SET + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
    }

}