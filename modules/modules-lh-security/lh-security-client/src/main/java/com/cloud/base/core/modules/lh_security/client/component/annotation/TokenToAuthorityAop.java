package com.cloud.base.core.modules.lh_security.client.component.annotation;

import com.cloud.base.core.modules.lh_security.client.service.SecurityClient;
import com.cloud.base.core.modules.lh_security.core.entity.SecurityAuthority;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;

/**
 * @author lh0811
 * @date 2021/5/10
 */
@Aspect
@Slf4j
public class TokenToAuthorityAop implements Ordered {

    @Autowired
    private SecurityClient securityClient;

    @Pointcut("@within(com.cloud.base.core.modules.lh_security.client.component.annotation.TokenToAuthority) || @annotation(com.cloud.base.core.modules.lh_security.client.component.annotation.TokenToAuthority)")
    public void annotationPointCut() {
    }


    @Around("annotationPointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        log.debug("进入HasTokenAop切面");
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        TokenToAuthority annotation = signature.getMethod().getAnnotation(TokenToAuthority.class);

        // 获取到当前用户信息
        SecurityAuthority securityAuthority = securityClient.tokenToAuthority();

        // 替换入参
        Object[] args = joinPoint.getArgs();
        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];
            if (arg instanceof SecurityAuthority)
                args[i] = securityAuthority;
        }
        Object proceed = joinPoint.proceed(args);
        log.debug("退出HasTokenAop切面");
        return proceed;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
