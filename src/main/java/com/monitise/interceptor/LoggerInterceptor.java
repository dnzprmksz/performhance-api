package com.monitise.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerInterceptor extends HandlerInterceptorAdapter{

    static Logger logger = LoggerFactory.getLogger(LoggerInterceptor.class);


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        long startTime = System.currentTimeMillis();
        logger.info("Request URL::" + request.getRequestURL().toString()
                + ":: Start Time=" + startTime);
        System.out.println("---Before Method Execution---");
        return true;
    }
    @Override
    public void postHandle(	HttpServletRequest request, HttpServletResponse response,
                               Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println("---method executed---");
    }
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) throws Exception {
        System.out.println("---View Rendered?!---");
    }

}