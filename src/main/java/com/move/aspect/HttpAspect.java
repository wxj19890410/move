package com.move.aspect;

import com.google.common.collect.Lists;
import com.move.exception.NotLogedInException;
import com.move.utils.Globals;
import com.move.utils.UserInfo;
import com.move.utils.Utilities;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Aspect
@Component
public class HttpAspect {
    public static final String LOGIN_HEADER_NAME = "loginUuid";

    static final String[] NO_AUTH_CONTROLLERS = new String[]{"com.move.action.LoginAction"};

    @Pointcut("execution(public  * com.move.action.*.*(..))")
    public void log() {

    }

    @Before("log()")
    public void doBefore(JoinPoint joinPoint) {
        String actionClass = joinPoint.getSignature().getDeclaringTypeName();

        if(!Arrays.asList(NO_AUTH_CONTROLLERS).contains(actionClass)){
            ServletRequestAttributes attributes= (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            String loginUuid = request.getHeader(LOGIN_HEADER_NAME);
            synchronized (Globals.USER_INFOS) {
                if (Globals.USER_INFOS.containsKey(loginUuid)) {
                    UserInfo userInfo = Globals.USER_INFOS.get(loginUuid);
                    // 最后操作时间加上自动登出时间已到
                    if ( Utilities.addDayHourMinute(userInfo.getOperateDate(), 0, 0, 120)
                            .before(new Date())) {
                        Globals.USER_INFOS.remove(loginUuid);
                        throw new NotLogedInException();
                    }
                    userInfo.setOperateDate(new Date());
                }else{
                    throw new NotLogedInException();
                }
            }
        }
    }
    @After("log()")
    public void doAfter() {

    }
}
