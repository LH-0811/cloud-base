package com.cloud.base.core.modules.zk.distributed.function.counter;

import com.cloud.base.core.modules.zk.distributed.client.ZkDistributedClient;
import com.cloud.base.core.modules.zk.distributed.properites.ZkDistributedProperties;
import org.apache.curator.framework.recipes.atomic.DistributedAtomicInteger;
import org.apache.curator.retry.RetryNTimes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * 获取分布式计数器
 *
 * @author lh0811
 * @date 2021/4/7
 */
@Component
public class ZkDistributedCounter {

    @Autowired
    private ZkDistributedClient zkClient;

    @Autowired
    private ZkDistributedProperties properties;

    // 计数器集合 如果已经声明过的path 就直接从map里面拿出来 算了 好像没必要
//    private HashMap<String,DistributedAtomicInteger> pathAtomicMap = new HashMap<>();

    /**
     * 使用zk path 获取一个分布式计数器
     *
     * @param path zkNode 的path地址
     * @return
     * @throws Exception
     */
    public DistributedAtomicInteger getAtomicInteger(String path) throws Exception {
        return new DistributedAtomicInteger(zkClient.getClient(),path,new RetryNTimes(properties.getMaxRetries(),properties.getBaseSleepTimeMs()));
    }


}
