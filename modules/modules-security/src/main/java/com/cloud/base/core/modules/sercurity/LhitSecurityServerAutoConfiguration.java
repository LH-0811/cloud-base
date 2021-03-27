package com.cloud.base.core.modules.sercurity;


import com.cloud.base.core.modules.sercurity.defense.adapter.*;
import com.cloud.base.core.modules.sercurity.defense.adapter.impl.*;
import com.cloud.base.core.modules.sercurity.defense.filter.DefaultLhitSecurityPermsFilter;
import com.cloud.base.core.modules.sercurity.defense.filter.LhitPermsWebMvcConfig;
import com.cloud.base.core.modules.sercurity.defense.filter.LhitSecurityPermsFilter;
import com.cloud.base.core.modules.sercurity.defense.mgr.UserVerificationCollection;
import com.cloud.base.core.modules.sercurity.defense.properties.LhitSecurityProperties;
import com.cloud.base.core.modules.sercurity.defense.util.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@EnableConfigurationProperties({LhitSecurityProperties.class})
@AutoConfigureAfter(value = {LhitSecurityProperties.class})
public class LhitSecurityServerAutoConfiguration {

    @Autowired
    private LhitSecurityProperties lhitSecurityProperties;

    //////////////////////////// 用户认证 ////////////////////////////////////////////////
    @Bean
    public LhitSecurityUserVerificationAdapter userAuthenticationAdapter() {
        return new DefaultLhitSecurityUserVerificationImpl();
    }

    @Bean
    public UserVerificationCollection userVerificationCollection() {
        return new UserVerificationCollection();
    }

    @Bean
    @ConditionalOnMissingBean(LhitSecurityVerificationDispatchAdapter.class)
    public LhitSecurityVerificationDispatchAdapter lhitSecurityVerificationDispatchAdapter() {
        return new DefaultLhitSecurityVerificationDispatchAdapter();
    }

    //////////////////////////// 权限拦截器配置 ////////////////////////////////////////////////
    @Bean
    @ConditionalOnMissingBean(LhitSecurityPermsFilter.class)
    public LhitSecurityPermsFilter lhitSecurityPermsFilter() {
        return new DefaultLhitSecurityPermsFilter();
    }

    @Bean
    public LhitPermsWebMvcConfig lhitPermsWebMvcConfig() {
        return new LhitPermsWebMvcConfig(lhitSecurityPermsFilter());
    }



    //////////////////////////// 资源保护 ////////////////////////////////////////////////
    @Bean
    @ConditionalOnMissingBean(LhitSecurityResourceProtectAdapter.class)
    public LhitSecurityResourceProtectAdapter lhitSecurityResourceProtectAdapter() {
        return new DefaultLhitSecurityResourceProtectAdapter();
    }


    //////////////////////////// token管理 ////////////////////////////////////////////////
    @Bean
    @ConditionalOnMissingBean(LhitSecurityTokenManagerAdapter.class)
    public LhitSecurityTokenManagerAdapter lhitSecurityTokenManagerAdapter() {
        if (lhitSecurityProperties.isRediscache()) {
            return new DefaultLhitSecurityTokenManagerAdapter();
        }
        return new DefaultLhitSecurityTokenManagerAdapter();
    }

    @Bean
    @ConditionalOnMissingBean(JwtUtils.class)
    public JwtUtils jwtUtils() {
        return new JwtUtils();
    }

    @Bean
    @ConditionalOnMissingBean(LhitSecurityTokenGenerateAdapter.class)
    public LhitSecurityTokenGenerateAdapter lhitSecurityTokenGenerateAdapter() {
        return new DefaultLhitSecurityTokenGenerateAdapter();
    }

    //////////////////////////// 为了简化用户登录认证授权的过程，提供默认的用户登录授权适配器 ////////////////////////////////////////////////
    @Bean
    @ConditionalOnMissingBean(LhitSecurityUserAuthenticationLoginAdapter.class)
    public LhitSecurityUserAuthenticationLoginAdapter lhitSecurityUserAuthenticationLoginAdapter() {
        return new DefaultLhitSecurityUserAuthenticationLoginAdapter();
    }




}
