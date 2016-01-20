package com.xecoder.model.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by  moxz
 * User: 224911261@qq.com
 * 2016/1/11-10:06
 * Feeling.com.xecoder.model
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Value {
    private Long id;
    private String quote;

    public Value() {
    }

    public Long getId() {
        return this.id;
    }

    public String getQuote() {
        return this.quote;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    @Override
    public String toString() {
        return "Value{" +
                "id=" + id +
                ", quote='" + quote + '\'' +
                '}';
    }
}
