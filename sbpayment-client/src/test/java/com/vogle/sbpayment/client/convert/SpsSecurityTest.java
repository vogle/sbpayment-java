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

        // when
        String decrypted = SpsSecurity.encrypt(cipherSets, charsetName, source);
        String encrypted = SpsSecurity.decrypt(cipherSets, charsetName, decrypted);

        // then
        assertThat(decrypted).isNotEmpty();
        assertThat(encrypted).isNotEmpty();
        assertThat(encrypted).isEqualTo(source);
    }
}
