package com.xecoder.model.embedded;

/**
 * Created by  moxz
 * User: 224911261@qq.com
 * 2016/1/26-23:06
 * Feeling.com.xecoder.model.business
 */

public class MessagesPhoto {
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
