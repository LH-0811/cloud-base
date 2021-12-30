package com.cloud.base.common.multisource.aspect;

import com.cloud.base.common.multisource.annotation.DataSource;
import com.cloud.base.common.multisource.config.DynamicContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 多数据源，切面处理类
 */
@Slf4j
@Aspect
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class DataSourceAspect {

    @Pointcut("@annotation(com.cloud.base.common.multisource.annotation.DataSource) " +
            "|| @within(com.cloud.base.common.multisource.annotation.DataSource)")
    public void dataSourcePointCut() {

    }

    @Around("dataSourcePointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Class targetClass = point.getTarget().getClass();
        Method method = signature.getMethod();
        DataSource targetDataSource = (DataSource) targetClass.getAnnotation(DataSource.class);
        DataSource methodDataSource = method.getAnnotation(DataSource.class);
        if (targetDataSource != null || methodDataSource != null) {
            String value;
            if (methodDataSource != null) {
                value = methodDataSource.value();
            } else {
                value = targetDataSource.value();
            }

// todo 如果是在请求参数中添加tenantParam 则打开这个代码并做些修改来适配
//            Object[] args = point.getArgs();
//            for (Object arg : args) {
//                if (arg instanceof TenantParam) {
//                    TenantParam tenantParam = (TenantParam) arg;
//                    value = String.valueOf(tenantParam.getTenantId());
//                }
//            }

            DynamicContextHolder.push( value);
            log.info("set datasource is {}", value);
        }

        try {
            return point.proceed();
        } finally {
            DynamicContextHolder.poll();
            log.info("clean datasource");
        }
    }
}
