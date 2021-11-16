package com.cloud.base.core.modules.youji.service;

import com.cloud.base.core.modules.youji.code.param.YouJiTaskCreateParam;

import javax.validation.Valid;

/**
 * @author lh0811
 * @date 2021/11/14
 */
public interface YouJiTaskService {


    /**
     * 注册定时任务
     *
     * @param param
     * @throws Exception
     */
    void registerWorker(@Valid YouJiTaskCreateParam param, String workerHost, Integer workerPort) throws Exception;

    /**
     * 客户端心跳检测
     *
     * @throws Exception
     */
    void heartBeatCheckWorker() ;
}
