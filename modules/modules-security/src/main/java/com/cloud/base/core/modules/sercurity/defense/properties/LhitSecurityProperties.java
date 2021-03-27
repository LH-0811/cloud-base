package com.cloud.base.core.modules.sercurity.defense.properties;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;


@ConfigurationProperties(prefix = "lhit.security.defense")
public class LhitSecurityProperties {

    @Getter
    @Setter
    // 是否开启安全控制
    private boolean enable = true;

    @Getter
    @Setter
    // 忽略权限验证的url
    private List<String> ignore = Lists.newArrayList();

    @Getter
    @Setter
    // 请求头中token的key
    private String tokenkey = "FTTOKEN";

    @Getter
    @Setter
    // 是不是使用redis作为缓存
    private boolean rediscache = false;


    @Getter
    @Setter
    // token 过期时间 单位是分钟
    private String expire = "9999999";

    @Getter
    @Setter
    // 无权限返回码
    private String un_authorized_code = "401";

    @Getter
    @Setter
    // 权限不足返回码
    private String un_enough_authorized_code = "403";

}
