package com.cloud.base.common.youji.scheduler;

import com.cloud.base.common.youji.code.exception.YouJiException;
import com.cloud.base.common.youji.service.YouJiExceptionService;
import com.cloud.base.common.youji.service.YouJiManageService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SendTaskToWorkerComponent implements Runnable {

    private YouJiManageService youJiManageService;
    private String taskNo;
    private YouJiExceptionService youJiExceptionService;

    public SendTaskToWorkerComponent(String taskNo, YouJiManageService youJiManageService, YouJiExceptionService youJiExceptionService) {
        this.taskNo = taskNo;
        this.youJiManageService = youJiManageService;
        this.youJiExceptionService = youJiExceptionService;
    }

    @Override
    public void run() {
        log.info("[酉鸡 Manage向Worker发起任务] taskNo:{}", taskNo);
        try {
            youJiManageService.executeTask(taskNo);
        } catch (Exception e) {
            if (e instanceof YouJiException) {
                youJiExceptionService.logException((YouJiException) e);
            }
        }

    }


}