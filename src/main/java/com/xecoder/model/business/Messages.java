package com.xecoder.model.business;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.xecoder.common.util.DateTools;
import com.xecoder.model.embedded.MessagesPhoto;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by  moxz
 * User: 224911261@qq.com
 * 2016/1/26-22:58
 * Feeling.com.xecoder.model.business
 *
 * 消息留言
 */
@Document(collection = "messages")
public class Messages implements Serializable {
    private static final long serialVersionUID = 3937826603798306562L;

    public static int CLOSE = 1; //未拆封  默认
    public static int LOCKED = 2; //答案正确 锁定，待经纬度差额为100米，解锁
    public static int OPEN = 3;  // 已被拆开
    public static int DELETED = 9;  // 已删除

    @Id
    private String id;

    /**
     * 发送对象名称
     */
    @NotEmpty
    private String from;

    /**
     * 接受对象名称
     */
    private String to;

    /**
     * 内容
     */
    private String content;

    /**
     * 图片组
     */
    @NotEmpty
    private List<MessagesPhoto> photos;

    /**
     * 留声机
     */
    private String soundPath;

    /**
     * 视频
     */
    private String videoPath;

    /**
     * 经纬度坐标
     * http://docs.mongodb.org/manual/reference/command/geoNear
     * db.messages.ensureIndex({point:"2dsphere"});  2.6 old
     * db.messages.createIndex( { point : "2dsphere" } )  3.0 new
     * { type: "Point", coordinates: [ 40, 5 ] }  https://docs.mongodb.org/v2.6/reference/geojson/
     */
    @NotEmpty
    @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
    private GeoJsonPoint point;

    /**
     * 城市
     */
    @NotEmpty
    private String city;

    /**
     * 县区
     */
    @NotEmpty
    private String district;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 问题
     */
    private String question;

    /**
     * 答案
     */
    private String answer;

    /**
     * 限制时间
     */
    @Field(value = "limit_date")
    @JsonProperty(value = "limit_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date limitDate = DateTools.addDay(new Date(),7);

    /**
     * 阅后即焚
     */
    @Field(value = "burn_after_reading")
    private boolean burnAfterReading;

    private int state = CLOSE;

    /**
     * 发送人
     */
    private String fromId;
    /**
     * 查看人
     */
    private String toId;

    /**
     * 创建时间
     */
    @Field(value = "create_date")
    @JsonProperty(value = "create_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDate = new Date();

    /**
     * 更新时间
     */
    @Field(value = "update_date")
    @JsonProperty(value = "update_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateDate;


    /**
     * 临时距离，默认为空，查询结果时赋值
     */
    @Transient
    private double distance;

    public void setId(String id) {
        this.id = id;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<MessagesPhoto> getPhotos() {
        return photos;
    }

    public void setPhotos(List<MessagesPhoto> photos) {
        this.photos = photos;
    }

    public String getSoundPath() {
        return soundPath;
    }

    public void setSoundPath(String soundPath) {
        this.soundPath = soundPath;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public GeoJsonPoint getPoint() {
        return point;
    }

    public void setPoint(GeoJsonPoint point) {
        this.point = point;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getId() {
        return id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Date getLimitDate() {
        return limitDate;
    }

    public void setLimitDate(Date limitDate) {
        this.limitDate = limitDate;
    }

    public boolean isBurnAfterReading() {
        return burnAfterReading;
    }

    public void setBurnAfterReading(boolean burnAfterReading) {
        this.burnAfterReading = burnAfterReading;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getFromId() {
        return fromId;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }

    public String getToId() {
        return toId;
    }

    public void setToId(String toId) {
        this.toId = toId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
