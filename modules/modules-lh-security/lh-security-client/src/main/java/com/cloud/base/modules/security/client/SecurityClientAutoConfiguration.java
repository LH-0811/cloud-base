package com.cloud.base.modules.security.client;


import com.cloud.base.modules.security.client.component.annotation.HasPermsCodeAop;
import com.cloud.base.modules.security.client.component.annotation.HasStaticResPathAop;
import com.cloud.base.modules.security.client.component.annotation.HasUrlAop;
import com.cloud.base.modules.security.client.component.annotation.TokenToAuthorityAop;
import com.cloud.base.modules.security.client.component.provide.ProvideResToSecurityClient;
import com.cloud.base.modules.security.client.component.provide.impl.DefaultProvideResToSecurityClient;
import com.cloud.base.modules.security.client.service.SecurityClient;
import com.cloud.base.modules.security.client.service.impl.DefaultSecurityClientImpl;
import com.cloud.base.modules.security.client.util.OkHttpClientUtil;
import com.cloud.base.modules.security.client.component.annotation.*;
import com.cloud.base.modules.security.core.properties.SecurityProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * 自动装配
 * <p>
 * 将安全组件委托到springContext
 */
@Slf4j
@Configuration
@EnableConfigurationProperties({SecurityProperties.class})
public class SecurityClientAutoConfiguration {


    // http客户端 ////////////////////////////////////////
    @Bean
    public OkHttpClientUtil okHttpClientUtil() {
        return new OkHttpClientUtil();
    }

    @Bean
    public HasUrlAop hasUrlAop() {
        return new HasUrlAop();
    }

    @Bean
    public HasPermsCodeAop hasPermsCodeAop() {
        return new HasPermsCodeAop();
    }

    @Bean
    public HasStaticResPathAop hasStaticResPathAop() {
        return new HasStaticResPathAop();
    }

    @Bean
    public TokenToAuthorityAop tokenToAuthorityAop() {
        return new TokenToAuthorityAop();
    }

    @Bean
    public SecurityClient securityClient(){
        return new DefaultSecurityClientImpl();
    }
    @Bean
    public ProvideResToSecurityClient provideResToSecurityClient() {
        return new DefaultProvideResToSecurityClient();
    }

}
