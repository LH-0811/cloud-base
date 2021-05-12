package com.cloud.base.core.modules.lh_security.server.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "lhit.security.server")
public class SecurityServerProperties {


    @Getter
    @Setter
    // token 过期时间 单位是分钟
    private String expire = "9999999";

    @Getter
    @Setter
    // 无权限返回码
    private String noAuthorizedCode = "401";

    @Getter
    @Setter
    // 权限不足返回码
    private String unAuthorizedCode = "403";

    @Getter
    @Setter
    // 校验url权限的服务url
    private String serverUrlOfCheckUrl = "/check_url";

    @Getter
    @Setter
    // 校验permsCode权限的服务url
    private String serverUrlOfCheckPermsCode = "/check_perms_code";

    @Getter
    @Setter
    // 校验permsCode权限的服务url
    private String serverUrlOfCheckStaticResPath = "/check_static_res_path";

    @Getter
    @Setter
    // 校验permsCode权限的服务url
    private String serverUrlOfGetSecurityAuthority = "/get_security_authority";

}