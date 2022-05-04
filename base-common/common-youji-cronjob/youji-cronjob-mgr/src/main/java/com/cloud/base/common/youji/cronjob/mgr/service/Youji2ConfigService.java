package com.cloud.base.common.youji.cronjob.mgr.service;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author lh0811
 * @date 2022/4/15
 */
public interface Youji2ConfigService {


    /**
     *  根据key获取锁
     * @param key
     * @return
     */
    Boolean getLockForKey(String key);

    /**
     * 根据key释放锁
     */
    void releaseLockForKey(String key);
}
