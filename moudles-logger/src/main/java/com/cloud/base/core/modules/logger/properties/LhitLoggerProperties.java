package com.cloud.base.core.modules.logger.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "lhit.logger")
public class LhitLoggerProperties {


    // 是否开启安全控制
    private Boolean enable = true;


    /**
     * 是否保存请求的参数
     */
    private Boolean saveRequestData = true;

    /**
     * 是否保存返回参数
     */
    private Boolean saveReturnData = true;


    /**
     * 是否保存操作用户信息
     */
    private Boolean saveOperUserInfo = true;


    /**
     * 是否保存操作角色信息(*必须打开保存用户信息)
     */
    private Boolean saveOperRoleInfo = true;


    /**
     * 是否保存操作客户端的网络地址信息
     */
    private Boolean saveOperNetAddr = true;

}
