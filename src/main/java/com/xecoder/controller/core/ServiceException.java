package com.xecoder.controller.core;

/**
 * Created by  moxz
 * User: 224911261@qq.com
 * 2016/1/11-13:38
 * Feeling.com.xecoder.interceptor
 */
public class ServiceException extends RuntimeException {

    private ExcepFactor factor;

    public ServiceException(ExcepFactor factor) {
        this.factor = factor;
    }

    public ServiceException(ExcepFactor factor, String message) {
        super(message);
        this.factor = factor;
    }

    public ServiceException(ExcepFactor factor, String message, Throwable cause) {
        super(message, cause);
        this.factor = factor;
    }

    public ExcepFactor getFactor() {
        return factor;
    }

    public void setFactor(ExcepFactor factor) {
        this.factor = factor;
    }
}
