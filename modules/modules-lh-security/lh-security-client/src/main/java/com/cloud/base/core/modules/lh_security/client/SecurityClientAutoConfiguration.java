package com.cloud.base.core.modules.lh_security.client;


import com.cloud.base.core.modules.lh_security.client.component.OkHttpClientUtil;
import com.cloud.base.core.modules.lh_security.client.component.annotation.*;
import com.cloud.base.core.modules.lh_security.client.properties.SecurityClientProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
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
@EnableConfigurationProperties({SecurityClientProperties.class})
public class SecurityClientAutoConfiguration {


    // http客户端 ////////////////////////////////////////
    @Bean
    public OkHttpClientUtil okHttpClientUtil() {
        return new OkHttpClientUtil();
    }

    @Bean
    public HasUrlAop hasUrlAop(){
        return new HasUrlAop();
    }

    @Bean
    public HasPermsCodeAop hasPermsCodeAop(){
        return new HasPermsCodeAop();
    }

    @Bean
    public HasStaticResPathAop hasStaticResPathAop(){
        return new HasStaticResPathAop();
    }

    @Bean
    public TokenToAuthorityAop tokenToAuthorityAop(){
        return new TokenToAuthorityAop();
    }

}
