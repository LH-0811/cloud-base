package com.cloud.base.zk.service.barrier;

import com.cloud.base.core.modules.zk.distributed.function.barrier.DistributedBarrier;
import com.cloud.base.core.modules.zk.distributed.function.barrier.DistributedDoubleBarrier;

/**
 * @author lh0811
 * @date 2021/4/9
 */
public interface BarrierService {
    void barrier(String str) throws Exception;
    void doubleBarrier(String str) throws Exception;
}
