package com.cloud.base.core.modules.rooster.component;

import com.alibaba.fastjson.JSON;
import com.cloud.base.core.modules.rooster.code.annotation.EnableRooster;
import com.cloud.base.core.modules.rooster.code.annotation.RoosterTask;
import com.cloud.base.core.modules.rooster.code.param.RoosterTaskCreateParam;
import com.cloud.base.core.modules.rooster.properties.RoosterWorkerProperties;
import com.cloud.base.core.modules.rooster.util.RoosterWorkOkHttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
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
@Order
@Component
public class RoosterTaskScanner implements CommandLineRunner {

    @Autowired
    private ApplicationContext applicationContext;


    @Autowired
    private RoosterWorkerProperties properties;

    @Autowired
    private RoosterWorkOkHttpClientUtil httpClientUtil;

    @Override
    public void run(String... args) throws Exception {
        // 获取到应用上下文中标记了RoosterTask 的方法
        Map<String, Object> roosterTaskMap = applicationContext.getBeansWithAnnotation(EnableRooster.class);
        RoosterTaskCreateParam param = new RoosterTaskCreateParam();
        param.setParamList(Lists.newArrayList());
        log.info("[酉鸡 开始扫描定时任务] RoosterTaskScanner.scannerTask");
        for (Map.Entry<String, Object> task : roosterTaskMap.entrySet()) {
            String key = task.getKey();
            Object value = task.getValue();
            Method[] methods = value.getClass().getMethods();
            for (Method method : methods) {
                RoosterTask annotation = method.getAnnotation(RoosterTask.class);
                if (annotation != null) {
                    log.info("[酉鸡 扫描到定时任务] RoosterTaskScanner.scannerTask: 任务名称={},任务编号={}",annotation.taskName(),annotation.taskNo());
                    // 创建默认配置 这个配置会以taskNo为唯一标志 注册到Rooster manage服务，如果数据库中有相关的记录，则以数据库中的配置为准，
                    // 这里只是做一个默认的初始化配置
                    RoosterTaskCreateParam.RoosterTaskForm createParam = new RoosterTaskCreateParam.RoosterTaskForm();
                    createParam.setTaskName(annotation.taskName());
                    createParam.setTaskNo(annotation.taskNo());
                    createParam.setCorn(annotation.corn());
                    createParam.setEnableFlag(annotation.enable());
                    createParam.setTaskBeanName(value.getClass().getName());
                    createParam.setTaskMethod(method.getName());
                    createParam.setTaskParam(StringUtils.join(annotation.params(),"|"));
                    createParam.setContactsName(annotation.contactsName());
                    createParam.setContactsPhone(annotation.contactsPhone());
                    createParam.setContactsEmail(annotation.contactsEmail());
                    param.getParamList().add(createParam);
                }
            }
        }
        log.info("[酉鸡 完成扫描定时任务] RoosterTaskScanner.scannerTask");

        String reqUrl = "http://"+properties.getManageHost()+":"+properties.getManagePort()+"/rooster/task/manage/create";
        Response response = httpClientUtil.postJSONParameters(reqUrl, JSON.toJSONString(param));

        log.info("[酉鸡 注册服务到manager] response={}",response.body());
    }
}
