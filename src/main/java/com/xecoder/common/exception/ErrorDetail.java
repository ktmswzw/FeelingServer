package com.xecoder.common.exception;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: zhangxin
 * Date:   15-9-17
 */
public class ErrorDetail  implements Serializable {

    private static final long serialVersionUID = 2803327363412578379L;
    protected String errorCode = "-1";		//錯誤代碼，0為無錯誤
    private String title;
    private int status;
    private String message;
    private long timestamp;
    private String developerMessage;
    private Map<String, List<ValidationError>> errors = new HashMap<>();

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getDeveloperMessage() {
        return developerMessage;
    }

    public void setDeveloperMessage(String developerMessage) {
        this.developerMessage = developerMessage;
    }

    public Map<String, List<ValidationError>> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, List<ValidationError>> errors) {
        this.errors = errors;
    }
}
