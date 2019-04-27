package utils;

import org.junit.Assert;
import org.junit.Test;
import org.hamcrest.core.StringStartsWith;

public class AESUtilTest {
    @Test
    public void testGenerateKey() {
        String key = "test";
        String realKey = AESUtil.generateKey(key);
        Assert.assertEquals(realKey, "test56789abcdefg");
        Assert.assertThat(realKey, StringStartsWith.startsWith(key));

        key = "testtesttesttesttest";
        realKey = AESUtil.generateKey(key);
        Assert.assertEquals(realKey, key.substring(0, 16));
    }

    @Test
    public void testEncryptAndDecrypt() throws Exception{
        String key = "test";
        String message = "test message";

        key = AESUtil.generateKey(key);
        String encryptedMessage = AESUtil.encrypt(key, message);
        String decryptedMessage = AESUtil.decrypt(key, encryptedMessage);

        Assert.assertEquals(decryptedMessage, message);

    }
}
