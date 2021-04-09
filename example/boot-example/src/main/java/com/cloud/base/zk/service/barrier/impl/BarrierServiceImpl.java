package com.cloud.base.zk.service.barrier.impl;

import com.cloud.base.core.modules.zk.distributed.function.barrier.DistributedBarrier;
import com.cloud.base.core.modules.zk.distributed.function.barrier.DistributedDoubleBarrier;
import com.cloud.base.zk.service.barrier.BarrierService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author lh0811
 * @date 2021/4/9
 */
@Slf4j
@Component("barrierService")
public class BarrierServiceImpl implements BarrierService {

    @Override
    @DistributedBarrier
    public void barrier(String str) throws Exception {
        log.info("进入barrier方法:{}",str);
    }

    @Override
    @DistributedDoubleBarrier(threadNum = 3)
    public void doubleBarrier(String str) throws Exception {
        log.info("进入doubleBarrier方法:{}",str);
    }

}
