package com.acrobat.ztb.config;

import com.acrobat.ztb.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * 拦截器配置
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Resource
    private LoginInterceptor loginInterceptor;

    @Value("${spring.mvc.static-path-pattern}")
    private String staticPathPattern;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 登录拦截器，拦截除静态资源、登录相关请求，测试相关请求外的所有请求
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/user/**")
                .excludePathPatterns("/test/**")
                .excludePathPatterns("/transact/**")
                .excludePathPatterns(staticPathPattern);

        WebMvcConfigurer.super.addInterceptors(registry);
    }

}