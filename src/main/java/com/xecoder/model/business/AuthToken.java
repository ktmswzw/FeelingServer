package com.xecoder.model.business;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xecoder.common.util.AESEncrypter;
import com.xecoder.common.util.RadomUtils;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * Created by  moxz
 * User: 224911261@qq.com
 * 2016/1/11-16:03
 * Feeling.com.xecoder.model.business
 */

@Document(collection = "auth_token")
public class AuthToken {
    /**
     * 令牌有效期
     */
    public static long EXPIRED_TIME = 24 * 3600 * 1000;
    /**
     * 通过验证的设备
     */
    private DeviceEnum device;
    /**
     * 令牌
     */
    private String token;
    /**
     * 令牌获取的时间
     */
    private Date timestamp = new Date();
    /**
     * 令牌所属的用户
     */
    @JsonIgnore
    private User user;

    public AuthToken(User user, DeviceEnum device) {
        this.token = AESEncrypter.getInstance().encryptAsString(RadomUtils.getRadomStr(4) + "_" + System.currentTimeMillis());
        this.timestamp = new Date();
        this.user = user;
        this.device = device;
    }

    public AuthToken(){}

    public DeviceEnum getDevice() {
        return device;
    }

    public void setDevice(DeviceEnum device) {
        this.device = device;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isExpired() {
        return System.currentTimeMillis() - this.timestamp.getTime() > EXPIRED_TIME;
    }
}
