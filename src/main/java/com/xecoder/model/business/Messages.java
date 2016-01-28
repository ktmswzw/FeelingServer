package com.xecoder.model.business;

import com.xecoder.model.embedded.MessagesPhoto;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

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
    public static int OPEN = 9;  // 已被拆开

    @Id
    private String id;

    /**
     * 发送对象
     */
    @NotEmpty
    private String from;

    /**
     * 接受对象
     */
    @NotEmpty
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

    @NotEmpty
    @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
    private GeoJsonPoint coordinate;

    /**
     * 经纬度坐标
     * http://docs.mongodb.org/manual/reference/command/geoNear
     * db.geography.ensureIndex({point:"2dsphere"});
     * db.collection.createIndex( { <location field> : "2dsphere" } )
     * { type: "Point", coordinates: [ 40, 5 ] }  https://docs.mongodb.org/v2.6/reference/geojson/
     */

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

    //TODO
    //发送者 条件

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
    private Date limitDate;

    /**
     * 阅后即焚
     */
    @Field(value = "burn_after_reading")
    private boolean burnAfterReading;

    private int state = CLOSE;

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

    public Point getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(GeoJsonPoint coordinate) {
        this.coordinate = coordinate;
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
}
