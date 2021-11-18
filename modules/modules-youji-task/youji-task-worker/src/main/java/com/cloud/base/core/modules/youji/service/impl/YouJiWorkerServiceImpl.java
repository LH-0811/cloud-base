package com.cloud.base.core.modules.youji.service.impl;

import com.cloud.base.core.common.response.ServerResponse;
import com.cloud.base.core.common.util.IdWorker;
import com.cloud.base.core.modules.youji.code.param.YouJiWorkerReceiveTaskParam;
import com.cloud.base.core.modules.youji.code.repository.dao.TaskInfoDao;
import com.cloud.base.core.modules.youji.code.repository.dao.TaskWorkerDao;
import com.cloud.base.core.modules.youji.code.repository.dao.YoujiTaskExecLogDao;
import com.cloud.base.core.modules.youji.code.repository.entity.TaskWorker;
import com.cloud.base.core.modules.youji.code.repository.entity.YoujiTaskExecLog;
import com.cloud.base.core.modules.youji.controller.YouJiWorkerEchoController;
import com.cloud.base.core.modules.youji.properties.YouJiWorkerProperties;
import com.cloud.base.core.modules.youji.service.YouJiWorkerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author lh0811
 * @date 2021/11/17
 */
@Slf4j
@Service
public class YouJiWorkerServiceImpl implements YouJiWorkerService {


    @Autowired
    private TaskInfoDao taskInfoDao;

    @Autowired
    private TaskWorkerDao taskWorkerDao;

    @Autowired
    private YoujiTaskExecLogDao youjiTaskExecLogDao;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private YouJiWorkerProperties properties;


    /**
     * 工作节点完成任务后的处理逻辑
     *
     * @param param
     * @throws Exception
     */
    @Override
    public void finishTask(YouJiWorkerReceiveTaskParam param, ServerResponse serverResponse) throws Exception {

        // 1. 记录任务执行日志
        YoujiTaskExecLog execLog = new YoujiTaskExecLog();
        execLog.setId(idWorker.nextId());
        execLog.setTaskNo(param.getTaskNo());
        execLog.setTaskName(param.getTaskName());
        execLog.setWorkerId(param.getWorkerId());
        execLog.setCorn(param.getCorn());
        execLog.setTaskUrl(param.getTaskUrl());
        execLog.setTaskBeanName(param.getTaskBeanName());
        execLog.setTaskMethod(param.getTaskMethod());
        execLog.setTaskParam(param.getTaskParam());
        execLog.setContactsName(param.getContactsName());
        execLog.setContactsPhone(param.getContactsPhone());
        execLog.setContactsEmail(param.getContactsEmail());
        execLog.setFinishFlag(serverResponse.isSuccess());
        execLog.setResultMsg(serverResponse.getMsg());
        execLog.setCreateTime(new Date());
        execLog.setUpdateTime(new Date());
        youjiTaskExecLogDao.save(execLog);
        // 2. 修改worker信息
        TaskWorker taskWorker = taskWorkerDao.getById(param.getWorkerId());
        TaskWorker workerUpdateInfo = new TaskWorker();
        workerUpdateInfo.setId(taskWorker.getId());
        workerUpdateInfo.setExecTaskNum(taskWorker.getExecTaskNum() == null ? 1 : taskWorker.getExecTaskNum() + 1);
        workerUpdateInfo.setLastExecTime(new Date());
        taskWorkerDao.updateById(workerUpdateInfo);

    }


}
