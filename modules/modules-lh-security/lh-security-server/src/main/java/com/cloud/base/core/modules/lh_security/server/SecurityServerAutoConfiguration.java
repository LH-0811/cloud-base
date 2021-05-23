package com.cloud.base.core.modules.lh_security.server;


import com.cloud.base.core.modules.lh_security.server.authentication.SecurityCheckAuthority;
import com.cloud.base.core.modules.lh_security.server.authentication.SecurityVoucherVerificationProcess;
import com.cloud.base.core.modules.lh_security.server.authentication.impl.DefaultSecurityCheckAuthority;
import com.cloud.base.core.modules.lh_security.server.authentication.impl.DefaultUsernamePasswordVoucherVoucherVerification;
import com.cloud.base.core.modules.lh_security.server.authentication.impl.DefaultSecurityVoucherVerificationProcess;
import com.cloud.base.core.modules.lh_security.core.properties.SecurityProperties;
import com.cloud.base.core.modules.lh_security.server.service.SecurityService;
import com.cloud.base.core.modules.lh_security.server.service.impl.SecurityServiceImpl;
import com.cloud.base.core.modules.lh_security.server.token.TokenGenerate;
import com.cloud.base.core.modules.lh_security.server.token.TokenManager;
import com.cloud.base.core.modules.lh_security.server.token.impl.DefaultTokenGenerate;
import com.cloud.base.core.modules.lh_security.server.token.impl.DefaultTokenManager;
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
@EnableConfigurationProperties({SecurityProperties.class})
public class SecurityServerAutoConfiguration {

    // token //////////////////////////////////////////////////////////////////

    // token管理
    @Bean
    @ConditionalOnMissingBean(TokenManager.class)
    public TokenManager tokenManager() {
        return new DefaultTokenManager();
    }

    // token生成
    @Bean
    @ConditionalOnMissingBean(TokenGenerate.class)
    public TokenGenerate tokenGenerate() {
        return new DefaultTokenGenerate();
    }


    @Bean
    @ConditionalOnMissingBean(SecurityService.class)
    public SecurityService securityService(){
        return new SecurityServiceImpl();
    }

    // 凭证认证 //////////////////////////////////////////////////////////////////

    // 认证过程
    @Bean
    @ConditionalOnMissingBean(SecurityVoucherVerificationProcess.class)
    public SecurityVoucherVerificationProcess securityVoucherVerificationProcess() {
        return new DefaultSecurityVoucherVerificationProcess();
    }

    // 默认认证器
    @Bean
    public DefaultUsernamePasswordVoucherVoucherVerification defaultSecurityVoucherVerification(){
        return new DefaultUsernamePasswordVoucherVoucherVerification();
    }

    // 权限校验
    @Bean
    @ConditionalOnMissingBean(SecurityCheckAuthority.class)
    public SecurityCheckAuthority securityCheckAuthority() {
        return new DefaultSecurityCheckAuthority();
    }



}
