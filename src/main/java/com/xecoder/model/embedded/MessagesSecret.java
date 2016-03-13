package com.xecoder.model.embedded;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.xecoder.common.util.DateTools;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * Created by  moxz
 * User: 224911261@qq.com
 * 2016/3/13-14:15
 * Feeling.com.xecoder.model.embedded
 */

@Document(collection = "messages_secret")
public class MessagesSecret {
    @Id
    private String id;

    /**
     * msg id
     */
    private String msgId;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
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
}
