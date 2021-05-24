package com.cloud.base.core.modules.lh_security.client.component.annotation;

import com.cloud.base.core.common.exception.CommonException;
import com.cloud.base.core.common.response.ServerResponse;
import com.cloud.base.core.modules.lh_security.client.component.SecurityClient;
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
public class HasPermsCodeAop {

    @Autowired
    private SecurityClient securityClient;


    @Pointcut("@annotation(com.cloud.base.core.modules.lh_security.client.component.annotation.HasPermsCode)")
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

        // 判断是否有permsCode
        SecurityAuthority securityAuthority = securityClient.hasPermsCode(permsCode);

        // 补充参数
        Object[] args = joinPoint.getArgs();
        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];
            if (arg instanceof SecurityAuthority)
                args[i] = securityAuthority;
        }
        Object proceed = joinPoint.proceed(args);
        log.debug("退出HasPermsCodeAop切面");
        return proceed;
    }

}
