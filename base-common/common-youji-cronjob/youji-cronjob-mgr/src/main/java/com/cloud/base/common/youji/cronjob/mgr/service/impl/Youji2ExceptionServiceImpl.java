package com.cloud.base.common.youji.cronjob.mgr.service.impl;

import com.cloud.base.common.core.util.IdWorker;
import com.cloud.base.common.youji.cronjob.core.exception.Youji2Exception;
import com.cloud.base.common.youji.cronjob.core.repository.dao.Youji2TaskExecLogDao;
import com.cloud.base.common.youji.cronjob.core.repository.entity.Youji2TaskExecLog;
import com.cloud.base.common.youji.cronjob.core.repository.entity.Youji2TaskInfo;
import com.cloud.base.common.youji.cronjob.core.repository.entity.Youji2TaskWorker;
import com.cloud.base.common.youji.cronjob.mgr.service.Youji2ExceptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author lh0811
 * @date 2021/11/23
 */
@Component
public class Youji2ExceptionServiceImpl implements Youji2ExceptionService {

    @Autowired
    private Youji2TaskExecLogDao youji2TaskExecLogDao;

    @Autowired
    private IdWorker idWorker;


    /**
     * 记录日志
     *
     * @param exception
     */
    @Override
    public void logException(Youji2Exception exception) {
        Youji2TaskInfo taskInfo = exception.getTaskInfo();
        if (taskInfo == null) {
            taskInfo = new Youji2TaskInfo();
        }
        Youji2TaskWorker taskWorker = exception.getTaskWorker();
        if (taskWorker == null) {
            taskWorker = new Youji2TaskWorker();
        }
        // 1. 记录任务执行日志
        Youji2TaskExecLog execLog = new Youji2TaskExecLog();
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
        execLog.setResultMsg(exception.getErrMsg()+":"+exception.getDescStr());
        execLog.setCreateTime(new Date());
        execLog.setUpdateTime(new Date());
        youji2TaskExecLogDao.save(execLog);
    }

}
