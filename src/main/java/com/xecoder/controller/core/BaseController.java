package com.xecoder.controller.core;

import com.xecoder.model.core.BaseBean;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by  moxz
 * User: 224911261@qq.com
 * 2016/1/11-10:26
 * Feeling.com.xecoder.controller
 */
public class BaseController {

    public static String TOKEN_STR = "TOKEN";
    private static String SEPARATOR = "-";
    public static String VERSION_STR = "CLIENT-VERSION";
    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected HttpSession session;
    private String token;
    private String userId;
    public BaseBean baseBean;

    /**
     * 应用版本号
     */
    private String version;
    /**
     * 设备唯一标示
     */
    private String udid;

    private DeviceType deviceType;

    @ModelAttribute
    public void setReqAndRes(HttpServletRequest request, HttpServletResponse response){
        this.request = request;
        this.response = response;
        this.session = request.getSession();
        this.token = request.getHeader(BaseController.TOKEN_STR);
    }


    public void setDeviceVersion(String versionStr) {
        if (versionStr != null && versionStr.contains(SEPARATOR)) {
            String[] strs = versionStr.split(SEPARATOR);
            if (strs.length == 3) {
                this.deviceType = DeviceType.getType(strs[0]);
                this.udid = strs[1];
                this.version = strs[2];
            }
        }
    }


    /**
     * 将HTTP请求参数映射到bean对象中
     * @param beanClass
     * @return
     * @throws Exception
     */
    public <T> T form(Class<T> beanClass) {
        try{
            T bean = beanClass.newInstance();
            BeanUtils.populate(bean, getParametersStartingWith(request,"1"));
            BeanUtils.copyProperties(bean, baseBean);
            return bean;
        }catch(Exception e) {
            e.printStackTrace();
            //throw new ActionException(e.getMessage());
            return null;
        }
    }

    /**
     * 取得带相同前缀的Request Parameters, copy from spring WebUtils.
     *
     * 返回的结果的Parameter名已去除前缀.
     */
    @SuppressWarnings("rawtypes")
    public static Map<String, Object> getParametersStartingWith(ServletRequest request, String prefix) {
        Enumeration paramNames = request.getParameterNames();
        Map<String, Object> params = new TreeMap<String, Object>();
        if (prefix == null) {
            prefix = "";
        }
        while ((paramNames != null) && paramNames.hasMoreElements()) {
            String paramName = (String) paramNames.nextElement();
            if ("".equals(prefix) || paramName.startsWith(prefix)) {
                String unprefixed = paramName.substring(prefix.length());
                String[] values = request.getParameterValues(paramName);
                if ((values == null) || (values.length == 0)) {
                    // Do nothing, no values found at all.
                } else if (values.length > 1) {
                    params.put(unprefixed, values);
                } else {
                    params.put(unprefixed, values[0]);
                }
            }
        }
        return params;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public static String getVersionStr() {
        return VERSION_STR;
    }

    public static void setVersionStr(String versionStr) {
        VERSION_STR = versionStr;
    }

    public static String getTokenStr() {
        return TOKEN_STR;
    }

    public static void setTokenStr(String tokenStr) {
        TOKEN_STR = tokenStr;
    }

    public BaseBean getBaseBean() {
        return baseBean;
    }

    public void setBaseBean(BaseBean baseBean) {
        this.baseBean = baseBean;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUdid() {
        return udid;
    }

    public void setUdid(String udid) {
        this.udid = udid;
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(DeviceType deviceType) {
        this.deviceType = deviceType;
    }
}
