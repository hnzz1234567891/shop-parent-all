package com.shop.proxy;

import com.shop.core.annotation.IsLogin;
import com.shop.core.constant.Constant;
import com.shop.core.exception.ParamException;
import com.shop.core.vo.LoginIndentity;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * Created by TW on 2017/6/17.
 */
@Component
@Aspect
public class CheckIsLoginProxy {

    private static Logger logger = LoggerFactory.getLogger(CheckIsLoginProxy.class);

    @Autowired
    HttpServletRequest request; //这里可以获取到request

    /**
     * 设置切入点
     */
    @Pointcut("@annotation(com.shop.core.annotation.IsLogin)")
//    @Pointcut("execution(* com.shop.controller.*.*(..))")
    public void pointcut() {

    }

    /*
    @Around(value = "pointcut() && @annotation(isLogin)")
    public Object handlerMethod(ProceedingJoinPoint pjp, IsLogin isLogin) throws Throwable {

//        try {
        if (isLogin == null) { // 如果没有这注解就放行
            return pjp.proceed(); // 执行
        }
        // 获取request
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder
                .getRequestAttributes()).getRequest();

        request.getParameterMap();

        // 验证用户是否登录
        LoginIndentity loginIndentity = (LoginIndentity)request.getSession().getAttribute(Constant.LOGIN_USER_KEY);
        if (loginIndentity == null || loginIndentity.getId() == null || loginIndentity.getId() < 1) {
            throw new ParamException(Constant.LOGIN_CODE, "请登录");
        }
//        } catch (Throwable t) {
//            logger.info("执行异常：{}", t);
//        }
        return pjp.proceed(); // 放行
    }*/

    /*
    @Around(value = "pointcut()")
    public Object handlerMethod(ProceedingJoinPoint pjp) throws Throwable {

//        try {
        MethodSignature methodSignature = (MethodSignature)pjp.getSignature();
        Method method = methodSignature.getMethod();
        IsLogin isLogin = method.getAnnotation(IsLogin.class);
        if (isLogin == null) { // 如果没有这注解就放行
            return pjp.proceed(); // 执行
        }
        // 获取request
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder
                .getRequestAttributes()).getRequest();

        request.getParameterMap();

        // 验证用户是否登录
        LoginIndentity loginIndentity = (LoginIndentity)request.getSession().getAttribute(Constant.LOGIN_USER_KEY);
        if (loginIndentity == null || loginIndentity.getId() == null || loginIndentity.getId() < 1) {
            throw new ParamException(Constant.LOGIN_CODE, "请登录");
        }
//        } catch (Throwable t) {
//            logger.info("执行异常：{}", t);
//        }
        return pjp.proceed(); // 放行
    }*/

    @Around(value = "execution(* com.shop.controller.*.*(..))")
    public Object handlerMethod(ProceedingJoinPoint pjp) throws Throwable {

//        try {

        MethodSignature methodSignature = (MethodSignature)pjp.getSignature();
        Method method = methodSignature.getMethod();
        IsLogin isLogin = method.getAnnotation(IsLogin.class);
        if (isLogin == null) { // 如果没有这注解就放行
            return pjp.proceed(); // 执行
        }
        // 获取request
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder
                .getRequestAttributes()).getRequest();

        request.getParameterMap();

        // 验证用户是否登录
        LoginIndentity loginIndentity = (LoginIndentity)request.getSession().getAttribute(Constant.LOGIN_USER_KEY);
        if (loginIndentity == null || loginIndentity.getId() == null || loginIndentity.getId() < 1) {
            throw new ParamException(Constant.LOGIN_CODE, "请登录");
        }
//        } catch (Throwable t) {
//            logger.info("执行异常：{}", t);
//        }
        return pjp.proceed(); // 放行
    }

}
