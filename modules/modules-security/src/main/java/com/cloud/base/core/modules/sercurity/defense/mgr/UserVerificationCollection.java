package com.cloud.base.core.modules.sercurity.defense.mgr;

import com.cloud.base.core.modules.sercurity.defense.adapter.LhitSecurityUserVerificationAdapter;
import com.cloud.base.core.modules.sercurity.defense.annotation.LhitUserVerification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.HashMap;
import java.util.Map;


/**
 * 通过监听springboot 启动事件
 * <p>
 * 在spring上下文获取到所有所有被LhitUserVerification注解标注的 用户验证适配器类
 */
@Slf4j
public class UserVerificationCollection implements ApplicationListener<ContextRefreshedEvent> {

    private Map<String, LhitSecurityUserVerificationAdapter> userVerificationBeans = new HashMap<>();

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        synchronized (this){
            userVerificationBeans = new HashMap<>();
            Map<String, Object> beans = event.getApplicationContext().getBeansWithAnnotation(LhitUserVerification.class);
            for (Map.Entry<String, Object> stringObjectEntry : beans.entrySet()) {
                userVerificationBeans.put(stringObjectEntry.getKey(), (LhitSecurityUserVerificationAdapter) stringObjectEntry.getValue());
            }
            log.info("获取容器中适配器验证器完成!");
        }
    }


    public Map<String, LhitSecurityUserVerificationAdapter> getUserVerificationBeans() {
        return userVerificationBeans;
    }

    public void setUserVerificationBeans(Map<String, LhitSecurityUserVerificationAdapter> userVerificationBeans) {
        this.userVerificationBeans = userVerificationBeans;
    }
}
