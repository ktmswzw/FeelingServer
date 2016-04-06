package com.xecoder.model.business;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * Created by  moxz
 * User: 224911261@qq.com
 * 2015/11/11-9:01
 * HabitServer.com.imakehabits.model
 * 好友关系
 */
@Document(collection = "friend")
public class Friend {

    @Id
    private String id;

    /**
     * 本人id
     */
    @Field(value = "key_user_id")
    @JsonProperty(value = "key_user_id")
    private String keyUserId;

    /**
     * 分组
     */
    @Field(value = "grouping")
    private String grouping;


    /**
     * 拉黑
     */
    private boolean blacklist = false;

    /**
     * 好友
     */
    private String user;

    /**
     * 备注
     */
    private String remark;


    @Field(value = "create_time")
    @JsonProperty(value = "create_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @Field(value = "update_time")
    @JsonProperty(value = "update_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    /**
     * 好友类型(未使用)
     */
    private int relationship;

    @Transient
    private boolean all = false;

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

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getRelationship() {
        return relationship;
    }

    public void setRelationship(int relationship) {
        this.relationship = relationship;
    }

    public String getGrouping() {
        return grouping;
    }

    public void setGrouping(String grouping) {
        this.grouping = grouping;
    }

    public boolean isBlacklist() {
        return blacklist;
    }

    public void setBlacklist(boolean blacklist) {
        this.blacklist = blacklist;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public boolean isAll() {
        return all;
    }

    public void setAll(boolean all) {
        this.all = all;
    }
}
