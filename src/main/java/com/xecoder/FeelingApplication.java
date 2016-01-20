package com.xecoder;

import com.xecoder.interceptor.AuthInterceptor;
import com.xecoder.interceptor.LogInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import java.util.Locale;

//@EnableAutoConfiguration
//@ComponentScan
//@Configuration
@SpringBootApplication
public class FeelingApplication extends WebMvcConfigurerAdapter {

	@Bean
	public ReloadableResourceBundleMessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("classpath:lang/messages");
		messageSource.setCacheSeconds(100000); //reload messages every 10 seconds
		return messageSource;
	}


	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
		LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
		localeChangeInterceptor.setParamName("language");
		return localeChangeInterceptor;
	}

	@Bean(name = "localeResolver")
	public CookieLocaleResolver localeResolver() {
		CookieLocaleResolver localeResolver = new CookieLocaleResolver();
		Locale defaultLocale = new Locale("en");
		localeResolver.setDefaultLocale(defaultLocale);
		return localeResolver;
	}

	/**
	 * 添加过滤
	 * @param registry
     */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new AuthInterceptor()).addPathPatterns("/greet/**");//认证
		registry.addInterceptor(new LogInterceptor()).addPathPatterns("/**");//日志
		registry.addInterceptor(localeChangeInterceptor());
	}


	public static void main(String[] args) {
		SpringApplication.run(FeelingApplication.class, args);
	}

}
