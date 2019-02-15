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
import org.junit.Test;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link SpsDataConverter}
 *
 * @author Allan Im
 */
public class SpsDataConverterTest {

    private final Charset charsetName = Charset.forName("Shift_JIS");
    private final String desKey = "abcdefghyjklmn1234567890";
    private final String desInitKey = "12345678";

    @Test
    public void encode() {
        // given
        String origin = "Allan Im";
        String encoded = "QWxsYW4gSW0=";
        SampleObject source = new SampleObject(origin, origin, origin);
        source.setSubBasic64(origin);
        source.setSubCipherString(origin);

        // when
        SpsDataConverter.encode(charsetName, source);

        // then
        assertThat(source).isNotNull();
        assertThat(source.getBasic64()).isNotNull();
        assertThat(source.getCipherString()).isNotNull();
        assertThat(source.getBasic64()).isEqualTo(encoded);
        assertThat(source.getCipherString()).isEqualTo(encoded);
        assertThat(source.getSubBasic64()).isEqualTo(encoded);
        assertThat(source.getSubCipherString()).isEqualTo(encoded);
        assertThat(source.getItems().get(0).getName()).isEqualTo(encoded);
        assertThat(source.getItem().getName()).isEqualTo(encoded);
    }

    @Test
    public void encodeWithoutCipherField() {
        // given
        String origin = "Allan Im";
        String encoded = "QWxsYW4gSW0=";
        SampleObject source = new SampleObject(origin, origin, origin);
        source.setSubBasic64(origin);
        source.setSubCipherString(origin);

        // when
        SpsDataConverter.encodeWithoutCipherString(charsetName, source);

        // then
        assertThat(source).isNotNull();
        assertThat(source.getBasic64()).isNotNull();
        assertThat(source.getCipherString()).isNotNull();
        assertThat(source.getBasic64()).isEqualTo(encoded);
        assertThat(source.getCipherString()).isEqualTo(origin);
        assertThat(source.getSubBasic64()).isEqualTo(encoded);
        assertThat(source.getSubCipherString()).isEqualTo(origin);
        assertThat(source.getItems().get(0).getName()).isEqualTo(encoded);
        assertThat(source.getItem().getName()).isEqualTo(encoded);
    }

    @Test(expected = InvalidRequestException.class)
    public void encodeWithNoGetterObject() {
        // given
        NoGetterObject source = new NoGetterObject();

        // when
        SpsDataConverter.encode(charsetName, source);
    }

    @Test
    public void encryptAndDecrypt() {
        String origin = "Allan Im";
        String encrypted = "P8Rw2Jh3J6A=";
        SampleObject source = new SampleObject(origin, origin, origin);
        source.setSubBasic64(origin);
        source.setSubCipherString(origin);

        // encrypt
        SpsDataConverter.encrypt(desKey, desInitKey, charsetName, source);

        assertThat(source).isNotNull();
        assertThat(source.getBasic64()).isNotNull();
        assertThat(source.getCipherString()).isNotNull();
        assertThat(source.getBasic64()).isEqualTo(origin);
        assertThat(source.getSubBasic64()).isEqualTo(origin);
        assertThat(source.getItems().get(0).getName()).isEqualTo(origin);
        assertThat(source.getItem().getName()).isEqualTo(origin);

        assertThat(source.getCipherString()).isEqualTo(encrypted);
        assertThat(source.getSubCipherString()).isEqualTo(encrypted);
        assertThat(source.getItems().get(0).getCipherName()).isEqualTo(encrypted);
        assertThat(source.getItem().getCipherName()).isEqualTo(encrypted);

        // decrypt
        SpsDataConverter.decrypt(desKey, desInitKey, charsetName, source);

        assertThat(source).isNotNull();
        assertThat(source.getBasic64()).isNotNull();
        assertThat(source.getCipherString()).isNotNull();
        assertThat(source.getBasic64()).isEqualTo(origin);
        assertThat(source.getSubBasic64()).isEqualTo(origin);
        assertThat(source.getItems().get(0).getName()).isEqualTo(origin);
        assertThat(source.getItem().getName()).isEqualTo(origin);

        assertThat(source.getCipherString()).isEqualTo(origin);
        assertThat(source.getSubCipherString()).isEqualTo(origin);
        assertThat(source.getItems().get(0).getCipherName()).isEqualTo(origin);
        assertThat(source.getItem().getCipherName()).isEqualTo(origin);
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

//    @Test(expected = MakeHashCodeException.class)
    public void makeSpsHashCodeWithFail() {
        SpsDataConverter.makeSpsHashCode(new Object(), "key", charsetName);
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public class SampleObject extends SuperObject {

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

        public SampleObject() {
        }

        public SampleObject(String basic64, String subCipherString, String itemName) {
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
    public class SuperObject {

        @MultiByteString
        private String basic64;

        @MultiByteString
        @CipherString
        private String cipherString;

        private String encryptedFlg;

        public SuperObject() {
        }

        public SuperObject(String basic64, String cipherString) {
            this.basic64 = basic64;
            this.cipherString = cipherString;
        }
    }

    @Data
    public class Item {
        @MultiByteString
        private String name;

        @CipherString
        private String cipherName;

        private String text;

        private int number;
    }

    public class NoGetterObject {
        @MultiByteString
        private String some;

        @CipherString
        private String cipher;

        private String encryptedFlg;
    }
}