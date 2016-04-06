package com.xecoder.model.business;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * Created by luxp on 2016/1/25.
 * 好友申请
 */
@Document(collection = "friend_requests")
public class FriendRequests {
    public static int APPLICATION_OPEN = 1;
    public static int APPLICATION_DEL = 5;

    @Id
    private String id;

    /**
     *申请方id
     */
    @Field(value = "key_user_id")
    @JsonProperty(value = "key_user_id")
    private String keyUserId;

    /**
     *
     */
    @Field(value = "user_id")
    @JsonProperty(value = "user_id")
    private String userId;

    /**
     * 验证信息
     */
    private String verification;

    /**
     * 邀请时间
     */
    @Field(value = "create_time")
    @JsonProperty(value = "create_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 状态
     * 1 开启 5 关闭
     */
    private int status = APPLICATION_OPEN;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKeyUserId() {
        return keyUserId;
    }

    public void setKeyUserId(String keyUserId) {
        this.keyUserId = keyUserId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getVerification() {
        return verification;
    }

    public void setVerification(String verification) {
        this.verification = verification;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
