package com.cloud.base.core.modules.youji.service;

import com.cloud.base.core.modules.youji.code.param.YouJiWorkerRegisterTaskParam;
import com.cloud.base.core.modules.youji.code.repository.entity.TaskInfo;
import com.cloud.base.core.modules.youji.code.repository.entity.TaskWorker;

import javax.validation.Valid;
import java.util.List;

/**
 * @author lh0811
 * @date 2021/11/14
 */
public interface YouJiManageService {


    /**
     * 注册定时任务
     *
     * @param param
     * @throws Exception
     */
    void registerWorker(@Valid YouJiWorkerRegisterTaskParam param, String workerHost, Integer workerPort) throws Exception;

    /**
     * 客户端心跳检测
     *
     * @throws Exception
     */
    void heartBeatCheckWorker() ;

    /**
     * 获取到全部（可用）定时任务
     * @return
     */
    List<TaskInfo> getAllEnableTaskInfo();

    /**
     * 获取到 一个工作接单来执行定时任务
     *
     * @param taskInfo
     * @return
     */
    TaskWorker getSingleNode(TaskInfo taskInfo);

    /**
     * 获取到全部的工作节点
     *
     * @param taskInfo
     * @return
     */
    List<TaskWorker> getAllNode(TaskInfo taskInfo);
}
