package com.xecoder.common.exception;

/**
 * Created by  moxz
 * User: 224911261@qq.com
 * 2016/2/6-21:08
 * Feeling.com.xecoder.common.exception
 */
public class HttpServiceException extends RuntimeException {
    private static final long serialVersionUID = 3976892301056166141L;
    private ReturnMessage returnMessage;

    public HttpServiceException(String msg){
        super(msg);
        this.returnMessage = new ReturnMessage(msg);
    }
    public ReturnMessage getReturnMessage() {
        return returnMessage;
    }

}
