package com.cloud.base.modules.youji.component;

import com.cloud.base.modules.youji.code.annotation.EnableYouJi;
import com.cloud.base.modules.youji.code.annotation.YouJiTask;
import com.cloud.base.modules.youji.code.param.YouJiWorkerRegisterTaskParam;
import com.cloud.base.modules.youji.properties.YouJiWorkerProperties;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * 任务扫描器
 *
 * @author lh0811
 * @date 2021/11/13
 */
@Slf4j
@Order(value = Integer.MAX_VALUE - 1000)
@Component
public class YouJiTaskScanner implements CommandLineRunner {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private WorkerRegisterNetwork registerNetwork;

    @Autowired
    private YouJiWorkerProperties workerProperties;

    @Override
    public void run(String... args) throws Exception {

        log.info("[酉鸡 Worder 扫描并注册定时任务] YouJiTaskScanner 开始");
        // 获取到应用上下文中标记了YouJiTask 的方法
        Map<String, Object> youjiTaskMap = applicationContext.getBeansWithAnnotation(EnableYouJi.class);
        YouJiWorkerRegisterTaskParam param = new YouJiWorkerRegisterTaskParam();
        param.setParamList(Lists.newArrayList());
        for (Map.Entry<String, Object> task : youjiTaskMap.entrySet()) {
            String key = task.getKey();
            Object value = task.getValue();
            Method[] methods = value.getClass().getMethods();
            for (Method method : methods) {
                YouJiTask annotation = method.getAnnotation(YouJiTask.class);
                if (annotation != null) {
                    log.info("[酉鸡 扫描到定时任务] YouJiTaskScanner.scannerTask: 任务名称={},任务编号={}", annotation.taskName(), annotation.taskNo());
                    // 创建默认配置 这个配置会以taskNo为唯一标志 注册到YouJi manage服务，如果数据库中有相关的记录，则以数据库中的配置为准，
                    // 这里只是做一个默认的初始化配置
                    YouJiWorkerRegisterTaskParam.YouJiTaskForm createParam = new YouJiWorkerRegisterTaskParam.YouJiTaskForm();
                    createParam.setTaskName(annotation.taskName());
                    createParam.setTaskNo(annotation.taskNo());
                    createParam.setCorn(annotation.corn());
                    createParam.setEnableFlag(annotation.enable());
                    createParam.setTaskBeanName(value.getClass().getName());
                    createParam.setTaskMethod(method.getName());
                    createParam.setTaskParam(annotation.param());
                    createParam.setContactsName(annotation.contactsName());
                    createParam.setContactsPhone(annotation.contactsPhone());
                    createParam.setContactsEmail(annotation.contactsEmail());
                    param.getParamList().add(createParam);
                }
            }
        }

        // 将注册请求发送到管理端
        param.setWorkIP(workerProperties.getCurrentWorkerIp());
        param.setWorkPort(workerProperties.getCurrentWorkerPort());
        registerNetwork.registerToManage(param);

        log.info("[酉鸡 Worder 扫描并注册定时任务] YouJiTaskScanner 完成");
    }

}
