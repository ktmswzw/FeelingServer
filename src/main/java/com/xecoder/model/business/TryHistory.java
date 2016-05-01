package com.xecoder.model.business;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.xecoder.model.core.BaseBean;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by vincent on 16/5/1.
 */

@Document(collection = "try_history")
public class TryHistory extends BaseBean implements Serializable{

    private static final long serialVersionUID = -8720349963268175129L;

    private String id;

    //尝试开启的信件id
    @JsonProperty(value = "message_id")
    private String messageId;


    //尝试答案
    @JsonProperty(value = "try_answer")
    private String tryAnswer;

    //尝试者ID
    @JsonProperty(value = "try_id")
    private String tryId;


    //更新时间
    @Field(value = "try_date")
    @JsonProperty(value = "update_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date tryDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getTryAnswer() {
        return tryAnswer;
    }

    public void setTryAnswer(String tryAnswer) {
        this.tryAnswer = tryAnswer;
    }

    public String getTryId() {
        return tryId;
    }

    public void setTryId(String tryId) {
        this.tryId = tryId;
    }

    public Date getTryDate() {
        return tryDate;
    }

    public void setTryDate(Date tryDate) {
        this.tryDate = tryDate;
    }
}
