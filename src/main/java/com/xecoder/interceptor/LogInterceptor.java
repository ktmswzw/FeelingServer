package com.xecoder.interceptor;

import com.xecoder.common.util.FeelingLogger;
import com.xecoder.common.util.IPUtils;
import com.xecoder.controller.core.BaseController;
import com.xecoder.model.business.LogRecord;
import com.xecoder.service.dao.LogRecordDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;


public class LogInterceptor implements HandlerInterceptor {

    @Autowired
    private LogRecordDao dao;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerMethod method = (HandlerMethod) handler;
        BaseController baseController = (BaseController) method.getBean();
        Date now = new Date();
        ContentResposeWrapper wrapper = (ContentResposeWrapper) response;

        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(now);
        String uid = baseController.getUserId();
        String action = request.getRequestURI();
        String version = request.getHeader(BaseController.VERSION_STR);
        String token = request.getHeader(BaseController.TOKEN_STR);
        String ip = IPUtils.getRealIpAddr(request);
        String result = new String(wrapper.getContentAsByteArray());

        LogRecord record = new LogRecord(now, uid, action, ip, result, version, request.getParameterMap());
        dao.save(record);

        FeelingLogger.request("{} {} {} {} {} {} {} {}", time, action, token, uid, record.getParams(), version, ip, result);
    }
}
