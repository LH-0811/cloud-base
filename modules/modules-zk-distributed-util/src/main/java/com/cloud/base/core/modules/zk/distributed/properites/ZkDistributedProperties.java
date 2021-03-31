package com.cloud.base.core.modules.zk.distributed.properites;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author lh0811
 * @date 2021/3/31
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "zk.distributed")
public class ZkDistributedProperties {
    // 开启标志
    private Boolean enabled = true;
    // 服务器地址
    private String server = "127.0.0.1:2181";
    // 命名空间，被称为ZNode
    private String namespace; //cicada
    // 权限控制，加密
    private String digest; //:smile:123456
    // 会话超时时间
    private Integer sessionTimeoutMs = 3000;
    // 连接超时时间
    private Integer connectionTimeoutMs = 60000;
    // 最大重试次数
    private Integer maxRetries = 2;
    // 初始休眠时间
    private Integer baseSleepTimeMs = 1000;
}
