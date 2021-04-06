package com.cloud.base.core.modules.zk.distributed.function;

import com.cloud.base.core.modules.zk.distributed.client.ZkDistributedClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author lh0811
 * @date 2021/4/6
 */
@Slf4j
@Component
public class ZkDistributedFunc {

    @Autowired
    private ZkDistributedClient zkClient;


    public void DistributedLock(String lockPath) throws Exception {
        log.info("开始进入分布式锁服务");


    }


}
