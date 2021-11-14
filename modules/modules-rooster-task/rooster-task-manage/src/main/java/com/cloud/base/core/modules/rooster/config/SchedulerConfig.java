package com.cloud.base.core.modules.rooster.config;

import com.cloud.base.core.modules.rooster.properties.RoosterServerProperties;
import com.cloud.base.core.modules.rooster.service.RoosterTaskService;
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
public class SchedulerConfig implements SchedulingConfigurer {

    @Autowired
    private RoosterServerProperties properties;

    @Autowired
    private RoosterTaskService roosterTaskService;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.addTriggerTask(
                //1.添加任务内容(Runnable)
                () -> {
                    // 心跳检测
                    roosterTaskService.heartBeatCheckWorker();
                },
                //2.设置执行周期(Trigger)
                triggerContext -> {
                    // 返回执行周期(Date) 每天上午4点触发一次
                    return new CronTrigger(properties.getHeartBeatCorn()).nextExecutionTime(triggerContext);
                }
        );
    }

}
