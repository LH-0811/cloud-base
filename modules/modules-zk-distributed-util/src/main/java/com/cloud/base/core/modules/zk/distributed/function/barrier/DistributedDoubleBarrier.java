package com.cloud.base.core.modules.zk.distributed.function.barrier;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author lh0811
 * @date 2021/4/9
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DistributedDoubleBarrier {
    // 默认分布式栅栏zkNode的路径
    String value() default "/cloud_base/zk_distributed_double_barrier_default";

    // 准备线程数
    int threadNum();
}
