package com.vogle.sbpayment.client.convert;

import com.vogle.sbpayment.client.SpsSettings;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test by @{@link SpsSecurity}
 *
 * @author Allan Im
 */
public class SpsSecurityTest {

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
    public void encryptAndDecrypt() {
        String source = "Allan Im";
        String decrypted = SpsSecurity.encrypt(cipherSets, charsetName, source);
        String encrypted = SpsSecurity.decrypt(cipherSets, charsetName, decrypted);

        assertThat(decrypted).isNotEmpty();
        assertThat(encrypted).isNotEmpty();
        assertThat(encrypted).isEqualTo(source);
    }
}
