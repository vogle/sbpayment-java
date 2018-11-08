package com.vogle.sbpayment.client.convert;

import com.vogle.sbpayment.client.SpsClientSettings;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link SpsSecurity}
 *
 * @author Allan Im
 */
public class SpsSecurityTest {

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
    public void encryptAndDecrypt() {
        // given
        String source = "Allan Im";
        String encryptedSource = "P8Rw2Jh3J6A=";

        // when
        String encrypted = SpsSecurity.encrypt(cipherSets, charsetName, source);
        String decrypted = SpsSecurity.decrypt(cipherSets, charsetName, encrypted);

        // then
        assertThat(encrypted).isNotEmpty();
        assertThat(decrypted).isNotEmpty();
        assertThat(encrypted).isEqualTo(encryptedSource);
        assertThat(decrypted).isEqualTo(source);
    }

    @Test
    public void encryptAndDecryptWithDisableCipherSet() {
        // given
        String source = "Allan Im";
        SpsClientSettings.CipherSets disableCipherSets = new SpsClientSettings.CipherSets();

        // when
        String encrypted = SpsSecurity.encrypt(disableCipherSets, charsetName, source);
        String decrypted = SpsSecurity.decrypt(disableCipherSets, charsetName, encrypted);

        // then
        assertThat(encrypted).isNotEmpty();
        assertThat(decrypted).isNotEmpty();
        assertThat(encrypted).isEqualTo(source);
        assertThat(decrypted).isEqualTo(source);
    }

    @Test(expected = SecurityException.class)
    public void encryptWithWrongCharsetName() {
        // when
        SpsSecurity.encrypt(cipherSets, "Allan_Charset", "Allan");
    }

    @Test(expected = SecurityException.class)
    public void decryptWithWrongCharsetName() {
        // when
        String encrypted = SpsSecurity.encrypt(cipherSets, charsetName, "Allan");
        SpsSecurity.decrypt(cipherSets, "Allan_Charset", encrypted);
    }

    @Test(expected = IllegalArgumentException.class)
    public void encryptWithWrongDesKey() {
        // given
        String source = "Allan";
        SpsClientSettings.CipherSets wrongCipherSets = new SpsClientSettings.CipherSets();
        wrongCipherSets.setEnabled(true);
        wrongCipherSets.setDesKey("1234567890123456789012345");
        wrongCipherSets.setDesInitKey("12345678");

        // when
        SpsSecurity.encrypt(wrongCipherSets, charsetName, source);
    }

    @Test(expected = IllegalArgumentException.class)
    public void encryptWithWrongDesInitKey() {
        // given
        String source = "Allan";
        SpsClientSettings.CipherSets wrongCipherSets = new SpsClientSettings.CipherSets();
        wrongCipherSets.setEnabled(true);
        wrongCipherSets.setDesKey("123456789012345678901234");
        wrongCipherSets.setDesInitKey("123456789");

        // when
        SpsSecurity.encrypt(wrongCipherSets, charsetName, source);
    }
}
