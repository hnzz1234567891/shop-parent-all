package com.shop.proxy;

import com.alibaba.fastjson.JSON;
import com.shop.core.annotation.IsLogin;
import com.shop.core.annotation.OptLog;
import com.shop.core.constant.Constant;
import com.shop.core.exception.ParamException;
import com.shop.core.vo.LoginIndentity;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;

/**
 * Created by TW on 2017/6/17.
 */
@Component
@Aspect
public class OptLogProxy {

    private static Logger logger = LoggerFactory.getLogger(OptLogProxy.class);

    @Autowired
    HttpServletRequest request; //这里可以获取到request

    /**
     * 设置切入点
     */
    @Pointcut("@annotation(com.shop.core.annotation.OptLog)")
    public void pointcut() {

    }

    @Around(value = "pointcut() && @annotation(optLog)")
    public Object handlerMethod(ProceedingJoinPoint pjp, OptLog optLog) throws Throwable {

        if (optLog == null) { // 如果没有这注解就放行
            return pjp.proceed(); // 执行
        }
        String module = optLog.module();
        String remarke = optLog.remark();

        // 获取request
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder
                .getRequestAttributes()).getRequest();

        Map<String, String[]> params = request.getParameterMap();

        // 获取登录用户信息
        LoginIndentity loginIndentity = (LoginIndentity)request.getSession().getAttribute(Constant.LOGIN_USER_KEY);
        long startTime = new Date().getTime();
        Object result = pjp.proceed();
        long endTime = new Date().getTime();
        // 执行时间
        long processTime = startTime - endTime;
        // 执行结果
        String resultJson = JSON.toJSONString(result);
        // 存入mysql



        return result; // 放行
    }



}
