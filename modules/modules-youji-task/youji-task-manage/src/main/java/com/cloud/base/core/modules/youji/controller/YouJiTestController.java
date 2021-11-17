package com.cloud.base.core.modules.youji.controller;

import com.cloud.base.core.common.response.ServerResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.concurrent.ScheduledFuture;

/**
 * @author lh0811
 * @date 2021/11/15
 */
@Slf4j
@Api(tags = "酉鸡-任务管理接口")
@RequestMapping("/youji/task/test")
@RestController
public class YouJiTestController {


    @Autowired
    private ThreadPoolTaskScheduler threadPoolTaskScheduler ;

    private ScheduledFuture<?> future;

    private String cron = "0/5 * * * * *";

    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        return new ThreadPoolTaskScheduler();
    }

    @GetMapping("/start_cron")
    @ApiOperation("开始定时任务")
    public ServerResponse startCron() {
        future = threadPoolTaskScheduler.schedule(new YouJiTestController.MyRunnable(), new CronTrigger(cron));
        log.info("开始定时任务:{}", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        return ServerResponse.createBySuccess("success");
    }


    @GetMapping("/stop_cron")
    @ApiOperation("停止定时任务")
    public ServerResponse stopCron() {
        if (future != null) {
            future.cancel(true);
        }
        log.info("停止定时任务:{}", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        return ServerResponse.createBySuccess("success");
    }


    @GetMapping("/change_cron")
    @ApiOperation("停止定时任务")
    public ServerResponse setCron() {
        this.cron = "0/10 * * * * *";
        // 先停止
        stopCron();
        // 再开始
        startCron();
        return ServerResponse.createBySuccess("success");
    }


    private class MyRunnable implements Runnable {
        @Override
        public void run() {
            System.out.println("定时任务执行: {}" + new Date());
        }
    }
}
