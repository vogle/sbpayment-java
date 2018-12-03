package com.vogle.sbpayment.client.convert;

import com.vogle.sbpayment.client.MakeHashCodeException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
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

    private static final ObjectMapper objectMapper = new ObjectMapper();

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
    public static <T> void encodeWithoutCipherString(String charsetName, T source) {
        base64Encode(charsetName, true, source);
    }

    private static <T> void base64Encode(String charsetName, boolean enableCipher, T source) {
        assert source != null;
        base64Encode(charsetName, enableCipher, source, source.getClass());
    }

    private static <T> void base64Encode(String charsetName, boolean enableCipher, Object source, Class<T> clazz) {
        assert source != null;

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
                } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
                    logger.error("Check Getter, Setter by field name '{}' ", field.getName());
                    throw new InvalidRequestException(ex);
                } catch (UnsupportedEncodingException ignored) {
                    logger.error("Do not encode, because Unsupported encoding '{}' ", charsetName);
                }
            }
        }
    }

    /**
     * Encrypt the source
     *
     * @param desKey      The DES cipherSets key
     * @param initKey     The DES initialization key
     * @param charsetName Character Set name
     * @param source      The source
     * @param <T>         String or Iterable object
     */
    public static <T> void encrypt(String desKey, String initKey, String charsetName, T source) {
        assert source != null;
        encrypt(desKey, initKey, charsetName, source, source.getClass());
    }

    private static <T> void encrypt(String desKey, String initKey, String charsetName, Object source, Class<T> clazz) {

        // for supper class
        for (Class<?> currentClass : getClassTree(clazz)) {

            // for filed
            for (Field field : currentClass.getDeclaredFields()) {
                String fieldName = field.getName();

                try {
                    // Encrypt 3DES
                    if (field.isAnnotationPresent(CipherString.class) && field.getType().equals(String.class)) {
                        // Get value
                        String value = (String) currentClass.getMethod(getterName(fieldName)).invoke(source);

                        // Encrypt 3DES and get value then setup
                        if (value != null) {
                            String encryptValue = SpsSecurity.encrypt(desKey, initKey, charsetName, value);
                            currentClass.getMethod(setterName(fieldName), String.class)
                                    .invoke(source, encryptValue);
                        }
                    } else if (field.isAnnotationPresent(CipherString.class)) {
                        if (field.getAnnotation(CipherString.class).isIterable()) {
                            Iterable iterable = (Iterable) currentClass.getMethod(getterName(fieldName))
                                    .invoke(source);
                            if (iterable != null) {
                                Iterator iterator = (Iterator) field.getType().getMethod(ITERATOR).invoke(iterable);
                                while (iterator.hasNext()) {
                                    encrypt(desKey, initKey, charsetName, iterator.next());
                                }
                            }
                        } else {
                            // get value
                            Object value = currentClass.getMethod(getterName(fieldName)).invoke(source);
                            if (value != null) {
                                encrypt(desKey, initKey, charsetName, value);
                            }
                        }
                    }

                } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
                    logger.error("Check Getter, Setter by field name '{}', And you have to return type is String ",
                            fieldName);
                    throw new InvalidRequestException(ex);
                }
            }
        }
    }

    /**
     * Decrypt the source
     *
     * @param desKey      The DES cipherSets key
     * @param initKey     The DES initialization key
     * @param charsetName Character Set name
     * @param source      The source
     * @param <T>         String or Iterable object
     */
    public static <T> void decrypt(String desKey, String initKey, String charsetName, T source) {
        assert source != null;
        decrypt(desKey, initKey, charsetName, source, source.getClass());
    }

    private static <T> void decrypt(String desKey, String initKey, String charsetName, Object source, Class<T> clazz) {

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
                            String decryptValue = SpsSecurity.decrypt(desKey, initKey, charsetName, value);
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
                                    decrypt(desKey, initKey, charsetName, iterator.next());
                                }
                            }
                        } else {
                            // get value
                            Object value = currentClass.getMethod(getterName(field.getName())).invoke(source);
                            if (value != null) {
                                decrypt(desKey, initKey, charsetName, value);
                            }

                        }
                    }

                } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
                    logger.error("Check Getter, Setter by field name '{}' ", field.getName());
                    throw new InvalidRequestException(ex);
                }
            }
        }
    }

    /**
     * The encryptedFlg of source sets "1", this mean that enable the cipher<br/>
     * Sourceオブジェクトで「encryptedFlg」フィールドを探して、”1”を登録する。暗号化をする場合、実行する。
     *
     * @param source check object source
     * @param clazz  source type
     */
    public static <T> void enableEncryptedFlg(Object source, Class<T> clazz) {

        // for supper class
        for (Class<?> currentClass : getClassTree(clazz)) {

            // for filed
            try {
                Field field = currentClass.getDeclaredField("encryptedFlg");
                if (field.getType().equals(String.class)) {

                    // Encrypted Flag set enable value that is "1"
                    try {
                        currentClass.getMethod(setterName(field.getName()), String.class).invoke(source, "1");
                    } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
                        logger.error("Check Setter by field name '{}'", field.getName());
                        throw new InvalidRequestException(ex);
                    }
                }
            } catch (NoSuchFieldException ignored) {
            }

        }
    }

    /**
     * Make hash-code by Softbank payment rules
     *
     * @param value   Request object
     * @param hashKey Sbpayment Hash-Key
     * @param charset Sbpayment charset
     */
    public static String makeSpsHashCode(Object value, String hashKey, String charset) {
        try {
            byte[] json = objectMapper.writeValueAsBytes(value);

            // hash code
            StringBuilder spsHashCode = new StringBuilder();

            // data
            JsonNode jsonNode = objectMapper.readTree(json);
            Iterator<String> nodeNames = jsonNode.fieldNames();
            while (nodeNames.hasNext()) {
                String filed = nodeNames.next();
                // hash code
                if (!"id".equals(filed) && !"spsHashcode".equals(filed)) {
                    spsHashCode.append(textValue(jsonNode.findValue(filed)));
                }
            }

            // sps hash key
            spsHashCode.append(hashKey);

            return DigestUtils.sha1Hex(spsHashCode.toString().getBytes(charset));

        } catch (IOException ex) {
            throw new MakeHashCodeException(ex);
        }
    }


    private static List<Class<?>> getClassTree(Class<?> clazz) {
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

    private static String textValue(JsonNode jsonNode) {
        StringBuilder result = new StringBuilder();

        if (jsonNode.isObject() || jsonNode.isArray()) {
            Iterator<JsonNode> subNode = jsonNode.elements();
            while (subNode.hasNext()) {
                result.append(textValue(subNode.next()));
            }
        } else if (jsonNode.isTextual()) {
            result.append(jsonNode.textValue().trim());
        } else if (jsonNode.isNumber()) {
            result.append(jsonNode.numberValue());
        }
        return result.toString();
    }

}
