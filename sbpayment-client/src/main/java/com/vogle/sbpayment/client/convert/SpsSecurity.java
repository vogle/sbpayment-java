package com.vogle.sbpayment.client.convert;

import com.vogle.sbpayment.client.SpsSettings;

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
     * Encrypt the source using CipherSets
     *
     * @param cipherSets  Sps cipherSets settings
     * @param charsetName Character Set name
     * @param source      the source
     * @return the encrypted data
     */
    public static String encrypt(SpsSettings.CipherSets cipherSets, String charsetName, String source) {
        if (cipherSets == null || !cipherSets.isEnabled() || isEmpty(cipherSets.getDesKey())
                || isEmpty(cipherSets.getDesInitKey()) || isEmpty(source)) {
            return source;
        } else {
            return encrypt(cipherSets.getDesKey(), cipherSets.getDesInitKey(), charsetName, source);
        }

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
    public static String encrypt(String desKey, String initKey, String charsetName, final String source) {

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
     * Decrypt the source using CipherSets
     *
     * @param cipherSets  Sps cipherSets settings
     * @param charsetName Character Set name
     * @param source      the source
     * @return The decrypted data
     */
    public static String decrypt(SpsSettings.CipherSets cipherSets, String charsetName, String source) {

        if (cipherSets == null || !cipherSets.isEnabled() || isEmpty(cipherSets.getDesKey())
                || isEmpty(cipherSets.getDesInitKey()) || isEmpty(source)) {
            return source;
        } else {
            return decrypt(cipherSets.getDesKey(), cipherSets.getDesInitKey(), charsetName, source);
        }

    }

    /**
     * Decrypt the source
     *
     * @param desKey      The DES cipherSets key
     * @param initKey     The DES initialization key
     * @param charsetName Character Set name
     * @param source      he source
     * @return The decrypted data
     */
    public static String decrypt(String desKey, String initKey, String charsetName, final String source) {

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
        byte[] kyeByte = key.getBytes();
        byte[] keyByte24 = new byte[24];
        System.arraycopy(kyeByte, 0, keyByte24, 0, kyeByte.length);
        return new SecretKeySpec(keyByte24, "DESede");
    }

    private static AlgorithmParameterSpec toAlgorithmParameterSpec(String initKey) {
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

    private static boolean isEmpty(final CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

}
