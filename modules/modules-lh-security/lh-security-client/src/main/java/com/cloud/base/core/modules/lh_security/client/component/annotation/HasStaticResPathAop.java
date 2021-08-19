package com.cloud.base.core.modules.lh_security.client.component.annotation;

import com.cloud.base.core.common.exception.CommonException;
import com.cloud.base.core.common.response.ServerResponse;
import com.cloud.base.core.modules.lh_security.client.service.SecurityClient;
import com.cloud.base.core.modules.lh_security.core.entity.SecurityAuthority;
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
public class HasStaticResPathAop {

    @Autowired
    private SecurityClient securityClient;

    @Pointcut("@within(com.cloud.base.core.modules.lh_security.client.component.annotation.HasStaticResPath)")
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


        // 判断是否有静态资源权限并返回权限信息
        SecurityAuthority securityAuthority = securityClient.hasStaticResPath(resPath);

        // 补充入参
        Object[] args = joinPoint.getArgs();
        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];
            if (arg instanceof SecurityAuthority)
                args[i] = securityAuthority;
        }
        Object proceed = joinPoint.proceed(args);
        log.debug("退出HasStaticResPathAop切面");
        return proceed;

    }
}
