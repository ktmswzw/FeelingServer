package com.xecoder.common.exception;

import org.springframework.http.HttpStatus;

import java.io.Serializable;

/**
 * Created by  moxz
 * User: 224911261@qq.com
 * 2016/2/6-21:06
 * Feeling.com.xecoder.common.exception
 */
public class ReturnMessage implements Serializable {


    private static final long serialVersionUID = -7942648282014784932L;

    protected String errorCode;		//錯誤代碼，0為無錯誤
    protected String message;		//錯誤信息
    protected HttpStatus status;	//HTTP錯誤代碼

    public ReturnMessage(String errorCode){
        super();
        this.errorCode = errorCode;
        this.message = "";
        this.status = HttpStatus.BAD_REQUEST;
    }

    public ReturnMessage(Throwable ex){
        super();
        this.errorCode = ReturnMessageEnum.Status.Failed.getStatus();
        this.message = ex.getMessage();
        this.status = HttpStatus.BAD_REQUEST;
    }

    public ReturnMessage(String errorCode, String message, HttpStatus status) {
        super();
        this.errorCode = errorCode;
        this.message = message;
        this.status = HttpStatus.BAD_REQUEST;
    }

    public String getErrorCode() {
        return this.errorCode;
    }

    public String getMessage() {
        return this.message;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
