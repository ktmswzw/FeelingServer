package com.xecoder.model.embedded;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by  moxz
 * User: 224911261@qq.com
 * 2016/1/26-23:06
 * Feeling.com.xecoder.model.business
 */

@Document(collection = "message_photo")
public class MessagesPhoto {

    @Id
    private String id;

    /**
     * 消息id
     */
    private String messageId;

    /**'
     * 名称
     */
    private String name;

    /**
     * 原路径
     */
    private String source;

    /**
     * 缩略图
     */
    private String thumbnails;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getThumbnails() {
        return thumbnails;
    }

    public void setThumbnails(String thumbnails) {
        this.thumbnails = thumbnails;
    }
}
