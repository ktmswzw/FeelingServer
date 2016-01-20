package com.xecoder.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class LogInterceptor implements HandlerInterceptor {

    /*@Autowired
    LogRecordDao dao;*/

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        /*HandlerMethod method = (HandlerMethod) handler;
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

        HabitLogger.request("{} {} {} {} {} {} {} {}", time, action, token, uid, record.getParams(), version, ip, result);*/
    }
}
