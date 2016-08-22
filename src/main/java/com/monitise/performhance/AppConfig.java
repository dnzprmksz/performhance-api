package com.monitise.performhance;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableAspectJAutoProxy
@ComponentScan("com.monitise.performhance")
public class AppConfig extends WebMvcConfigurerAdapter {
}
