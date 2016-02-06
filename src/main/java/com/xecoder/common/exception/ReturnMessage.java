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

    protected String errorCode = "-1";		//錯誤代碼，0為無錯誤
    protected String message;		//錯誤信息
    protected HttpStatus status = HttpStatus.BAD_REQUEST;	//HTTP錯誤代碼

    public ReturnMessage(String message) {
        super();
        this.message = message;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }
}
