package com.vogle.sbpayment.client.convert;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

/**
 * Tests for {@link SpsSecurity}
 *
 * @author Allan Im
 */
public class SpsSecurityTest {

    private final String charsetName = "Shift_JIS";
    private final String desKey = "abcdefghyjklmn1234567890";
    private final String desInitKey = "12345678";

    @Test
    public void encryptAndDecrypt() {
        // given
        String source = "Allan Im";
        String encryptedSource = "P8Rw2Jh3J6A=";

        // when
        String encrypted = SpsSecurity.encrypt(desKey, desInitKey, charsetName, source);
        String decrypted = SpsSecurity.decrypt(desKey, desInitKey, charsetName, encrypted);

        // then
        assertThat(encrypted).isNotEmpty();
        assertThat(decrypted).isNotEmpty();
        assertThat(encrypted).isEqualTo(encryptedSource);
        assertThat(decrypted).isEqualTo(source);
    }

    @Test(expected = SecurityException.class)
    public void encryptWithWrongCharsetName() {
        // when
        SpsSecurity.encrypt(desKey, desInitKey, "Allan_Charset", "Allan");
    }

    @Test(expected = SecurityException.class)
    public void decryptWithWrongCharsetName() {
        // when
        String encrypted = SpsSecurity.encrypt(desKey, desInitKey, charsetName, "Allan");
        SpsSecurity.decrypt(desKey, desInitKey, "Allan_Charset", encrypted);
    }

    @Test(expected = IllegalArgumentException.class)
    public void encryptWithWrongDesKey() {
        // given
        String source = "Allan";
        String wrongDesKey = "1234567890123456789012345";

        // when
        SpsSecurity.encrypt(wrongDesKey, desInitKey, charsetName, source);
    }

    @Test(expected = IllegalArgumentException.class)
    public void encryptWithWrongDesInitKey() {
        // given
        String source = "Allan";
        String wrongDesInitKey = "123456789";

        // when
        SpsSecurity.encrypt(desKey, wrongDesInitKey, charsetName, source);
    }
}
