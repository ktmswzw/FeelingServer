package com.xecoder.common.util;

import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by  moxz
 * User: 224911261@qq.com
 * 2016/1/11-16:04
 * Feeling.com.xecoder.model.core
 */
public class AESEncrypter {

    private static String aesKeyStr = "NGQxNmUwMjM4M2Y0MTI2MTM3NDI0Y2MxMjA1N2IyNDM=";

    private SecretKey aesKey;

    private AESEncrypter() {
        aesKey = loadAesKey();
    }

    private AESEncrypter(String aes) {
        aesKey = loadAesKey(aes);
    }

    private static AESEncrypter INSTANCE;

    private static Map<String, AESEncrypter> INSTANCES = new HashMap<>();

    public static AESEncrypter getInstance() {
        if (INSTANCE == null) {
            synchronized (aesKeyStr) {
                if (INSTANCE == null) {
                    INSTANCE = new AESEncrypter();
                }
            }
        }
        return INSTANCE;
    }

    public static AESEncrypter getInstance(String aes) {
        if (INSTANCES.get(aes) == null) {
            synchronized (aesKeyStr) {
                if (INSTANCES.get(aes) == null) {
                    INSTANCES.put(aes, new AESEncrypter(aes));
                }
            }
        }
        return INSTANCES.get(aes);
    }

    public byte[] encrypt(String msg) {
        try {
            Cipher ecipher = Cipher.getInstance("AES");
            ecipher.init(Cipher.ENCRYPT_MODE, aesKey);
            return ecipher.doFinal(msg.getBytes());
        } catch (Exception e) {
            String errMsg = "encrypt error, data:" + msg;
            return null;
        }
    }

    public byte[] decrypt(String msg) {
        try {
            Cipher dcipher = Cipher.getInstance("AES");
            dcipher.init(Cipher.DECRYPT_MODE, aesKey);
            return dcipher.doFinal(Encrypter.toBytes(msg));
        } catch (Exception e) {
            String errMsg = "decrypt error, data:" + msg;
            return null;
        }
    }

    public String decryptAsString(String msg) {
        return new String(this.decrypt(msg));
    }

    public String encryptAsString(String msg) {
        return Encrypter.toHexString(this.encrypt(msg));
    }

    private static SecretKey loadAesKey() {
        String buffer = new String(Base64.decodeBase64(aesKeyStr));
        byte[] keyStr = buffer.getBytes();
        SecretKeySpec aesKey = new SecretKeySpec(keyStr, "AES");
        return aesKey;
    }

    private static SecretKey loadAesKey(String aesKeyStr) {
        String buffer = new String(Base64.decodeBase64(aesKeyStr));
        byte[] keyStr = buffer.getBytes();
        SecretKeySpec aesKey = new SecretKeySpec(keyStr, "AES");
        return aesKey;
    }
}
