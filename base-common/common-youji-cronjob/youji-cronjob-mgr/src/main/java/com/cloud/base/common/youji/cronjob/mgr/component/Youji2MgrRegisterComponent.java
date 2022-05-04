package com.cloud.base.common.youji.cronjob.mgr.component;

import com.cloud.base.common.youji.cronjob.mgr.properties.Youji2ServerProperties;
import com.cloud.base.common.youji.cronjob.mgr.service.Youji2MgrService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author lh0811
 * @date 2022/4/15
 */
@Slf4j
@Order(value = Integer.MAX_VALUE-100)
@Component
public class Youji2MgrRegisterComponent implements CommandLineRunner {

    @Autowired
    private Youji2ServerProperties properties;

    @Value("${server.port:8080}")
    private Integer port;

    @Autowired
    private Youji2MgrService youji2MgrService;

    @Override
    public void run(String... args) throws Exception {
        log.info("[酉鸡2 注册当前服务节点到MySql] host:{} port:{}", properties.getMgrIp(), port);
        // 将当前节点保存到mysql中
        youji2MgrService.saveOrUpdateCurrentNodeTaskMgr();

        // 确定互相监控的规则
        log.info("[酉鸡2 更新管理节点之间的echo关系] host:{} port:{}", properties.getMgrIp(), port);
        youji2MgrService.updateTaskMgrBindEchoMgrRel();

        // 分发任务给管理节点
        log.info("[酉鸡2 分配任务给管理节点] host:{} port:{}", properties.getMgrIp(), port);
        youji2MgrService.dispatchTaskToMgr();

        log.info("[酉鸡2 管理节点注册完成] host:{} port:{}", properties.getMgrIp(), port);

    }

}
