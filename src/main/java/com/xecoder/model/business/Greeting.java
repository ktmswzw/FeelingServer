package com.xecoder.model.business;

import com.xecoder.model.core.BaseBean;

/**
 * Created by  moxz
 * User: 224911261@qq.com
 * 2016/1/8-13:54
 * Demo2.com.example.bean
 */
public class Greeting extends BaseBean {
    private static final long serialVersionUID = 8710453917584610929L;
     long id;
     String content;


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
