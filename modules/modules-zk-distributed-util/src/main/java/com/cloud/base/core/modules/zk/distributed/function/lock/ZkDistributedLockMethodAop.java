package com.cloud.base.core.modules.zk.distributed.function.lock;

import com.cloud.base.core.common.entity.ServerResponse;
import com.cloud.base.core.common.exception.CommonException;
import com.cloud.base.core.modules.zk.distributed.client.ZkDistributedClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class ZkDistributedLockMethodAop {

    @Autowired
    private ZkDistributedClient zkClient;

    @Pointcut("@annotation(com.cloud.base.core.modules.zk.distributed.function.lock.ZkDistributedLock)")
    public void annotationPoinCut() {
    }

    @Around("annotationPoinCut()")
    public Object aroundMethod(ProceedingJoinPoint pdj) throws Exception {
        // 获取到注解传入的锁路径
        MethodSignature signature = (MethodSignature) pdj.getSignature();
        ZkDistributedLock annotation = signature.getMethod().getAnnotation(ZkDistributedLock.class);
        String lockPath = annotation.value();
        if (StringUtils.isEmpty(lockPath)) {
            throw CommonException.create(ServerResponse.createByError("@ZkDistributedLock注解方式使用分布式锁必须传入合法的lockPath"));
        }
        // result为连接点的放回结果
        Object result = null;
        String methodName = pdj.getSignature().getName();
        // 前置通知方法
        // 初始化锁
        log.info("前置通知方法 加锁{} >目标方法名：{},参数为：{}", lockPath, methodName, Arrays.asList(pdj.getArgs()));

        InterProcessMutex lock = new InterProcessMutex(zkClient.getClient(), lockPath);
        // 尝试获取锁资源
        lock.acquire();
        //执行目标方法
        try {
            result = pdj.proceed();
            // 返回通知方法
            log.info("返回通知方法>目标方法名:{},返回结果为:{}", methodName, result);
        } catch (Throwable e) {
            // 异常通知方法
            log.info("异常通知方法>目标方法名:{},异常为:{}", methodName, e);
        }
        // 后置通知
        log.info("后置通知方法 释放锁资源:{}>目标方法名{}", lockPath, methodName);
        // 释放锁资源
        lock.release();
        // 返回最终结果
        return result;
    }
}
