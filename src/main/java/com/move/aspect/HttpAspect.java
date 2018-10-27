package com.move.aspect;

import com.google.common.collect.Lists;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Aspect
@Component
public class HttpAspect {
    private static List<String> BASE_ACTION = Lists.newArrayList("com.move.action.UserAction");

    @Pointcut("execution(public  * com.move.action.*.*(..))")
    public void log() {
    }

    @Before("log()")
    public void doBefore(JoinPoint joinPoint) {
        String actionClass = joinPoint.getSignature().getDeclaringTypeName();
        if(BASE_ACTION.contains(actionClass)){
            System.out.print("2222");
        }else{
            System.out.print("1111");
        }
        ServletRequestAttributes attributes= (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

    /*    System.out.print("url:"+request.getRequestURI());

        System.out.print("method:"+request.getMethod());

        System.out.print("ip:"+request.getRemoteAddr());

        System.out.print("lei:"+joinPoint.getSignature().getDeclaringTypeName());*/
    }
    @After("log()")
    public void doAfter() {

    }
}
