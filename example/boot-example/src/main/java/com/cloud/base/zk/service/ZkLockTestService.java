package com.cloud.base.zk.service;

import com.cloud.base.core.modules.zk.distributed.function.lock.ZkDistributedLock;

/**
 * @author lh0811
 * @date 2021/4/6
 */
public interface ZkLockTestService {
    String testZkClient(String name) throws Exception;

    String testZkClient1(String name) throws Exception;

    String testZkClient2(String name) throws Exception;

    @ZkDistributedLock("/cloud_base/zk_distributed_lock_test3")
    String testZkClient3(String name) throws Exception;

    @ZkDistributedLock("/cloud_base/zk_distributed_lock_test4")
    String testZkClient4(String name) throws Exception;
}
