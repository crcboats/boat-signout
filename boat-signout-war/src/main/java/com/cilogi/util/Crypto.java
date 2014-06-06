package com.cilogi.util;

import java.io.IOException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import org.apache.commons.codec.binary.Base64;

public class Crypto {

    private static final String ALGORITHYM = "PBEWithMD5AndDES";
    private static final char[] PASSWORD = "enfldsgbnlsngdlksdsgm".toCharArray();
    private static final byte[] SALT = {
        (byte) 0xde, (byte) 0x33, (byte) 0x10, (byte) 0x12,
        (byte) 0xde, (byte) 0x33, (byte) 0x10, (byte) 0x12,
    };


    public static String encrypt(String property)  {
        try {
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHYM);
            SecretKey key = keyFactory.generateSecret(new PBEKeySpec(PASSWORD));
            Cipher pbeCipher = Cipher.getInstance(ALGORITHYM);
            pbeCipher.init(Cipher.ENCRYPT_MODE, key, new PBEParameterSpec(SALT, 20));
            return base64Encode(pbeCipher.doFinal(property.getBytes("UTF-8")));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String base64Encode(byte[] bytes) {
        return Base64.encodeBase64String(bytes);
    }

    public static String decrypt(String property)  {
        try {
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHYM);
            SecretKey key = keyFactory.generateSecret(new PBEKeySpec(PASSWORD));
            Cipher pbeCipher = Cipher.getInstance(ALGORITHYM);
            pbeCipher.init(Cipher.DECRYPT_MODE, key, new PBEParameterSpec(SALT, 20));
            return new String(pbeCipher.doFinal(base64Decode(property)), "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static byte[] base64Decode(String property) throws IOException {
        return Base64.decodeBase64(property);
    }

    public static void main(String[] args) {
        System.out.println(Crypto.encrypt("test"));
        System.out.println(Crypto.decrypt("nGxum2uOdpU="));
    }

}
