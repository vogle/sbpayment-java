package com.vogle.sbpayment.client.convert;

import com.vogle.sbpayment.client.SpsSettings;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests by {@link SpsDataConverter}
 *
 * @author Allan Im
 */
public class SpsDataConverterTest {

    private SpsSettings.CipherSets cipherSets;
    private final String charsetName = "Shift_JIS";

    @Before
    public void init() {
        cipherSets = new SpsSettings.CipherSets();
        cipherSets.setEnabled(true);
        cipherSets.setDesKey("abcdefghyjklmn1234567890");
        cipherSets.setDesInitKey("12345678");
    }

    @Test
    public void encode() {
        String origin = "Allan Im";
        String encoded = "QWxsYW4gSW0=";
        SampleObject source = new SampleObject(origin, origin);
        source.setSubBasic64(origin);
        source.setSubCipherString(origin);
        SpsDataConverter.encode(charsetName, source);

        assertThat(source).isNotNull();
        assertThat(source.getBasic64()).isNotNull();
        assertThat(source.getCipherString()).isNotNull();
        assertThat(source.getBasic64()).isEqualTo(encoded);
        assertThat(source.getCipherString()).isEqualTo(encoded);
        assertThat(source.getSubBasic64()).isEqualTo(encoded);
        assertThat(source.getSubCipherString()).isEqualTo(encoded);
    }

    @Test
    public void encodeWithoutCipherField() {
        String origin = "Allan Im";
        String encoded = "QWxsYW4gSW0=";
        SampleObject source = new SampleObject(origin, origin);
        source.setSubBasic64(origin);
        source.setSubCipherString(origin);
        SpsDataConverter.encodeWithoutCipherField(charsetName, source);

        assertThat(source).isNotNull();
        assertThat(source.getBasic64()).isNotNull();
        assertThat(source.getCipherString()).isNotNull();
        assertThat(source.getBasic64()).isEqualTo(encoded);
        assertThat(source.getCipherString()).isEqualTo(origin);
        assertThat(source.getSubBasic64()).isEqualTo(encoded);
        assertThat(source.getSubCipherString()).isEqualTo(origin);
    }

    @Test
    public void encryptAndDecrypt() {
        String origin = "Allan Im";
        String encrypted = "P8Rw2Jh3J6A=";
        SampleObject source = new SampleObject(origin, origin);
        source.setSubBasic64(origin);
        source.setSubCipherString(origin);

        // encrypt
        SpsDataConverter.encrypt(cipherSets, charsetName, source);

        assertThat(source).isNotNull();
        assertThat(source.getBasic64()).isNotNull();
        assertThat(source.getCipherString()).isNotNull();
        assertThat(source.getBasic64()).isEqualTo(origin);
        assertThat(source.getCipherString()).isEqualTo(encrypted);
        assertThat(source.getSubBasic64()).isEqualTo(origin);
        assertThat(source.getSubCipherString()).isEqualTo(encrypted);

        // decrypt
        SpsDataConverter.decrypt(cipherSets, charsetName, source);

        assertThat(source).isNotNull();
        assertThat(source.getBasic64()).isNotNull();
        assertThat(source.getCipherString()).isNotNull();
        assertThat(source.getBasic64()).isEqualTo(origin);
        assertThat(source.getCipherString()).isEqualTo(origin);
        assertThat(source.getSubBasic64()).isEqualTo(origin);
        assertThat(source.getSubCipherString()).isEqualTo(origin);
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static class SampleObject extends SuperObject {

        @MultiByteString
        private String subBasic64;

        @MultiByteString
        @CipherString
        private String subCipherString;

        public SampleObject(String basic64, String subCipherString) {
            super(basic64, subCipherString);
        }
    }

    @Data
    public static class SuperObject {

        @MultiByteString
        private String basic64;

        @MultiByteString
        @CipherString
        private String cipherString;

        public SuperObject(String basic64, String cipherString) {
            this.basic64 = basic64;
            this.cipherString = cipherString;
        }
    }
}