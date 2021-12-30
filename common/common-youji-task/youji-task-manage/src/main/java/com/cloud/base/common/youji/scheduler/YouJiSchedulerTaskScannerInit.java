package com.cloud.base.common.youji.scheduler;

import com.cloud.base.common.youji.code.repository.entity.TaskInfo;
import com.cloud.base.common.youji.scheduler.entity.YouJiSchedulerEntity;
import com.cloud.base.common.youji.service.YouJiExceptionService;
import com.cloud.base.common.youji.service.YouJiManageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

/**
 * 酉鸡 定时任务初始化组件
 */
@Slf4j
@Order(value = Integer.MAX_VALUE)
@Component
public class YouJiSchedulerTaskScannerInit implements CommandLineRunner {


    @Autowired
    private YouJiManageService youJiManageService;

    private ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
    // 任务容器
    private HashMap<String, YouJiSchedulerEntity> schedulerEntityHashMap = new HashMap<>();

    @Autowired
    private YouJiExceptionService youJiExceptionService;

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
            ScheduledFuture<?> schedule = threadPoolTaskScheduler.schedule(new SendTaskToWorkerComponent(taskInfo.getTaskNo(), youJiManageService,youJiExceptionService), new CronTrigger(taskInfo.getCorn()));
            schedulerEntity.setFuture(schedule);
            schedulerEntityHashMap.put(taskInfo.getTaskNo(), schedulerEntity);
        }
        log.info("[酉鸡 服务Manage初始化定时任务] YouJiSchedulerTaskInit 完成");
    }

    // 保存定时任务修改并立即生效启动定时任务
    public void saveTask(TaskInfo taskInfo) {
        // 获取原任务的执行计划
        YouJiSchedulerEntity schedulerEntity = schedulerEntityHashMap.get(taskInfo.getTaskNo());
        // 先中断之前的定时任务
        if (schedulerEntity == null) {
            schedulerEntity = new YouJiSchedulerEntity();
        } else {
            if (!schedulerEntity.getFuture().isCancelled()) {
                // 取消定时任务 如果执行中是否中断
                schedulerEntity.getFuture().cancel(true);
            }
        }
        // 如果该任务是可用状态 需要直接停用启动
        if (taskInfo.getEnableFlag()) {
            // 覆盖原定时任务执行计划
            schedulerEntity.setTaskNo(taskInfo.getTaskNo());
            schedulerEntity.setTaskInfo(taskInfo);
            ScheduledFuture<?> schedule = threadPoolTaskScheduler.schedule(new SendTaskToWorkerComponent(taskInfo.getTaskNo(), youJiManageService, youJiExceptionService), new CronTrigger(taskInfo.getCorn()));
            schedulerEntity.setFuture(schedule);
            schedulerEntityHashMap.put(taskInfo.getTaskNo(), schedulerEntity);
        } else {
            // 如果是停用状态
            schedulerEntityHashMap.remove(taskInfo.getTaskNo());
        }
    }
}
