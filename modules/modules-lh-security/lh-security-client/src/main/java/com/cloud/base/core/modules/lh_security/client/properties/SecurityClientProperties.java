package com.cloud.base.core.modules.lh_security.client.properties;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;


@ConfigurationProperties(prefix = "lhit.security.client")
public class SecurityClientProperties {

    @Getter
    @Setter
    // 请求头中token的key
    private String tokenKey = "LHTOKEN";

    @Getter
    @Setter
    // 请求头中token的key
    private String serverAddr = "127.0.0.1";

    @Getter
    @Setter
    // token 过期时间 单位是分钟
    private Integer serverPort = 8999;

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

}
