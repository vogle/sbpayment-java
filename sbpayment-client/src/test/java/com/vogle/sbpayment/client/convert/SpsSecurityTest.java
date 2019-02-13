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

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

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
