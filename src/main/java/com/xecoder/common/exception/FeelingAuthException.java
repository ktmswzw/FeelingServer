package com.xecoder.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by  moxz
 * User: 224911261@qq.com
 * 2016/2/1-10:33
 * Feeling.com.xecoder.common.exception
 */

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class FeelingAuthException extends RuntimeException {
    private static final long serialVersionUID = -7467053929942663405L;

    public FeelingAuthException(){super();}

    public FeelingAuthException(String msg){
        super(msg);
    }

    public FeelingAuthException(String msg,Throwable throwable)
    {
        super(msg,throwable);
    }
}
