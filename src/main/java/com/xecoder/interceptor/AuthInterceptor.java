package com.xecoder.interceptor;

import com.xecoder.common.exception.CustomException;
import com.xecoder.controller.core.BaseController;
import com.xecoder.model.core.BaseBean;
import com.xecoder.model.core.NoAuth;
import com.xecoder.service.impl.AuthServerImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Locale;

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
        boolean isAn = method.getMethod().isAnnotationPresent(NoAuth.class);

        if (isAn) {
            return true;
        }

        String token = null;

        if (request.getHeader(BaseController.TOKEN_STR) != null) {
            token = request.getHeader(BaseController.TOKEN_STR);
        }
        else if(request.getParameter(BaseController.TOKEN_STR)!=null){//静态服务器调用时，数据放在url里面
            token = request.getParameter(BaseController.TOKEN_STR);
        }
        else {

            throw new CustomException(messageSource.getMessage("error.user.not.register",null, Locale.getDefault()));
        }
        String userId = authServer.getUserIdByToken(token);
        if (userId == null) {
            throw new CustomException(messageSource.getMessage("error.user.out.time",null, Locale.getDefault()));
        }


        try {
            final Claims claims = Jwts.parser().setSigningKey("secretkey")
                    .parseClaimsJws(token).getBody();
            request.setAttribute("claims", claims);
        }
        catch (final SignatureException e) {
            throw new ServletException("Invalid token.");
        }

        BaseController base = (BaseController)((HandlerMethod) handler).getBean();
        //base.setUserId(userId);
        BaseBean baseBean = new BaseBean();
        baseBean.setCreator("系统用户");
        baseBean.setLastModifier("最后修 改者人klp");
        base.setBaseBean(baseBean);
        base.setDeviceVersion(request.getHeader(BaseController.VERSION_STR));
        return true;
    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }
}
