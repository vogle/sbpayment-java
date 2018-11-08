package com.vogle.sbpayment.client.convert;

import com.vogle.sbpayment.client.SpsClientSettings;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link SpsDataConverter}
 *
 * @author Allan Im
 */
public class SpsDataConverterTest {

    private SpsClientSettings.CipherSets cipherSets;
    private final String charsetName = "Shift_JIS";

    @Before
    public void init() {
        cipherSets = new SpsClientSettings.CipherSets();
        cipherSets.setEnabled(true);
        cipherSets.setDesKey("abcdefghyjklmn1234567890");
        cipherSets.setDesInitKey("12345678");
    }

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
    public void encodeWithUnsupportedCharsetName() {
        // given
        String origin = "Allan Im";
        SampleObject source = new SampleObject(origin, origin, origin);
        source.setSubBasic64(origin);
        source.setSubCipherString(origin);

        // when
        SpsDataConverter.encode("ALLAN", source);

        // then
        assertThat(source).isNotNull();
        assertThat(source.getBasic64()).isNotNull();
        assertThat(source.getCipherString()).isNotNull();
        assertThat(source.getBasic64()).isEqualTo(origin);
        assertThat(source.getCipherString()).isEqualTo(origin);
        assertThat(source.getSubBasic64()).isEqualTo(origin);
        assertThat(source.getSubCipherString()).isEqualTo(origin);
        assertThat(source.getItems().get(0).getName()).isEqualTo(origin);
        assertThat(source.getItem().getName()).isEqualTo(origin);
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
        SpsDataConverter.encrypt(cipherSets, charsetName, source);

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
        SpsDataConverter.decrypt(cipherSets, charsetName, source);

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

    @Test
    public void encryptAndDecryptWithDisableCipherSet() {
        SpsClientSettings.CipherSets disableCipherSets = new SpsClientSettings.CipherSets();
        cipherSets.setEnabled(false);

        String origin = "Allan Im";
        SampleObject source = new SampleObject(origin, origin, origin);
        source.setSubBasic64(origin);
        source.setSubCipherString(origin);

        // encrypt
        SpsDataConverter.encrypt(disableCipherSets, charsetName, source);

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

        // decrypt
        SpsDataConverter.decrypt(disableCipherSets, charsetName, source);

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
        SpsDataConverter.encrypt(cipherSets, charsetName, source);
    }

    @Test(expected = InvalidRequestException.class)
    public void decryptWithNoGetterObject() {
        // given
        NoGetterObject source = new NoGetterObject();

        // when
        SpsDataConverter.decrypt(cipherSets, charsetName, source);
    }

    @Test
    public void enableEncryptedFlg() {
        // given
        SampleObject source = new SampleObject();

        // when
        SpsDataConverter.enableEncryptedFlg(source, SampleObject.class);

        // then
        assertThat(source.getEncryptedFlg()).isEqualTo("1");
    }

    @Test(expected = InvalidRequestException.class)
    public void enableEncryptedFlgWithNoGetter() {
        // given
        NoGetterObject source = new NoGetterObject();

        // when
        SpsDataConverter.enableEncryptedFlg(source, NoGetterObject.class);
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

        @MultiByteString(isIterable = true)
        @CipherString(isIterable = true)
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
    }

    public class NoGetterObject {
        @MultiByteString
        private String some;

        @CipherString
        private String cipher;

        private String encryptedFlg;
    }
}