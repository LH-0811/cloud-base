package com.cloud.base.common.youji.cronjob.mgr.component;

import com.cloud.base.common.youji.cronjob.core.repository.entity.Youji2TaskInfo;
import com.cloud.base.common.youji.cronjob.mgr.entity.Youji2SchedulerEntity;
import com.cloud.base.common.youji.cronjob.mgr.properties.Youji2ServerProperties;
import com.cloud.base.common.youji.cronjob.mgr.service.Youji2ExceptionService;
import com.cloud.base.common.youji.cronjob.mgr.service.Youji2MgrService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

/**
 * @author lh0811
 * @date 2022/4/17
 */
@Slf4j
@Order(value = Integer.MAX_VALUE)
@Component
public class Youji2MgrTaskInitComponent implements CommandLineRunner {

    @Autowired
    private Youji2ServerProperties properties;

    @Value("${server.port:8080}")
    private Integer port;

    @Autowired
    private Youji2MgrService youji2MgrService;

    @Autowired
    private Youji2ExceptionService youji2ExceptionService;

    @Autowired
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    // 任务容器
    @Autowired
    private HashMap<String, Youji2SchedulerEntity> schedulerEntityHashMap;

    @Override
    public void run(String... args) throws Exception {
        log.info("[酉鸡2] 开始初始化定时任务到管理节点中 当前节点{}:{}-{}", properties.getMgrIp(), port, properties.getMgrIndex());

        List<Youji2TaskInfo> taskInfoList = youji2MgrService.getAllTaskInfo();
        for (Youji2TaskInfo taskInfo : taskInfoList) {
            Youji2SchedulerEntity schedulerEntity = new Youji2SchedulerEntity();
            schedulerEntity.setTaskNo(taskInfo.getTaskNo());
            schedulerEntity.setTaskInfo(taskInfo);
            ScheduledFuture<?> schedule = threadPoolTaskScheduler.schedule(new Youji2MgrTaskSendComponent(taskInfo.getTaskNo(), youji2MgrService, youji2ExceptionService), new CronTrigger(taskInfo.getCorn()));
            schedulerEntity.setFuture(schedule);
            schedulerEntityHashMap.put(taskInfo.getTaskNo(), schedulerEntity);
        }
        log.info("[酉鸡2 初始化定时任务到管理节点中] YouJiSchedulerTaskInit 完成");
    }


    // 保存定时任务修改并立即生效启动定时任务
    public void saveTask(Youji2TaskInfo taskInfo) {
        // 获取原任务的执行计划
        Youji2SchedulerEntity schedulerEntity = schedulerEntityHashMap.get(taskInfo.getTaskNo());

        // 先中断之前的定时任务
        if (schedulerEntity == null) {
            schedulerEntity = new Youji2SchedulerEntity();
        } else {
            if (!schedulerEntity.getFuture().isCancelled()) {
                // 取消定时任务 如果执行中是否中断
                schedulerEntity.getFuture().cancel(true);
            }
        }

        // 覆盖原定时任务执行计划
        schedulerEntity.setTaskNo(taskInfo.getTaskNo());
        schedulerEntity.setTaskInfo(taskInfo);
        ScheduledFuture<?> schedule = threadPoolTaskScheduler.schedule(new Youji2MgrTaskSendComponent(taskInfo.getTaskNo(), youji2MgrService, youji2ExceptionService), new CronTrigger(taskInfo.getCorn()));
        schedulerEntity.setFuture(schedule);
        schedulerEntityHashMap.put(taskInfo.getTaskNo(), schedulerEntity);
    }
}
