package com.xecoder.controller.core;

import org.springframework.http.HttpStatus;

/**
 * Created by  moxz
 * User: 224911261@qq.com
 * 2016/1/11-13:39
 * Feeling.com.xecoder.interceptor
 */
public class ExcepFactor {

    public static final ExcepFactor E_DEFAULT = new ExcepFactor("error.system", HttpStatus.INTERNAL_SERVER_ERROR);

    private String name;
    private HttpStatus httpStatus;

    public ExcepFactor(String name, HttpStatus httpStatus) {
        this.name = name;
        this.httpStatus = httpStatus;
    }

    public String getName() {
        return name;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

}
