package com.xecoder.model.business;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xecoder.common.util.Digests;
import com.xecoder.common.util.Encodes;
import com.xecoder.common.util.RadomUtils;
import com.xecoder.model.embedded.DeviceEnum;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
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

        this.token = getJWT(user.getNickname());
        this.timestamp = new Date();
        this.user = user;
        this.device = device;
    }


    private String getJWT(String username)
    {

        //JWT http://blog.leapoahead.com/2015/09/06/understanding-jwt/
        String temp =Encodes.encodeHex(Digests.sha1((RadomUtils.getRadomByte().toString()+"_"+System.currentTimeMillis()).getBytes()));
        return Jwts.builder().setSubject(username)
                .claim("token", temp).setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, "FEELING_ME007").compact();
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
