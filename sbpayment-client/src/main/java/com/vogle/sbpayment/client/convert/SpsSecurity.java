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

import org.apache.http.util.Asserts;

import java.nio.charset.Charset;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Security utils by Softbank payment rules.
 *
 * @author Allan Im
 **/
public class SpsSecurity {

    private static final char SPACE = ' ';
    private static final String ALGORITHM = "DESede/CBC/NoPadding";

    private SpsSecurity() {
    }

    /**
     * Encrypt the source.
     *
     * @param desKey      The DES cipherSets key
     * @param initKey     The DES initialization key
     * @param charsetName Character Set name
     * @param source      he source
     * @return the encrypted data
     */
    public static String encrypt(final String desKey, final String initKey,
                                 final Charset charsetName, final String source) {
        Asserts.notNull(charsetName, "charsetName");
        Asserts.notNull(source, "source");

        try {
            // アルゴリズム：3DES-CBC（Padding なし）
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, toSecretKey(desKey), toAlgorithmParameterSpec(initKey));

            // 自動Padding なしのため、最後の8バイトブロックに対し、補完文字列追加
            byte[] sourcePad8 = rightPad8(source.getBytes(charsetName));

            // 暗号化
            byte[] encryptedByte = cipher.doFinal(sourcePad8);

            // BASE64 エンコード
            return Base64.getEncoder().encodeToString(encryptedByte);

        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException
                | InvalidAlgorithmParameterException | BadPaddingException | IllegalBlockSizeException ex) {
            throw new SecurityException("3DES-CBC encrypt fail", ex);
        }
    }

    /**
     * Decrypt the source
     *
     * @param desKey      The DES cipherSets key
     * @param initKey     The DES initialization key
     * @param charsetName Character Set name
     * @param source      The source
     * @return The decrypted data
     */
    public static String decrypt(final String desKey, final String initKey,
                                 final Charset charsetName, final String source) {
        Asserts.notNull(charsetName, "charsetName");
        Asserts.notNull(source, "source");

        try {
            // アルゴリズム：3DES-CBC（Padding なし）
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, toSecretKey(desKey), toAlgorithmParameterSpec(initKey));

            // BASE64 デコーダ
            byte[] srcByte = Base64.getDecoder().decode(source.trim());

            // 復号処理
            byte[] decryptedByte = cipher.doFinal(srcByte);

            return new String(decryptedByte, charsetName).trim();

        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException
                | InvalidAlgorithmParameterException | BadPaddingException | IllegalBlockSizeException ex) {
            throw new SecurityException("3DES-CBC decrypt fail", ex);
        }
    }

    private static SecretKey toSecretKey(String key) {
        if (key == null || key.length() > 24) {
            throw new IllegalArgumentException("Invalid DES-Key");
        }
        byte[] kyeByte = key.getBytes();
        byte[] keyByte24 = new byte[24];
        System.arraycopy(kyeByte, 0, keyByte24, 0, kyeByte.length);
        return new SecretKeySpec(keyByte24, "DESede");
    }

    private static AlgorithmParameterSpec toAlgorithmParameterSpec(String initKey) {
        if (initKey == null || initKey.length() > 8) {
            throw new IllegalArgumentException("Invalid DES-InitKey");
        }
        byte[] kyeByte = initKey.getBytes();
        byte[] keyByte8 = new byte[8];
        System.arraycopy(kyeByte, 0, keyByte8, 0, kyeByte.length);
        return new IvParameterSpec(keyByte8);
    }

    private static byte[] rightPad8(final byte[] strByte) {
        int repeatSize = 8;
        int strLen = strByte.length;
        int pads = repeatSize - (strLen % repeatSize);

        if (pads == repeatSize) {
            return strByte;
        } else {
            byte[] strByte8 = new byte[strLen + pads];
            System.arraycopy(strByte, 0, strByte8, 0, strLen);
            for (int i = strLen; i < strLen + pads; i++) {
                strByte8[i] = SPACE;
            }
            return strByte8;
        }
    }

}
