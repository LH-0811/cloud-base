package com.cloud.base.modules.xugou.client.component.annotation;

import com.cloud.base.common.exception.CommonException;
import com.cloud.base.common.response.ServerResponse;
import com.cloud.base.modules.xugou.client.service.SecurityClient;
import com.cloud.base.modules.xugou.core.model.entity.SecurityAuthority;
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

/**
 * @author lh0811
 * @date 2021/5/10
 */
@Aspect
@Slf4j
public class HasUrlAop {

    @Autowired
    private SecurityClient securityClient;

    @Pointcut("@within(com.cloud.base.modules.xugou.client.component.annotation.HasUrl) || @annotation(com.cloud.base.modules.xugou.client.component.annotation.HasUrl)")
    public void annotationPointCut() {
    }


    @Around("annotationPointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        log.debug("进入HasUrlAop切面");
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        HasUrl annotation = signature.getMethod().getAnnotation(HasUrl.class);
        String url = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getRequestURI();
        if (StringUtils.isBlank(url)) {
            throw CommonException.create(ServerResponse.createByError("使用HasUrl标志的方法必须在请求上下文中:"+signature.getDeclaringType().getName()+"."+signature.getMethod().getName()));
        }
        // 判断是否有静态资源权限并返回权限信息
        SecurityAuthority securityAuthority = securityClient.hasUrl(url);
        // 替换入参
        Object[] args = joinPoint.getArgs();
        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];
            if (arg instanceof SecurityAuthority)
                args[i] = securityAuthority;
        }
        Object proceed = joinPoint.proceed(args);
        log.debug("退出HasUrlAop切面");
        return proceed;
    }

}
