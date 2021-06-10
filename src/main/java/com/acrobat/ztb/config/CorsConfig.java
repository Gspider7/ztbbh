package com.acrobat.ztb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.List;

/**
 * 全局跨域请求配置
 *
 * @author xutao
 * @date 2020-03-15 09:51
 */
@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {

        CorsConfiguration config = new CorsConfiguration();

        List<String> allowedHeaders = Arrays.asList("x-auth-token", "content-type", "X-Requested-With", "XMLHttpRequest");
        List<String> exposedHeaders = Arrays.asList("x-auth-token", "content-type", "X-Requested-With", "XMLHttpRequest");
        List<String> allowedMethods = Arrays.asList("POST", "GET", "DELETE", "PUT", "OPTIONS");
        List<String> allowedOrigins = Arrays.asList("*");
        config.setAllowedHeaders(allowedHeaders);   // 允许头携带信息
        config.setAllowedMethods(allowedMethods);   // 允许的跨域请求方式
        config.setAllowedOrigins(allowedOrigins);   // 允许从哪些地址发起跨域访问
        config.setExposedHeaders(exposedHeaders);   // 暴露哪些头信息，因为跨域访问默认不能获得全部头信息

        config.setMaxAge(36000L);   // 多久时间内不需要重新验证
        config.setAllowCredentials(true);   // 是否允许发送Cookie

        // 添加映射路径
        UrlBasedCorsConfigurationSource configurationSource = new UrlBasedCorsConfigurationSource();
        configurationSource.registerCorsConfiguration("/**", config);

        return new CorsFilter(configurationSource);
    }
}
