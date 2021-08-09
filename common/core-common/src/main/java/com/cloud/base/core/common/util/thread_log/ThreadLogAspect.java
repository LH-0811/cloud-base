package com.cloud.base.core.common.util.thread_log;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.SourceLocation;
import org.springframework.stereotype.Component;

/**
 * 系统日志切面
 * @author zhuzhe
 * @date 2018/6/4 9:27
 * @email 1529949535@qq.com
 */
@Slf4j
@Aspect  // 使用@Aspect注解声明一个切面
@Component
public class ThreadLogAspect {
 

    /**
     * 这里我们使用注解的形式
     * 当然，我们也可以通过切点表达式直接指定需要拦截的package,需要拦截的class 以及 method
     * 切点表达式:   execution(...)
     */
    @Pointcut("execution(* com.*..controller.*.*(..))")
    public void logPointCut() {}
 
    /**
     * 环绕通知 @Around  ， 当然也可以使用 @Before (前置通知)  @After (后置通知)
     * @param point
     * @return
     * @throws Throwable
     */
    @After("logPointCut()")
    public void around(JoinPoint point) throws Throwable {
        // Controller 层统一输出日志
        ThreadLog.output();
    }

}
