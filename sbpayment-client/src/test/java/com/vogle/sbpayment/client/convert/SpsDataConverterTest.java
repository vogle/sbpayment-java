/*
 * Copyright 2019 Vogle Labs.
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

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link SpsDataConverter}
 *
 * @author Allan Im
 */
public class SpsDataConverterTest {

    private static final String ORIGIN = "Allan Im";
    private static final String ENCODED = "QWxsYW4gSW0=";
    private static final String ENCRYPTED = "P8Rw2Jh3J6A=";
    private final Charset charsetName = Charset.forName("Shift_JIS");
    private final String desKey = "abcdefghyjklmn1234567890";
    private final String desInitKey = "12345678";

    @Test
    public void encode() {
        // given
        SampleObject source = new SampleObject(ORIGIN, ORIGIN, ORIGIN);
        source.setSubBasic64(ORIGIN);
        source.setSubCipherString(ORIGIN);

        // when
        SpsDataConverter.encode(charsetName, source);

        // then
        assertThat(source).isNotNull();
        assertThat(source.getBasic64()).isNotNull();
        assertThat(source.getCipherString()).isNotNull();
        assertThat(source.getBasic64()).isEqualTo(ENCODED);
        assertThat(source.getCipherString()).isEqualTo(ENCODED);
        assertThat(source.getSubBasic64()).isEqualTo(ENCODED);
        assertThat(source.getSubCipherString()).isEqualTo(ENCODED);
        assertThat(source.getItems().get(0).getName()).isEqualTo(ENCODED);
        assertThat(source.getItem().getName()).isEqualTo(ENCODED);
    }

    @Test
    public void encodeWithoutCipherField() {
        // given
        SampleObject source = new SampleObject(ORIGIN, ORIGIN, ORIGIN);
        source.setSubBasic64(ORIGIN);
        source.setSubCipherString(ORIGIN);

        // when
        SpsDataConverter.encodeWithoutCipherString(charsetName, source);

        // then
        assertThat(source).isNotNull();
        assertThat(source.getBasic64()).isNotNull();
        assertThat(source.getCipherString()).isNotNull();
        assertThat(source.getBasic64()).isEqualTo(ENCODED);
        assertThat(source.getCipherString()).isEqualTo(ORIGIN);
        assertThat(source.getSubBasic64()).isEqualTo(ENCODED);
        assertThat(source.getSubCipherString()).isEqualTo(ORIGIN);
        assertThat(source.getItems().get(0).getName()).isEqualTo(ENCODED);
        assertThat(source.getItem().getName()).isEqualTo(ENCODED);
    }

    @Test(expected = InvalidRequestException.class)
    public void encodeWithNoGetterObject() {
        // given
        NoGetterObject source = new NoGetterObject();

        // when
        SpsDataConverter.encode(charsetName, source);
    }

    @Test
    public void encrypt() {
        SampleObject source = new SampleObject(ORIGIN, ORIGIN, ORIGIN);
        source.setSubBasic64(ORIGIN);
        source.setSubCipherString(ORIGIN);

        // encrypt
        SpsDataConverter.encrypt(desKey, desInitKey, charsetName, source);

        assertThat(source.getBasic64()).isNotNull();
        assertThat(source.getCipherString()).isNotNull();
        assertThat(source.getBasic64()).isEqualTo(ORIGIN);
        assertThat(source.getSubBasic64()).isEqualTo(ORIGIN);
        assertThat(source.getItems().get(0).getName()).isEqualTo(ORIGIN);
        assertThat(source.getItem().getName()).isEqualTo(ORIGIN);

        assertThat(source.getCipherString()).isEqualTo(ENCRYPTED);
        assertThat(source.getSubCipherString()).isEqualTo(ENCRYPTED);
        assertThat(source.getItems().get(0).getCipherName()).isEqualTo(ENCRYPTED);
        assertThat(source.getItem().getCipherName()).isEqualTo(ENCRYPTED);
    }

    @Test
    public void decrypt() {
        SampleObject source = new SampleObject(ORIGIN, ORIGIN, ORIGIN);
        source.setSubBasic64(ORIGIN);
        source.setSubCipherString(ORIGIN);

        // encrypt
        SpsDataConverter.encrypt(desKey, desInitKey, charsetName, source);
        // decrypt
        SpsDataConverter.decrypt(desKey, desInitKey, charsetName, source);

        assertThat(source.getBasic64()).isNotNull();
        assertThat(source.getCipherString()).isNotNull();
        assertThat(source.getBasic64()).isEqualTo(ORIGIN);
        assertThat(source.getSubBasic64()).isEqualTo(ORIGIN);
        assertThat(source.getItems().get(0).getName()).isEqualTo(ORIGIN);
        assertThat(source.getItem().getName()).isEqualTo(ORIGIN);

        assertThat(source.getCipherString()).isEqualTo(ORIGIN);
        assertThat(source.getSubCipherString()).isEqualTo(ORIGIN);
        assertThat(source.getItems().get(0).getCipherName()).isEqualTo(ORIGIN);
        assertThat(source.getItem().getCipherName()).isEqualTo(ORIGIN);
    }

    @Test(expected = InvalidRequestException.class)
    public void encryptWithNoGetterObject() {
        // given
        NoGetterObject source = new NoGetterObject();

        // when
        SpsDataConverter.encrypt(desKey, desInitKey, charsetName, source);
    }

    @Test(expected = InvalidRequestException.class)
    public void decryptWithNoGetterObject() {
        // given
        NoGetterObject source = new NoGetterObject();

        // when
        SpsDataConverter.decrypt(desKey, desInitKey, charsetName, source);
    }

    @Test
    public void enableEncryptedFlg() {
        // given
        SampleObject source = new SampleObject();

        // when
        SpsDataConverter.setEncryptedFlg(source, true);

        // then
        assertThat(source.getEncryptedFlg()).isEqualTo("1");
    }

    @Test(expected = InvalidRequestException.class)
    public void enableEncryptedFlgWithNoGetter() {
        // given
        NoGetterObject source = new NoGetterObject();

        // when
        SpsDataConverter.setEncryptedFlg(source, true);
    }

    @Test
    public void makeSpsHashCode() {
        // given
        SampleObject source = new SampleObject();
        Item item = new Item();
        item.setText("TEXT");
        item.setNumber(9);
        source.setItem(item);
        source.setItems(new ArrayList<>());
        String hashKey = "HASH_KEY";

        // when
        String result = SpsDataConverter.makeSpsHashCode(source, hashKey, charsetName);

        // then
        assertThat(result).isEqualTo("2004fd019ce9e4bec52e7e5b74a4adad09dbed7d");
    }

    @Test(expected = MakeHashCodeException.class)
    public void makeSpsHashCodeWithFail() {
        SpsDataConverter.makeSpsHashCode(new Object(), "key", charsetName);
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    private class SampleObject extends SuperObject {

        @MultiByteString
        private String subBasic64;

        @MultiByteString
        @CipherString
        private String subCipherString;

        @MultiByteString
        @CipherString
        private List<Item> items;

        @MultiByteString
        @CipherString
        private Item item;

        SampleObject() {
            super();
        }

        SampleObject(String basic64, String subCipherString, String itemName) {
            super(basic64, subCipherString);

            Item item = new Item();
            item.setName(itemName);
            item.setCipherName(itemName);

            this.items = new ArrayList<>();
            this.items.add(item);

            Item item2 = new Item();
            item2.setName(itemName);
            item2.setCipherName(itemName);
            this.item = item2;
        }
    }

    @Data
    private class SuperObject {

        @MultiByteString
        private String basic64;

        @MultiByteString
        @CipherString
        private String cipherString;

        private String encryptedFlg;

        SuperObject() {
        }

        SuperObject(String basic64, String cipherString) {
            this.basic64 = basic64;
            this.cipherString = cipherString;
        }
    }

    @Data
    private class Item {
        @MultiByteString
        private String name;

        @CipherString
        private String cipherName;

        private String text;

        private int number;
    }

    private class NoGetterObject {
        @MultiByteString
        String some;

        @CipherString
        String cipher;

        String encryptedFlg;
    }
}