package com.xecoder.model.business;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xecoder.model.core.BaseBean;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by  moxz
 * User: 224911261@qq.com
 * 2016/2/4-15:57
 * Feeling.com.xecoder.model.business
 */

@Document(collection = "book")
public class Book  extends BaseBean implements Serializable {

    private static final long serialVersionUID = -3516779633175220283L;

    public Book(String id, String title, String content)
    {
        this.id = id;
        this.title = title;
        this.content=content;
    }

    @Id
    private String id ;

    private String title;
    private String content;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date publicDate =  new Date();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getPublicDate() {
        return publicDate;
    }

    public void setPublicDate(Date publicDate) {
        this.publicDate = publicDate;
    }
}
