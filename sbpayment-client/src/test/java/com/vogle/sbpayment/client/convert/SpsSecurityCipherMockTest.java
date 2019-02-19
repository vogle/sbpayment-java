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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

import static org.powermock.api.mockito.PowerMockito.doThrow;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

/**
 * Tests for {@link SpsSecurity} with {@link Cipher} Mock
 *
 * @author Allan Im
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(Cipher.class)
public class SpsSecurityCipherMockTest {

    private final Charset charsetName = Charset.forName("Shift_JIS");
    private final String desKey = "abcdefghyjklmn1234567890";
    private final String desInitKey = "12345678";

    @Before
    public void setup() throws NoSuchPaddingException, NoSuchAlgorithmException {
        mockStatic(Cipher.class);
        doThrow(new NoSuchAlgorithmException()).when(Cipher.class);
        Cipher.getInstance("DESede/CBC/NoPadding");
    }

    @Test(expected = SecurityException.class)
    public void encryptWithSecurityException() {
        // given
        String source = "Allan";

        // when
        SpsSecurity.encrypt(desKey, desInitKey, charsetName, source);
    }

    @Test(expected = SecurityException.class)
    public void decryptWithSecurityException() {
        // given
        String source = "Allan";

        // when
        SpsSecurity.decrypt(desKey, desInitKey, charsetName, source);
    }
}
