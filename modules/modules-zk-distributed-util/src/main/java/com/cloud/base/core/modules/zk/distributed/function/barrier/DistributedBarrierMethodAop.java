package com.cloud.base.core.modules.zk.distributed.function.barrier;

import com.cloud.base.core.common.entity.CommonMethod;
import com.cloud.base.core.common.entity.ServerResponse;
import com.cloud.base.core.common.exception.CommonException;
import com.cloud.base.core.modules.zk.distributed.client.ZkDistributedClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * @author lh0811
 * @date 2021/4/9
 */
@Slf4j
@Aspect
@Component
public class DistributedBarrierMethodAop {

    @Autowired
    private ZkDistributedClient zkClient;

    @Pointcut("@annotation(com.cloud.base.core.modules.zk.distributed.function.barrier.DistributedBarrier)")
    public void distributedBarrierPoinCut() {
    }

    @Pointcut("@annotation(com.cloud.base.core.modules.zk.distributed.function.barrier.DistributedDoubleBarrier)")
    public void distributedDoubleBarrierPoinCut() {
    }

    @Around("distributedDoubleBarrierPoinCut()")
    public Object distributedDoubleBarrierPoinCut(ProceedingJoinPoint pdj) throws Exception {
        // 获取到注解传入的锁路径
        MethodSignature signature = (MethodSignature) pdj.getSignature();
        DistributedDoubleBarrier annotation = signature.getMethod().getAnnotation(DistributedDoubleBarrier.class);
        String lockPath = annotation.value();
        int threadNum = annotation.threadNum();
        if (StringUtils.isEmpty(lockPath)) {
            throw CommonException.create(ServerResponse.createByError("@DistributedBarrier注解方式使用分布式锁必须传入合法的lockPath"));
        }
        // result为连接点的放回结果
        Object result = null;
        String methodName = pdj.getSignature().getName();
        // 前置通知方法
        // 初始化锁
        log.info("前置通知方法 进入栅栏{} >目标方法名：{},参数为：{}", lockPath, methodName, Arrays.asList(pdj.getArgs()));
        //设置Barrier
        org.apache.curator.framework.recipes.barriers.DistributedDoubleBarrier  barrier = new org.apache.curator.framework.recipes.barriers.DistributedDoubleBarrier(zkClient.getClient(), lockPath, threadNum);
        barrier.enter();
        //执行目标方法
        try {
            result = pdj.proceed();
        } catch (Throwable e) {
            throw CommonException.create(ServerResponse.createByError("DistributedBarrier 切点异常:{}", CommonMethod.getTrace(e)));
        }
        barrier.leave();
        // 后置通知
        log.info("后置通知方法 释放栅栏:{}>目标方法名{}", lockPath, methodName);
        // 返回最终结果
        return result;

    }


    @Around("distributedBarrierPoinCut()")
    public Object distributedBarrierPoinCut(ProceedingJoinPoint pdj) throws Exception {
        // 获取到注解传入的锁路径
        MethodSignature signature = (MethodSignature) pdj.getSignature();
        DistributedBarrier annotation = signature.getMethod().getAnnotation(DistributedBarrier.class);
        String lockPath = annotation.value();
        if (StringUtils.isEmpty(lockPath)) {
            throw CommonException.create(ServerResponse.createByError("@DistributedBarrier注解方式使用分布式锁必须传入合法的lockPath"));
        }
        // result为连接点的放回结果
        Object result = null;
        String methodName = pdj.getSignature().getName();
        // 前置通知方法
        // 初始化锁
        log.info("前置通知方法 进入栅栏{} >目标方法名：{},参数为：{}", lockPath, methodName, Arrays.asList(pdj.getArgs()));

        org.apache.curator.framework.recipes.barriers.DistributedBarrier barrier = new org.apache.curator.framework.recipes.barriers.DistributedBarrier(zkClient.getClient(), lockPath);
        // 尝试获取锁资源
        barrier.setBarrier();
        barrier.waitOnBarrier();

        //执行目标方法
        try {
            result = pdj.proceed();
        } catch (Throwable e) {
            throw CommonException.create(ServerResponse.createByError("DistributedBarrier 切点异常:{}", CommonMethod.getTrace(e)));
        }
        // 后置通知
        log.info("后置通知方法 释放栅栏:{}>目标方法名{}", lockPath, methodName);
        // 返回最终结果
        return result;
    }

}
