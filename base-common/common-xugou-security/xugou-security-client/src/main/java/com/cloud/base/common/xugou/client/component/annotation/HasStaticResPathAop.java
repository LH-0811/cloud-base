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

import java.lang.reflect.Method;


@Aspect
@Slf4j
public class HasStaticResPathAop extends AuthAbstractClass {

    @Autowired
    private SecurityClient securityClient;

    @Pointcut("@within(com.cloud.base.common.xugou.client.component.annotation.HasStaticResPath) || @annotation(com.cloud.base.common.xugou.client.component.annotation.HasStaticResPath)")
    public void annotationPointCut() {
    }


    @Around("annotationPointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        log.debug("进入HasStaticResPathAop切面");
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        HasStaticResPath annotation = signature.getMethod().getAnnotation(HasStaticResPath.class);

        String resPath = annotation.resPath();
        if (StringUtils.isBlank(resPath))
            throw CommonException.create(ServerResponse.createByError("使用@HasStaticResPath注解，必须填写resPath。"));

        // 判断是否已经有了用户权限信息
        SecurityAuthority securityAuthority = getSecurityAuthority(joinPoint);
        if (securityAuthority != null) {
            // 获取到当前用户信息
            securityAuthority = securityClient.hasStaticResPath(resPath);
            // 设置用户权限信息
            setSecurityAuthority(joinPoint,securityAuthority);
        }else {
            // 获取到当前用户信息
            securityAuthority = securityClient.hasStaticResPath(resPath,securityAuthority);
            // 设置用户权限信息
            setSecurityAuthority(joinPoint,securityAuthority);
        }

        Object proceed = joinPoint.proceed(joinPoint.getArgs());
        // 将新的token写入到返回值中
        Method setToken = proceed.getClass().getDeclaredMethod("setToken", String.class);
        if (setToken!=null) {
            setToken.invoke(proceed,securityAuthority.getToken());
        }
        log.debug("退出HasStaticResPathAop切面");
        return proceed;

    }
}
