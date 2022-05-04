package com.cloud.base.common.youji.cronjob.mgr.component;

import com.alibaba.fastjson.JSON;
import com.cloud.base.common.core.response.ServerResponse;
import com.cloud.base.common.youji.cronjob.mgr.properties.Youji2ServerProperties;
import com.cloud.base.common.youji.cronjob.mgr.service.Youji2MgrService;
import okhttp3.Response;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author lh0811
 * @date 2022/4/17
 */
public class Youji2MgrEchoWorkComponent implements SchedulingConfigurer {


    @Autowired
    private Youji2MgrService youji2MgrService;

    @Autowired
    private Youji2ServerProperties properties;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.addTriggerTask(
                //1.添加任务内容(Runnable)
                () -> {
                    // 心跳检测
                    youji2MgrService.echoWorker();
                },
                //2.设置执行周期(Trigger)
                triggerContext -> {
                    // 返回执行周期(Date)
                    return new CronTrigger(properties.getHeartBeatCron()).nextExecutionTime(triggerContext);
                }
        );
    }
}
