package com.cloud.base.core.modules.youji.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cloud.base.core.common.util.IdWorker;
import com.cloud.base.core.modules.youji.code.exception.YouJiException;
import com.cloud.base.core.modules.youji.code.repository.dao.YoujiTaskExecLogDao;
import com.cloud.base.core.modules.youji.code.repository.entity.TaskInfo;
import com.cloud.base.core.modules.youji.code.repository.entity.TaskWorker;
import com.cloud.base.core.modules.youji.code.repository.entity.YoujiTaskExecLog;
import com.cloud.base.core.modules.youji.service.YouJiExceptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author lh0811
 * @date 2021/11/23
 */
@Component
public class YouJiExceptionServiceImpl implements YouJiExceptionService {

    @Autowired
    private YoujiTaskExecLogDao youjiTaskExecLogDao;

    @Autowired
    private IdWorker idWorker;


    /**
     * 记录日志
     *
     * @param exception
     */
    @Override
    public void logException(YouJiException exception) {
        TaskInfo taskInfo = exception.getTaskInfo();
        if (taskInfo == null) {
            taskInfo = new TaskInfo();
        }
        TaskWorker taskWorker = exception.getTaskWorker();
        if (taskWorker == null) {
            taskWorker = new TaskWorker();
        }
        // 1. 记录任务执行日志
        YoujiTaskExecLog execLog = new YoujiTaskExecLog();
        execLog.setId(idWorker.nextId());
        execLog.setTaskNo(taskInfo.getTaskNo());
        execLog.setTaskName(taskInfo.getTaskName());
        execLog.setWorkerId(taskWorker.getId());
        execLog.setWorkerIp(taskWorker.getWorkerIp());
        execLog.setWorkerPort(taskWorker.getWorkerPort());
        execLog.setTaskParam(taskInfo.getTaskParam());
        execLog.setContactsName(taskInfo.getContactsName());
        execLog.setContactsPhone(taskInfo.getContactsPhone());
        execLog.setContactsEmail(taskInfo.getContactsEmail());
        execLog.setFinishFlag(Boolean.FALSE);
        execLog.setResultMsg(exception.getErrMsg());
        execLog.setCreateTime(new Date());
        execLog.setUpdateTime(new Date());
        youjiTaskExecLogDao.save(execLog);
    }

}
