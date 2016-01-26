package com.xecoder.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Author: zhangxin
 * Date:   15-9-17
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class FeelingException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public FeelingException() {
        super();
    }

    public FeelingException(String message) {
        super(message);
    }

    public FeelingException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
