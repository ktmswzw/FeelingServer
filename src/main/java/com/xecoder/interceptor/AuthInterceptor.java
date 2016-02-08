package com.xecoder.interceptor;

import com.xecoder.common.exception.HttpServiceException;
import com.xecoder.common.util.JWTCode;
import com.xecoder.controller.core.BaseController;
import com.xecoder.model.core.BaseBean;
import com.xecoder.model.core.NonAuthoritative;
import com.xecoder.service.impl.AuthServerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by  moxz
 * User: 224911261@qq.com
 * 2016/1/11-13:21
 * Feeling.com.xecoder.model
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Autowired
    private AuthServerImpl authServer;

    @Autowired
    private MessageSource messageSource;

    private List<String> excluded;

    public void setExcluded(List<String> excludedUrls) {
        this.excluded = excludedUrls;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (excluded != null && excluded.contains(request.getRequestURI())) {
            return true;
        }

        HandlerMethod method = (HandlerMethod) handler;
        boolean isAn = method.getMethod().isAnnotationPresent(NonAuthoritative.class);

        if (isAn) {
            return true;
        }

        String authorization = null;
        String token = null;

        if (request.getHeader(JWTCode.AUTHORIZATION_STR) != null) {
            authorization = request.getHeader(JWTCode.AUTHORIZATION_STR);
        }
        else if(request.getParameter(JWTCode.AUTHORIZATION_STR)!=null){//静态服务器调用时，数据放在url里面
            authorization = request.getParameter(JWTCode.AUTHORIZATION_STR);
        }
        else {
            throw new HttpServiceException(getMsg("error.user.not.register"));
        }
        try {
            Map<String, Object> claims = JWTCode.VERIFIER.verify(authorization);
            if(claims.size()!=0)
            {
                if(claims.containsKey(BaseController.TOKEN_STR))
                {
                    request.setAttribute(BaseController.TOKEN_STR, claims.get(BaseController.TOKEN_STR));
                    token = (String)claims.get(BaseController.TOKEN_STR);
                }
            }
            request.setAttribute("claims", claims);
        }
        catch (Exception e) {
            throw new HttpServiceException(getMsg("error.token.validation.failed"));
        }

        String userId = authServer.getUserIdByToken(token);
        if (userId == null) {
            throw new HttpServiceException(getMsg("error.user.out.time"));
        }

        BaseController base = (BaseController)((HandlerMethod) handler).getBean();
        //base.setUserId(userId);
        BaseBean baseBean = new BaseBean();
        baseBean.setBaseCreator("系统用户");
        baseBean.setBaseLastModifier("最后修");
        base.setBaseBean(baseBean);
        base.setDeviceVersion(request.getHeader(BaseController.VERSION_STR));
        return true;
    }

    private String getMsg(String msgcode) {
        return messageSource.getMessage(msgcode,null, Locale.getDefault());
    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }
}
