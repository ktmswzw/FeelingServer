package com.xecoder.model.business;

import com.xecoder.model.core.BaseBean;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * Created by  moxz
 * User: 224911261@qq.com
 * 2016/1/22-14:01
 * Feeling.com.xecoder.common.util
 */
@SuppressWarnings("unchecked")
@Document(collection = "log_record")
public class LogRecord  extends BaseBean implements Serializable {

    private static final long serialVersionUID = 4135902734480106473L;
    private Date time = new Date();
    /**
     * 操作者
     */
    private String uid;

    /**
     * 行为
     */
    private String action;

    /**
     * 操作是否成功
     */
    @Field(value = "is_success")
    private Boolean isSuccess;

    @Field(value = "response_code")
    private String responseCode;

    private String ip;

    private String response;

    @Transient
    private Map<String, String[]> paramsMap;

    private String params;


    @Field(value = "client_version")
    private String clientVersion;

    public LogRecord(Date time, String uid, String action, String ip, String response, String clientVersion, Map paramsMap) {
        this.time = time;
        this.uid = uid;
        this.action = action;
        this.ip = ip;
        this.response = response;
        this.clientVersion = clientVersion;
        this.paramsMap = paramsMap;
        buildParams();
    }

    public Date getTime() {
        return time;
    }

    public String getUid() {
        return uid;
    }

    public String getAction() {
        return action;
    }

    public Boolean getIsSuccess() {
        return isSuccess;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public String getIp() {
        return ip;
    }

    public String getResponse() {
        return response;
    }

    public String getClientVersion() {
        return clientVersion;
    }

    public String getParams() {
        return params;
    }

    private void buildParams() {
        if (this.params == null) {
            if (this.paramsMap != null) {
                StringBuilder paramBuf = new StringBuilder();
                for (Map.Entry<String, String[]> e : this.paramsMap.entrySet()) {
                    String key = e.getKey();
                    String[] values = e.getValue();
                    for(String value:values) {
                        paramBuf.append(key).append("=").append(value);
                        paramBuf.append("&");
                    }
                }
                if (paramBuf.length() > 0 && paramBuf.charAt(paramBuf.length() - 1) == '&') {
                    paramBuf.deleteCharAt(paramBuf.length() - 1);
                }
                this.params = paramBuf.toString();
            } else {
                this.params = "";
            }
        }

    }
}
