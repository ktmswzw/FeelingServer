package com.xecoder.common.util;

import java.io.Serializable;

/**
 * Created by  moxz
 * User: 224911261@qq.com
 * 2016/1/25-11:10
 * Feeling.com.xecoder.common.util
 */
public class HashPassword implements Serializable {
    public String salt;
    public String password;

    private static final int INTERATIONS = 1024;
    private static final int SALT_SIZE = 8;

    public static HashPassword encryptPassword(String plainPassword,byte[] salt) {
        HashPassword result = new HashPassword();
        result.salt = Encodes.encodeHex(salt);

        byte[] hashPassword = Digests.sha1(plainPassword.getBytes(), salt, INTERATIONS);
        result.password = Encodes.encodeHex(hashPassword);
        return result;
    }

    /**
     *
     * 验证密码
     * @param plainPassword 明文密码
     * @param password 密文密码
     * @param salt 盐值
     * @return
     */
    public static boolean validatePassword(String plainPassword, String password, String salt) {
        byte[] hashPassword = Digests.sha1(plainPassword.getBytes(), Encodes.decodeHex(salt), INTERATIONS);
        String oldPassword = Encodes.encodeHex(hashPassword);
        return password.equals(oldPassword);
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}