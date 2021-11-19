package com.cloud.base.core.modules.youji.scheduler;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

import com.cloud.base.core.modules.youji.code.repository.entity.TaskInfo;
import com.cloud.base.core.modules.youji.code.util.YouJiOkHttpClientUtil;
import com.cloud.base.core.modules.youji.service.YouJiManageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

/**
 * 酉鸡 定时任务初始化组件
 */
@Slf4j
@Order(value = Integer.MAX_VALUE)
@Component
public class YouJiSchedulerTaskInit implements CommandLineRunner {


    @Autowired
    private YouJiManageService youJiManageService;
    @Autowired
    private YouJiOkHttpClientUtil httpClientUtil;

    private ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
    // 任务容器
    private HashMap<String, YouJiSchedulerEntity> schedulerEntityHashMap = new HashMap<>();

    @Override
    public void run(String... args) throws Exception {
        log.info("[酉鸡 服务Manage初始化定时任务] YouJiSchedulerTaskInit 开始");
        // 初始化线程池
        threadPoolTaskScheduler.initialize();
        List<TaskInfo> taskInfoList = youJiManageService.getAllEnableTaskInfo();
        for (TaskInfo taskInfo : taskInfoList) {
            YouJiSchedulerEntity schedulerEntity = new YouJiSchedulerEntity();
            schedulerEntity.setTaskNo(taskInfo.getTaskNo());
            schedulerEntity.setTaskInfo(taskInfo);
            ScheduledFuture<?> schedule = threadPoolTaskScheduler.schedule(new SendTaskToWorker(taskInfo, youJiManageService,httpClientUtil), new CronTrigger(taskInfo.getCorn()));
            schedulerEntity.setFuture(schedule);
            schedulerEntityHashMap.put(taskInfo.getTaskNo(), schedulerEntity);
        }
        log.info("[酉鸡 服务Manage初始化定时任务] YouJiSchedulerTaskInit 完成");
    }

    public HashMap<String, YouJiSchedulerEntity> getSchedulerEntityHashMap() {
        return schedulerEntityHashMap;
    }

    public ThreadPoolTaskScheduler getThreadPoolTaskScheduler() {
        return threadPoolTaskScheduler;
    }
}
