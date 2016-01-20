package com.xecoder.common.util;

import org.apache.commons.codec.digest.DigestUtils;


public class CryptoUtils {

    private static int DEFAULT_SALT_SPLIT_LENGTH = 5;

    /**
     * 加盐的密码加密方式
     * @param password 密码
     * @param salt 盐字符串，为随机字符串
     * @return 密码加密后的字符串
     */
    public static String cryptoPassword(String password, String salt) {
        int splitLength = salt.length() > DEFAULT_SALT_SPLIT_LENGTH ? DEFAULT_SALT_SPLIT_LENGTH : salt.length();
        String prefixSalt = salt.substring(0, splitLength);
        String suffixSalt = salt.substring(splitLength);
        StringBuilder buffer = new StringBuilder().append(prefixSalt).append(password).append(suffixSalt);
        return DigestUtils.sha256Hex(buffer.toString().getBytes());
    }

}
