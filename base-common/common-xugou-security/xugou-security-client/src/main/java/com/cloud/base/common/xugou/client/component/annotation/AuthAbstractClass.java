package com.cloud.base.common.xugou.client.component.annotation;

import com.cloud.base.common.xugou.core.model.entity.SecurityAuthority;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * @author lh0811
 * @date 2022/5/4
 */
public abstract class AuthAbstractClass {


    /**
     * 获取用户权限信息
     *
     * @param joinPoint
     * @return
     */
    public SecurityAuthority getSecurityAuthority(ProceedingJoinPoint joinPoint) {
        // 替换入参
        Object[] args = joinPoint.getArgs();
        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];
            if (arg instanceof SecurityAuthority) {
               return (SecurityAuthority) arg;
            }
        }
        return null;
    }

    /**
     * 设置用户权限信息
     *
     * @param joinPoint
     * @param securityAuthority
     * @return
     */
    public Object[] setSecurityAuthority(ProceedingJoinPoint joinPoint,SecurityAuthority securityAuthority) {
        Object[] args = joinPoint.getArgs();
        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];
            if (arg instanceof SecurityAuthority)
                args[i] = securityAuthority;
        }
        return args;
    }
}
