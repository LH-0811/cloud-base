package com.cloud.base.core.modules.youji.config;

import com.cloud.base.core.modules.youji.properties.YouJiServerProperties;
import com.cloud.base.core.modules.youji.service.YouJiManageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;

/**
 * @author lh0811
 * @date 2021/10/8
 */
@Slf4j
@EnableScheduling
@Configuration
public class YouJiHeartBeatSchedulerConfig implements SchedulingConfigurer {

    @Autowired
    private YouJiServerProperties properties;

    @Autowired
    private YouJiManageService youJiManageService;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.addTriggerTask(
                //1.添加任务内容(Runnable)
                () -> {
                    // 心跳检测
                    youJiManageService.heartBeatCheckWorker();
                },
                //2.设置执行周期(Trigger)
                triggerContext -> {
                    // 返回执行周期(Date)
                    return new CronTrigger(properties.getHeartBeatCorn()).nextExecutionTime(triggerContext);
                }
        );
    }

}
