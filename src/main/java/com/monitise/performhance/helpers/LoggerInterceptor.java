package com.monitise.performhance.helpers;


import org.apache.catalina.servlet4preview.http.HttpServletRequestWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.List;

public class LoggerInterceptor extends HandlerInterceptorAdapter {

    static Logger logger = LoggerFactory.getLogger(LoggerInterceptor.class);
    // TODO: find a way to log bodies.


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        long start = System.currentTimeMillis();
        logger.info("## -SHOTS FIRED- ##");

        logger.info("Request URL: " + request.getRequestURL().toString());

        List<String> requestParamNames = Collections.list(request.getParameterNames());
        logger.info("Parameter number: " + requestParamNames.size());

        for (String paramName : requestParamNames) {
            logger.info("Parameter name: " + paramName + " - Parameter value: " + request.getParameter(paramName));
        }
        HttpServletRequest requestToCache = new ContentCachingRequestWrapper(request);
        String requestData = getRequestData(requestToCache);
        System.out.println(requestData);


        return true;
    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView modelAndView) throws Exception {


    }


    private String getRequestBody(final HttpServletRequest request)
            throws Exception {

        HttpServletRequestWrapper requestWrapper = new HttpServletRequestWrapper(request);
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader;

        InputStream inputStream = requestWrapper.getInputStream();
        if (inputStream != null) {
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            char[] charBuffer = new char[128];
            int bytesRead = -1;
            while ((bytesRead = bufferedReader.read(charBuffer)) != -1) {
                stringBuilder.append(charBuffer, 0, bytesRead);
            }
        }

        return stringBuilder.toString();
    }

    public static String getRequestData(final HttpServletRequest request) throws UnsupportedEncodingException {
        String payload = null;
        ContentCachingRequestWrapper wrapper = WebUtils.getNativeRequest(request, ContentCachingRequestWrapper.class);
        if (wrapper != null) {
            byte[] buf = wrapper.getContentAsByteArray();
            if (buf.length > 0) {
                payload = new String(buf, 0, buf.length, wrapper.getCharacterEncoding());
            }
        }
        return payload;
    }


}