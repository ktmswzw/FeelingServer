package com.xecoder.common.util;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;

/**
 * Created by  moxz
 * User: 224911261@qq.com
 * 2016/1/11-16:07
 * Feeling.com.xecoder.common
 */
public class RandomUtils {

    private static int DEFAULT_KEY_LENGTH = 32;
    private static SecureRandom random = null;

    /**
     * 获取随机字符串
     *
     * @param keyLength 字符串长度
     * @return 随机字符串
     */
    public static String getRadomStr(int keyLength) {
        random = RadomUtils(random);
        byte[] buffer = new byte[keyLength];
        random.nextBytes(buffer);
        String str = null;
        try {
            str = new String(buffer,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }

    private static SecureRandom RadomUtils(SecureRandom random){
        if (random == null) {
            try {
                random = SecureRandom.getInstance("SHA1PRNG", "SUN");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (NoSuchProviderException e) {
                e.printStackTrace();
            }
        }
        return random;
    }

    public static byte[] getRadomByte(int keyLength) {
        random = RadomUtils(random);
        byte[] buffer = new byte[keyLength];
        random.nextBytes(buffer);
        return buffer;
    }


    public static  byte[] getRadomByte() {
        return getRadomByte(DEFAULT_KEY_LENGTH);
    }

    public static String getRadomStr() {
        return getRadomStr(DEFAULT_KEY_LENGTH);
    }
}
