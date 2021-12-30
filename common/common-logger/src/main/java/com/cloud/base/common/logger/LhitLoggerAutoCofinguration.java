package com.cloud.base.common.logger;


import com.cloud.base.common.logger.adapter.LhitLoggerRoleInfoByUserAdapter;
import com.cloud.base.common.logger.adapter.LhitLoggerStorageAdapter;
import com.cloud.base.common.logger.adapter.LhitLoggerUserInfoFromRequestAdapter;
import com.cloud.base.common.logger.adapter.impl.DefaultLhitLoggerStorageAdapter;
import com.cloud.base.common.logger.adapter.impl.DefaultRoleInfoByUserAdapter;
import com.cloud.base.common.logger.adapter.impl.DefaultUserInfoFromRequest;
import com.cloud.base.common.logger.aop.LhitLoggerMethodAop;
import com.cloud.base.common.logger.properties.LhitLoggerProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@EnableConfigurationProperties(LhitLoggerProperties.class)
@AutoConfigureAfter(value = {LhitLoggerProperties.class})
public class LhitLoggerAutoCofinguration {

    @Autowired
    private LhitLoggerMethodAop lhitLoggerMethodAop;

    @Bean
    public AspectJExpressionPointcutAdvisor permsPointcutAdvisor() {
        AspectJExpressionPointcutAdvisor advisor = new AspectJExpressionPointcutAdvisor();
        advisor.setExpression("@annotation(com.cloud.base.common.logger.annotation.LhitLogger)");
        advisor.setAdvice(lhitLoggerMethodAop);
        return advisor;
    }


    @Bean
    @ConditionalOnMissingBean(LhitLoggerUserInfoFromRequestAdapter.class)
    public LhitLoggerUserInfoFromRequestAdapter lhitLoggerUserInfoFromRequestAdapter() {
        return new DefaultUserInfoFromRequest();
    }

    @Bean
    @ConditionalOnMissingBean(LhitLoggerRoleInfoByUserAdapter.class)
    public LhitLoggerRoleInfoByUserAdapter lhitLoggerRoleInfoByUserAdapter() {
        return new DefaultRoleInfoByUserAdapter();
    }

    @Bean
    @ConditionalOnMissingBean(LhitLoggerStorageAdapter.class)
    public LhitLoggerStorageAdapter lhitLoggerStorageAdapter() {
        return new DefaultLhitLoggerStorageAdapter();
    }

}

