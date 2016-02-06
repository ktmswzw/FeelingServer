package com.xecoder.common.exception;

import org.springframework.http.HttpStatus;

/**
 * Created by  moxz
 * User: 224911261@qq.com
 * 2016/2/6-21:08
 * Feeling.com.xecoder.common.exception
 */
public class HttpServiceException extends RuntimeException {
    private static final long serialVersionUID = 3976892301056166141L;
    private ReturnMessage returnMessage;
    private HttpStatus status;

    public HttpServiceException(ReturnMessage returnMessage){
        super(returnMessage.getMessage());
        this.returnMessage = returnMessage;
        this.status = returnMessage.getStatus();
    }

    public HttpStatus getStatus() {
        return returnMessage.getStatus();
    }

    public String getErrorCode() {
        return returnMessage.getErrorCode();
    }

    public ReturnMessage getReturnMessage() {
        return returnMessage;
    }

}
