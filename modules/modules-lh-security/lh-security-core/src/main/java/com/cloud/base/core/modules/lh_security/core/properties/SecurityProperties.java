package com.cloud.base.core.modules.lh_security.core.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "lhit.security")
public class SecurityProperties {

    @Getter
    @Setter
    // 请求Header中token的key
    private String tokenKey = "LHTOKEN";

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
    private String serverUrlOfCheckUrl = "/security/check_url";

    @Getter
    @Setter
    // 校验permsCode权限的服务url
    private String serverUrlOfCheckPermsCode = "/security/check_perms_code";

    @Getter
    @Setter
    // 校验permsCode权限的服务url
    private String serverUrlOfCheckStaticResPath = "/security/check_static_res_path";

    @Getter
    @Setter
    // 校验permsCode权限的服务url
    private String serverUrlOfTokenToAuthority = "/security/token_to_authority";

    @Getter
    @Setter
    // 鉴权服务端是否使用 springcloud
    private Boolean useCloud = true;

    @Getter
    @Setter
    private String serverName = "authorize-center-server";



}
