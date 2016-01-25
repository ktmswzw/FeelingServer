package com.xecoder.interceptor;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by  moxz
 * User: 224911261@qq.com
 * 2016/1/21-14:12
 * Feeling.com.xecoder.interceptor
 */
@Component
public class CORSFilter  implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    //跨域
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;
//        response.setHeader("Access-Control-Allow-Origin", "http://www.xecoder.com");
//        response.setHeader("Access-Control-Allow-Methods", "POST, GET, DELETE, OPTIONS, PUT");
//        response.setHeader("Access-Control-Max-Age", "3600");
//        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Accept");
        chain.doFilter(req, res);
    }

    @Override
    public void destroy() {

    }
}
