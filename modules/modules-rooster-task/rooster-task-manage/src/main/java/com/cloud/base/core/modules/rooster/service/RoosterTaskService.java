package com.cloud.base.core.modules.rooster.service;

import com.cloud.base.core.modules.rooster.code.param.RoosterTaskCreateParam;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;

/**
 * @author lh0811
 * @date 2021/11/14
 */
public interface RoosterTaskService {


    /**
     * 注册定时任务
     *
     * @param param
     * @throws Exception
     */
    void registerWorker(@Valid RoosterTaskCreateParam param, String workerHost, Integer workerPort) throws Exception;

    /**
     * 客户端心跳检测
     *
     * @throws Exception
     */
    void heartBeatCheckWorker() ;
}
