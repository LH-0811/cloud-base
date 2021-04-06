package com.cloud.base.zk.service;

import com.cloud.base.core.modules.zk.distributed.function.lock.ZkDistributedLock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author lh0811
 * @date 2021/4/6
 */
@Slf4j
@Service("zkLockTestService")
public class ZkLockTestServiceImpl implements ZkLockTestService {


    @Override
    @ZkDistributedLock
    public String testZkClient(String name) throws Exception {
        log.info("开始执行zk锁测试方法:{}",name);
        log.info("执行中。。。。:{}",name);
        // 线程休眠 模拟耗时操作
        Thread.sleep(10000);
        log.info("执行完成:{}",name);
        return "success";
    }

}
