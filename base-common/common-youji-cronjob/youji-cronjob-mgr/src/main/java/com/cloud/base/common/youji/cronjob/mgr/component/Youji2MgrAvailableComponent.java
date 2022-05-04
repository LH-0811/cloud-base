package com.cloud.base.common.youji.cronjob.mgr.component;

import com.alibaba.fastjson.JSON;
import com.cloud.base.common.core.response.ServerResponse;
import com.cloud.base.common.youji.cronjob.core.repository.entity.Youji2TaskMgr;
import com.cloud.base.common.youji.cronjob.core.util.YouJi2OkHttpClientUtil;
import com.cloud.base.common.youji.cronjob.mgr.properties.Youji2ServerProperties;
import com.cloud.base.common.youji.cronjob.mgr.service.Youji2MgrService;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lh0811
 * @date 2022/4/16
 */
@Slf4j
@EnableScheduling
@Configuration
public class Youji2MgrAvailableComponent implements SchedulingConfigurer {

    @Autowired
    private Youji2ServerProperties properties;

    @Value("${server.port:8080}")
    private Integer port;

    @Autowired
    private Youji2MgrService youji2MgrService;

    @Autowired
    private YouJi2OkHttpClientUtil httpClient;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.addTriggerTask(
                //1.添加任务内容(Runnable)
                () -> {
                    // 心跳检测
                    try {
                        log.info("[酉鸡 心跳检测] 当前节点host:{},port:{}",properties.getMgrIp(),port);
                        echo();
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.error("[酉鸡 管理节点心跳检查失败]  Error {}:{}", properties.getMgrIp(),port);
                    }
                },
                //2.设置执行周期(Trigger)
                triggerContext -> {
                    // 返回执行周期(Date)
                    return new CronTrigger(properties.getHeartBeatCron()).nextExecutionTime(triggerContext);
                }
        );
    }

    // echo 心跳保活
    private void echo() throws Exception {
        log.info("[酉鸡2 当前节点{}:{} echo监听节点] 开始echo",properties.getMgrIp(),port);
        // 获取到自身当前节点
        Youji2TaskMgr taskMgrByIndex = youji2MgrService.getTaskMgrByIndex(properties.getMgrIndex());
        if (taskMgrByIndex == null) {
            log.info("[酉鸡2 当前节点{}:{} index={} 不存在 echo 结束] 开始echo",properties.getMgrIp(),port,properties.getMgrIndex());
            return;
        }

        // 获取echo的管理节点
        Integer pingMgrIndex = taskMgrByIndex.getPingMgrIndex();
        if (pingMgrIndex == null) {
            log.info("[酉鸡2 当前节点{}:{} 对应的echo节点index={} 不存在 echo 结束] 开始echo",properties.getMgrIp(),port,pingMgrIndex);
            return;
        }
        Youji2TaskMgr echoMgr = youji2MgrService.getTaskMgrByIndex(pingMgrIndex);
        if (echoMgr == null) {
            log.info("[酉鸡2 当前节点{}:{} 对应的echo节点index={} 不存在 echo 结束] 开始echo",properties.getMgrIp(),port,pingMgrIndex);
            return;
        }
        try {
            // 发起echo
            Response response = httpClient.syncGet("http://" + echoMgr.getMgrIp() + ":" + echoMgr.getMgrPort() + "/youji/mgr/echo");
            if (response.code() == 200) { // http 请求响应200
                ServerResponse serverResponse = JSON.parseObject(response.body().string(), ServerResponse.class);
                if (serverResponse.isSuccess()) { // 业务返回 处理成功
                    // echo 成功
                    // 更新被echo节点的数据
                    youji2MgrService.echoSuccess(echoMgr);
                    return;
                }
            }
            log.info("[酉鸡2 当前节点{}:{} ---> {}:{}] echo失败:{}",properties.getMgrIp(),port,echoMgr.getMgrIp(),echoMgr.getMgrPort(),response.toString());
        } catch (Exception e) {
            log.info("[酉鸡2 当前节点{}:{} ---> {}:{}] echo失败:{}",properties.getMgrIp(),port,echoMgr.getMgrIp(),echoMgr.getMgrPort(),e.getMessage());
        }

        // echo 失败
        // 更新被echo节点的数据
        youji2MgrService.echoFail(echoMgr);
    }
}
