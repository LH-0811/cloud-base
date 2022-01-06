package com.cloud.base.user.service;

/**
 * @author lh0811
 * @date 2021/12/8
 */
public interface SysSerialService {
    /**
     * 获取下一个业务序号
     * 为了避免并发获取到重复的序号，必须要求在数据库事务级别串行处理该事务。
     *
     * @param bizType 业务类型
     * @return
     * @throws Exception
     */
    String getNextNumByBizType(String bizType) throws Exception;
}
