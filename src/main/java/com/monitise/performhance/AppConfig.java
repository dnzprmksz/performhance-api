package com.monitise.performhance;

import com.monitise.performhance.helpers.LoggerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableAspectJAutoProxy
@ComponentScan("com.monitise.performhance")
public class AppConfig extends WebMvcConfigurerAdapter {

//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//
//        registry.addInterceptor(new LoggerInterceptor());
//
//    }

//    @Bean
//    public LogAspect logAspect() {
//        return new LogAspect();
//    }
}
