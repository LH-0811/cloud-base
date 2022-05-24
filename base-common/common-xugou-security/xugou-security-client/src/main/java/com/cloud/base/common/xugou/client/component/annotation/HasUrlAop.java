package com.cloud.base.common.xugou.client.component.annotation;

import com.cloud.base.common.core.exception.CommonException;
import com.cloud.base.common.core.response.ServerResponse;
import com.cloud.base.common.xugou.client.service.SecurityClient;
import com.cloud.base.common.xugou.core.model.entity.SecurityAuthority;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @author lh0811
 * @date 2021/5/10
 */
@Aspect
@Slf4j
public class HasUrlAop extends AuthAbstractClass {

    @Autowired
    private SecurityClient securityClient;

    @Pointcut("@within(com.cloud.base.common.xugou.client.component.annotation.HasUrl) || @annotation(com.cloud.base.common.xugou.client.component.annotation.HasUrl)")
    public void annotationPointCut() {
    }


    @Around("annotationPointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        log.debug("进入HasUrlAop切面");
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        HasUrl annotation = signature.getMethod().getAnnotation(HasUrl.class);
        if (annotation == null) {
            Annotation declaredAnnotation = signature.getDeclaringType().getDeclaredAnnotation(HasUrl.class);
            if (declaredAnnotation != null) {
                annotation = (HasUrl) declaredAnnotation;
            }
        }
        String url = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getRequestURI();
        if (StringUtils.isBlank(url)) {
            throw CommonException.create(ServerResponse.createByError("使用HasUrl标志的方法必须在请求上下文中:"+signature.getDeclaringType().getName()+"."+signature.getMethod().getName()));
        }

        // 判断是否已经有了用户权限信息
        SecurityAuthority securityAuthority = getSecurityAuthority(joinPoint);
        if (securityAuthority != null) {
            // 获取到当前用户信息
            securityAuthority = securityClient.hasUrl(url);
            // 设置用户权限信息
            setSecurityAuthority(joinPoint,securityAuthority);
        }else {
            // 获取到当前用户信息
            securityClient.hasUrl(url,securityAuthority);
            // 设置用户权限信息
            setSecurityAuthority(joinPoint,securityAuthority);
        }

        Object proceed = joinPoint.proceed(joinPoint.getArgs());
        if (proceed == null) {
            return proceed;
        }
        // 将新的token写入到返回值中
        Method setToken = proceed.getClass().getDeclaredMethod("setToken", String.class);
        if (setToken!=null) {
            setToken.invoke(proceed,securityAuthority.getToken());
        }
        log.debug("退出HasUrlAop切面");
        return proceed;
    }

}
