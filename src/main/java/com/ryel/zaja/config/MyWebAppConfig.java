package com.ryel.zaja.config;

import com.ryel.zaja.fillter.MyInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by billyu on 2017/1/3.
 */
@Configuration
public class MyWebAppConfig extends WebMvcConfigurerAdapter {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 多个拦截器组成一个拦截器链
        // addPathPatterns 用于添加拦截规则
        // excludePathPatterns 用户排除拦截
//        registry.addInterceptor(new MyInterceptor())
//                .addPathPatterns("*//**//*api*//**");
//        super.addInterceptors(registry);
    }

    //全局解决跨域的问题
    @Override
    public void addCorsMappings(CorsRegistry registry) {

        registry.addMapping("/back/*").allowedOrigins("*");

        registry.addMapping("/api/*").allowedOrigins("*");
    }
}
