package com.cloud.base.core.modules.zk.distributed.function.lock;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * zk分布式锁注解
 *
 * @author lh0811
 * @date 2021/4/6
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ZkDistributedLock {

    /**
     * 参数输入 输入一个zk锁的路径 以下是默认路径
     * 锁路径建议： 工程名/业务名/锁名称
     *
     * @return
     */
    String value() default "/cloud_base/zk_distributed_lock_default";

}
