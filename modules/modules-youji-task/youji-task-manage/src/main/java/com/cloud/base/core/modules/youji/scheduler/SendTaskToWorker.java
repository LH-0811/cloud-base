package com.cloud.base.core.modules.youji.scheduler;

import com.cloud.base.core.modules.youji.service.YouJiManageService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SendTaskToWorker implements Runnable {

    private YouJiManageService youJiManageService;
    private String taskNo;

    public SendTaskToWorker(String taskNo, YouJiManageService youJiManageService) {
        this.taskNo = taskNo;
        this.youJiManageService = youJiManageService;
    }

    @Override
    public void run() {
        log.info("[酉鸡 Manage向Worker发起任务] taskNo:{}", taskNo);
        youJiManageService.executeTask(taskNo);
    }


}
