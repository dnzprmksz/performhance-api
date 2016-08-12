package com.monitise;


import com.monitise.interceptor.LoggerInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.interceptor.TransactionInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration

public class AppConfig extends WebMvcConfigurerAdapter {

    @Override

    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(new LoggerInterceptor());

    }

}

