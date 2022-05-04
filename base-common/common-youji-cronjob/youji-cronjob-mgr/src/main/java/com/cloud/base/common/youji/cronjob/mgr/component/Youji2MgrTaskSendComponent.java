package com.cloud.base.common.youji.cronjob.mgr.component;

import com.cloud.base.common.youji.cronjob.core.exception.Youji2Exception;
import com.cloud.base.common.youji.cronjob.mgr.properties.Youji2ServerProperties;
import com.cloud.base.common.youji.cronjob.mgr.service.Youji2ExceptionService;
import com.cloud.base.common.youji.cronjob.mgr.service.Youji2MgrService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Youji2MgrTaskSendComponent implements Runnable {

    private String taskNo;

    private Youji2MgrService youji2MgrService;

    private Youji2ExceptionService youji2ExceptionService;


    public Youji2MgrTaskSendComponent(String taskNo, Youji2MgrService youJiManageService, Youji2ExceptionService youJiExceptionService) {
        this.taskNo = taskNo;
        this.youji2MgrService = youJiManageService;
        this.youji2ExceptionService = youJiExceptionService;
    }

    @Override
    public void run() {
        log.info("[酉鸡 Manage向Worker发起任务] taskNo:{}", taskNo);
        try {
            youji2MgrService.executeTask(taskNo);
        } catch (Exception e) {
            if (e instanceof Youji2Exception) {
                youji2ExceptionService.logException((Youji2Exception) e);
            }
        }

    }


}
