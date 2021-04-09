package com.cloud.base.core.modules.zk.distributed.function.barrier;

import com.cloud.base.core.modules.zk.distributed.client.ZkDistributedClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author lh0811
 * @date 2021/4/9
 */
@Component
public class DistributedBarrierEngine {

    @Autowired
    private ZkDistributedClient zkClient;

    /**
     * 释放栅栏
     * DistributedDoubleBarrier
     * @param barrierPath
     * @throws Exception
     */
    public void removeBarrier(String barrierPath) throws Exception {
        org.apache.curator.framework.recipes.barriers.DistributedBarrier barrier = new org.apache.curator.framework.recipes.barriers.DistributedBarrier(zkClient.getClient(), barrierPath);
        barrier.removeBarrier();
    }


}
