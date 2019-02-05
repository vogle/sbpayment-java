/*
 * Copyright 2019 VOGLE Labs.
 *
 * This file is part of sbpayment-java - Sbpayment client.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.vogle.sbpayment.client.convert;

import com.vogle.sbpayment.client.ValidationHelper;

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
import java.util.Locale;

/**
 * Data Converter by Softbank payment rules
 *
 * @author Allan Im
 **/
public class SpsDataConverter {

    private static final String PREFIX_GET = "get";
    private static final String PREFIX_SET = "set";
    private static final String ITERATOR = "iterator";

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private static final Logger LOGGER = LoggerFactory.getLogger(SpsDataConverter.class);

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
        ValidationHelper.assertsNotEmpty("charsetName", charsetName);
        ValidationHelper.assertsNotNull("source", source);

        // for supper class
        for (Class<?> currentClass : getClassTree(source.getClass())) {
            // for filed
            for (Field field : currentClass.getDeclaredFields()) {
                // Base64 Encode by MultiByteString
                if (field.isAnnotationPresent(MultiByteString.class)) {
                    if (String.class.isAssignableFrom(field.getType())) {
                        // without subCipherString field
                        if (enableCipher && field.isAnnotationPresent(CipherString.class)) {
                            continue;
                        }

                        // get value
                        String value = getValueFrom(source, field);

                        // encode value then set
                        if (value != null) {
                            setValueTo(source, field, encodeToString(charsetName, value));
                        }
                    } else if (Iterable.class.isAssignableFrom(field.getType())) {
                        Iterator iterator = getIteratorFrom(source, field);
                        while (iterator.hasNext()) {
                            base64Encode(charsetName, enableCipher, iterator.next());
                        }
                    } else {
                        Object filedValue = getObjectFrom(source, field);
                        if (filedValue != null) {
                            base64Encode(charsetName, enableCipher, getObjectFrom(source, field));
                        }
                    }
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
        ValidationHelper.assertsNotEmpty("charsetName", charsetName);
        ValidationHelper.assertsNotNull("source", source);

        // for supper class
        for (Class<?> currentClass : getClassTree(source.getClass())) {
            // for filed
            for (Field field : currentClass.getDeclaredFields()) {
                // Encrypt 3DES by CipherString
                if (field.isAnnotationPresent(CipherString.class)) {
                    if (String.class.isAssignableFrom(field.getType())) {
                        // get value
                        String value = getValueFrom(source, field);

                        // Encrypt 3DES and get value then setup
                        if (value != null) {
                            String encryptValue = SpsSecurity.encrypt(desKey, initKey, charsetName, value);
                            setValueTo(source, field, encryptValue);
                        }
                    } else if (Iterable.class.isAssignableFrom(field.getType())) {
                        Iterator iterator = getIteratorFrom(source, field);
                        while (iterator.hasNext()) {
                            encrypt(desKey, initKey, charsetName, iterator.next());
                        }
                    } else {
                        Object value = getObjectFrom(source, field);
                        if (value != null) {
                            encrypt(desKey, initKey, charsetName, value);
                        }
                    }
                }
            }
        }
    }

    private static String encodeToString(String charsetName, String value) {
        try {
            return Base64.getEncoder().encodeToString(value.getBytes(charsetName));
        } catch (UnsupportedEncodingException ex) {
            LOGGER.error("Do not encode because Unsupported encoding '{}' ", charsetName);
            throw new InvalidRequestException(ex);
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
        ValidationHelper.assertsNotEmpty("charsetName", charsetName);
        ValidationHelper.assertsNotNull("source", source);

        // for supper class
        for (Class<?> currentClass : getClassTree(source.getClass())) {
            // for filed
            for (Field field : currentClass.getDeclaredFields()) {
                // Decrypt 3DES
                if (field.isAnnotationPresent(CipherString.class)) {
                    if (String.class.isAssignableFrom(field.getType())) {
                        // get value
                        String value = getValueFrom(source, field);

                        // Decrypt 3DES and get value then setup
                        if (value != null) {
                            String decryptValue = SpsSecurity.decrypt(desKey, initKey, charsetName, value);
                            setValueTo(source, field, decryptValue);
                        }
                    } else if (Iterable.class.isAssignableFrom(field.getType())) {
                        Iterator iterator = getIteratorFrom(source, field);
                        while (iterator.hasNext()) {
                            decrypt(desKey, initKey, charsetName, iterator.next());
                        }
                    } else {
                        // get value
                        Object value = getObjectFrom(source, field);
                        if (value != null) {
                            decrypt(desKey, initKey, charsetName, value);
                        }
                    }
                }
            }
        }
    }

    /**
     * The encryptedFlg of source sets "1", this mean that enable the cipher<br/>
     * Sourceオブジェクトで「encryptedFlg」フィールドを探して、”1”を登録する。暗号化をする場合、実行する。
     *
     * @param source check object source
     */
    public static <T> void enableEncryptedFlg(T source) {
        ValidationHelper.assertsNotNull("source", source);

        // for supper class
        for (Class<?> currentClass : getClassTree(source.getClass())) {

            // for filed
            try {
                Field field = currentClass.getDeclaredField("encryptedFlg");
                if (field.getType().equals(String.class)) {
                    // Encrypted Flag set enable value that is "1"
                    setValueTo(source, field, "1");
                }
            } catch (NoSuchFieldException ignored) {
            }

        }
    }

    /**
     * Make hash-code by Softbank payment rules
     *
     * @param value       Request object
     * @param hashKey     Sbpayment Hash-Key
     * @param charsetName Sbpayment charset
     */
    public static String makeSpsHashCode(Object value, String hashKey, String charsetName) {
        ValidationHelper.assertsNotNull("value", value);
        ValidationHelper.assertsNotEmpty("hashKey", hashKey);
        ValidationHelper.assertsNotEmpty("charsetName", charsetName);

        try {
            byte[] json = MAPPER.writeValueAsBytes(value);

            // hash code source
            StringBuilder spsHashCodeSource = new StringBuilder();

            // data
            JsonNode jsonNode = MAPPER.readTree(json);
            Iterator<String> nodeNames = jsonNode.fieldNames();
            while (nodeNames.hasNext()) {
                String filed = nodeNames.next();
                // set filed without "id" & "spsHashcode" because made hashcode is do not need them
                if (!"id".equals(filed) && !"spsHashcode".equals(filed)) {
                    spsHashCodeSource.append(textValue(jsonNode.findValue(filed)));
                }
            }

            // set hash key
            spsHashCodeSource.append(hashKey);

            return DigestUtils.sha1Hex(spsHashCodeSource.toString().getBytes(charsetName));

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
        return PREFIX_GET + fieldName.substring(0, 1).toUpperCase(Locale.ENGLISH) + fieldName.substring(1);
    }

    private static String setterName(String fieldName) {
        return PREFIX_SET + fieldName.substring(0, 1).toUpperCase(Locale.ENGLISH) + fieldName.substring(1);
    }

    private static Object getObjectFrom(Object source, Field field) {
        try {
            return source.getClass().getMethod(getterName(field.getName())).invoke(source);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
            LOGGER.error("Must check a getter for field: '{}'", field.getName());
            throw new InvalidRequestException(ex);
        }
    }

    private static Iterator getIteratorFrom(Object source, Field field) {
        try {
            Iterable iterable = (Iterable) source.getClass().getMethod(getterName(field.getName())).invoke(source);
            return (iterable == null) ? Collections.emptyIterator()
                    : (Iterator) field.getType().getMethod(ITERATOR).invoke(iterable);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
            LOGGER.error("Is not iterator field: '{}'", field.getName());
            throw new InvalidRequestException(ex);
        }
    }

    private static String getValueFrom(Object source, Field field) {
        try {
            return (String) source.getClass().getMethod(getterName(field.getName())).invoke(source);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
            LOGGER.error("Must check a getter for field: '{}', And it have to return that is the String type.",
                    field.getName());
            throw new InvalidRequestException(ex);
        }
    }

    private static void setValueTo(Object source, Field field, String value) {
        try {
            source.getClass().getMethod(setterName(field.getName()), String.class)
                    .invoke(source, value);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
            LOGGER.error("Must check a setter for field: '{}', And it have to parameter that is the String type. ",
                    field.getName());
            throw new InvalidRequestException(ex);
        }
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
