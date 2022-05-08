package com.cloud.base.common.xugou.client.component.annotation;

import com.cloud.base.common.xugou.client.service.SecurityClient;
import com.cloud.base.common.xugou.core.model.entity.SecurityAuthority;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;

import java.lang.reflect.Method;

/**
 * @author lh0811
 * @date 2021/5/10
 */
@Aspect
@Slf4j
public class TokenToAuthorityAop extends AuthAbstractClass implements Ordered {

    @Autowired
    private SecurityClient securityClient;

    @Pointcut("@within(com.cloud.base.common.xugou.client.component.annotation.TokenToAuthority) || @annotation(com.cloud.base.common.xugou.client.component.annotation.TokenToAuthority)")
    public void annotationPointCut() {
    }


    @Around("annotationPointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        log.debug("进入HasTokenAop切面");
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        TokenToAuthority annotation = signature.getMethod().getAnnotation(TokenToAuthority.class);
        // 判断是否已经有了用户权限信息
        SecurityAuthority securityAuthority = getSecurityAuthority(joinPoint);
        if (securityAuthority != null) {
            // 获取到当前用户信息
            securityAuthority = securityClient.tokenToAuthority(annotation.require());
            // 设置用户权限信息
            setSecurityAuthority(joinPoint,securityAuthority);
        }else {
            // 获取到当前用户信息
            securityAuthority = securityClient.tokenToAuthority(annotation.require(),securityAuthority);
            // 设置用户权限信息
            setSecurityAuthority(joinPoint,securityAuthority);
        }
        Object proceed = joinPoint.proceed(joinPoint.getArgs());
        // 将新的token写入到返回值中
        Method setToken = proceed.getClass().getDeclaredMethod("setToken", String.class);
        if (setToken!=null) {
            setToken.invoke(proceed,securityAuthority.getToken());
        }
        log.debug("退出HasTokenAop切面");
        return proceed;
    }

    @Override
    public int getOrder() {
        return 0;
    }


}
