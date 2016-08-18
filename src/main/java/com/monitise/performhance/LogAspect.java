package com.monitise.performhance;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class LogAspect {
    @Pointcut("execution(* com.monisitse.performhance.api.*(..)")
    public void defineEntryPoint() {
    }

    @Before("com.monitise.performhance.api.OrganizationController.getAll()")
    public void beforeMethod(JoinPoint joinPoint) {
        System.out.println("Before : " + joinPoint.getSignature());
    }

    @After("execution(* com.monitise.performhance.*.*.*.(..))")
    public void afterMethod(JoinPoint joinPoint) {
        System.out.println("After :" + joinPoint.getSignature());
    }
}
