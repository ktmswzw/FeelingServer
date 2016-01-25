package com.xecoder.common.util;

import java.security.SecureRandom;

/**
 * Created by  moxz
 * User: 224911261@qq.com
 * 2016/1/11-16:07
 * Feeling.com.xecoder.common
 */
public class RadomUtils {

    private static int DEFAULT_KEY_LENGTH = 16;
    private static SecureRandom random = null;

    /**
     * 获取随机字符串
     *
     * @param keyLength 字符串长度
     * @return 随机字符串
     */
    public static String getRadomStr(int keyLength) {
        if (random == null) {
            random = new SecureRandom();
        }
        byte[] buffer = new byte[keyLength];
        random.nextBytes(buffer);
        return buffer.toString();
    }

    public static String getRadomStr() {
        return getRadomStr(DEFAULT_KEY_LENGTH);
    }
}
