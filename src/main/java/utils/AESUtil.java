package utils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AESUtil {
    private static final String ALGORITHM_NAME = "AES";

    public static String generateKey(String key) {
        if (key.length() > 16) {
            key = key.substring(0, 16);
        }

        String realKey = "123456789abcdefg";
        StringBuilder realKeyBuilder = new StringBuilder(realKey);
        realKeyBuilder.replace(0, key.length(), key);
        realKey = realKeyBuilder.toString();
        return realKey;
    }

    public static String encrypt(String key, String encryptStr) throws Exception {
        if (key.length() != 16) {
            throw new Exception("key length is not 16");
        }
        byte [] encryptBytes = encryptStr.getBytes("UTF8");
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes("UTF8"), ALGORITHM_NAME);
        Cipher cipher = Cipher.getInstance(ALGORITHM_NAME);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        byte [] encryptedBytes = cipher.doFinal(encryptBytes);
        String encryptedStr = new BASE64Encoder().encode(encryptedBytes);
        return encryptedStr;
    }

    public static String decrypt(String key, String encryptedStr) throws Exception {
        if (key.length() != 16) {
            throw new Exception("key length is not 16");
        }
        byte [] encryptedBytes = new BASE64Decoder().decodeBuffer(encryptedStr);
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes("UTF8"), ALGORITHM_NAME);
        Cipher cipher = Cipher.getInstance(ALGORITHM_NAME);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        byte [] originalBytes = cipher.doFinal(encryptedBytes);
        String originalStr = new String(originalBytes, "UTF8");
        return originalStr;
    }
}
