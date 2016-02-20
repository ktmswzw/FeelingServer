package com.xecoder.model.business;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xecoder.common.util.Digests;
import com.xecoder.common.util.Encodes;
import com.xecoder.common.util.JWTCode;
import com.xecoder.common.util.RandomUtils;
import com.xecoder.service.restful.BaseController;
import com.xecoder.model.core.BaseBean;
import com.xecoder.model.embedded.DeviceEnum;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by  moxz
 * User: 224911261@qq.com
 * 2016/1/11-16:03
 * Feeling.com.xecoder.model.business
 */

@Document(collection = "auth_token")
public class AuthToken  extends BaseBean implements Serializable {

    private static final long serialVersionUID = -3373371149561708376L;
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


    @JsonIgnore
    private String jwt;

    public AuthToken(User user, DeviceEnum device) {

        this.token = getRandomToken();
        this.jwt = getJWT(this.token,user.getId());
        this.timestamp = new Date();
        this.user = user;
        this.device = device;
    }

    private String getRandomToken(){
        return Encodes.encodeHex(Digests.sha1((RandomUtils.getRadomByte().toString()).getBytes()));
    }

    private String getJWT(String token,String userId)
    {

        //https://github.com/auth0/java-jwt
        //JWT http://blog.leapoahead.com/2015/09/06/understanding-jwt/
        HashMap<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put(BaseController.TOKEN_STR,token);
        claims.put("iss","http://www.imakehabits.com");
        return JWTCode.SIGNER.sign(claims);
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

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }
}
