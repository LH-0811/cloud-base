package com.cloud.base.core.modules.zk.distributed.function.barrier;

/**
 * @author lh0811
 * @date 2021/4/9
 */
public @interface DistributedDoubleBarrier {
    // 默认分布式栅栏zkNode的路径
    String value() default "/cloud_base/zk_distributed_double_barrier_default";

    // 准备线程数
    int threadNum();
}
