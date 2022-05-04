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


@Aspect
@Slf4j
public class HasPermsCodeAop extends AuthAbstractClass {

    @Autowired
    private SecurityClient securityClient;


    @Pointcut("@within(com.cloud.base.common.xugou.client.component.annotation.HasPermsCode) || @annotation(com.cloud.base.common.xugou.client.component.annotation.HasPermsCode)")
    public void annotationPointCut() {
    }


    @Around("annotationPointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        log.debug("进入HasPermsCodeAop切面");
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        HasPermsCode annotation = signature.getMethod().getAnnotation(HasPermsCode.class);
        String permsCode = annotation.permsCode();
        if (StringUtils.isBlank(permsCode))
            throw CommonException.create(ServerResponse.createByError("使用@HasPermsCode注解，必须填写permsCode。"));
        // 判断是否已经有了用户权限信息
        SecurityAuthority securityAuthority = getSecurityAuthority(joinPoint);
        if (securityAuthority != null) {
            // 获取到当前用户信息
            securityAuthority = securityClient.hasPermsCode(permsCode);
            // 设置用户权限信息
            setSecurityAuthority(joinPoint, securityAuthority);
        }else {
            // 获取到当前用户信息
            securityAuthority = securityClient.hasPermsCode(permsCode,securityAuthority);
            // 设置用户权限信息
            setSecurityAuthority(joinPoint, securityAuthority);
        }
        Object proceed = joinPoint.proceed(joinPoint.getArgs());
        log.debug("退出HasPermsCodeAop切面");
        return proceed;
    }

}
