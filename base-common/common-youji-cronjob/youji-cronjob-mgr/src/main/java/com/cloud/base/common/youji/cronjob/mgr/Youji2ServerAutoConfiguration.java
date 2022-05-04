package com.cloud.base.common.youji.cronjob.mgr;


import com.cloud.base.common.youji.cronjob.mgr.entity.Youji2SchedulerEntity;
import com.cloud.base.common.youji.cronjob.mgr.properties.Youji2ServerProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.HashMap;

@Slf4j
@Configuration
@EnableConfigurationProperties(Youji2ServerProperties.class)
public class Youji2ServerAutoConfiguration {

    /**
     * 定时任务管理器
     *
     * @return
     */
    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.initialize();
        return threadPoolTaskScheduler;
    }

    /**
     * 当前节点定时任务节点
     *
     * @return
     */
    @Bean
    public HashMap<String, Youji2SchedulerEntity> schedulerEntityHashMap() {
        return new HashMap<>();
    }


}

