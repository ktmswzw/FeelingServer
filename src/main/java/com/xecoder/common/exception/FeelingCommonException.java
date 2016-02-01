package com.xecoder.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class FeelingCommonException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public FeelingCommonException() {
        super();
    }

    public FeelingCommonException(String message) {
        super(message);
    }

    public FeelingCommonException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
