package com.xecoder.model.business;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 用户文档
 * @author hongh
 *
 */
@Document(collection = "user")
public class User {
    public static int USER_OPEN = 1;
    public static int USER_DEL = 5;
    /**
     * id
     */
    @Id
    private String id;

    /**
     * 真实姓名
     */
    private String realname;
    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 性别
     */
    private Sex sex = Sex.MALE;

    /**
     * 手机号
     */
    @NotEmpty
    private String phone;

    /**
     * 个人签名
     */
    private String motto;

    @Field(value = "create_time")
    @JsonProperty(value = "create_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime = new Date();
    /**
     * 状态
     * 1开启 5删除
     */
    private int status = USER_OPEN;

    /**
     * 积分
     */
    private long integral = 0;

    private boolean single = true;

    private String birthday;

    private String region;


    @NotEmpty  @Transient
    private String password;

    @NotNull
    @Transient
    private DeviceEnum device;

    public User() {

    }

    public User(String telephone, String avatar, String motto, String realname, Sex sex) {
        this.phone = telephone;
        this.avatar = avatar;
        this.motto = motto;
        this.realname = realname;
        this.sex = sex;
    }

    public User(String realname, String phone) {
        this.realname = realname;
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMotto() {
        return motto;
    }

    public void setMotto(String motto) {
        this.motto = motto;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getIntegral() {
        return integral;
    }

    public void setIntegral(long integral) {
        this.integral = integral;
    }

    public boolean isSingle() {
        return single;
    }

    public void setSingle(boolean single) {
        this.single = single;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public DeviceEnum getDevice() {
        return device;
    }

    public void setDevice(DeviceEnum device) {
        this.device = device;
    }
}
