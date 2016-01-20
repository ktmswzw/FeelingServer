package com.xecoder;

import com.xecoder.interceptor.AuthInterceptor;
import com.xecoder.interceptor.LogInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

//@EnableAutoConfiguration
//@ComponentScan
//@Configuration
@SpringBootApplication
public class FeelingApplication extends WebMvcConfigurerAdapter {


	/**
	 * 添加过滤
	 * @param registry
     */
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new AuthInterceptor()).addPathPatterns("/greet/**");//认证
		registry.addInterceptor(new LogInterceptor()).addPathPatterns("/**");//日志
	}


	public static void main(String[] args) {
		SpringApplication.run(FeelingApplication.class, args);
	}

}
