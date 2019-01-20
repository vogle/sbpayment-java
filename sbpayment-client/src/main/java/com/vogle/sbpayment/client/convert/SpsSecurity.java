package com.vogle.sbpayment.client.convert;

import org.apache.http.util.Asserts;

import java.io.UnsupportedEncodingException;
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
                                 final String charsetName, final String source) {
        Asserts.notBlank(charsetName, "charsetName");
        Asserts.notNull(source, "source");

        try {
            // アルゴリズム：3DES-CBC（Padding なし）
            Cipher cipher = Cipher.getInstance("DESede/CBC/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, toSecretKey(desKey), toAlgorithmParameterSpec(initKey));

            // 自動Padding なしのため、最後の8バイトブロックに対し、補完文字列追加
            byte[] sourcePad8 = rightPad8(source.getBytes(charsetName));

            // 暗号化
            byte[] encryptedByte = cipher.doFinal(sourcePad8);

            // BASE64 エンコード
            return Base64.getEncoder().encodeToString(encryptedByte);

        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException
                | InvalidAlgorithmParameterException | BadPaddingException | IllegalBlockSizeException
                | UnsupportedEncodingException ex) {
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
                                 final String charsetName, final String source) {
        Asserts.notBlank(charsetName, "charsetName");
        Asserts.notNull(source, "source");

        try {
            // アルゴリズム：3DES-CBC（Padding なし）
            Cipher cipher = Cipher.getInstance("DESede/CBC/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, toSecretKey(desKey), toAlgorithmParameterSpec(initKey));

            // BASE64 デコーダ
            byte[] srcByte = Base64.getDecoder().decode(source.trim());

            // 復号処理
            byte[] decryptedByte = cipher.doFinal(srcByte);

            return new String(decryptedByte, charsetName).trim();

        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException
                | InvalidAlgorithmParameterException | BadPaddingException | IllegalBlockSizeException
                | UnsupportedEncodingException ex) {
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
        final int repeatSize = 8;
        final int strLen = strByte.length;
        final int pads = repeatSize - (strLen % repeatSize);

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
