package com.cloud.base.common.youji.cronjob.mgr.properties;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author lh0811
 * @date 2021/11/14
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "youji.server")
public class Youji2ServerProperties {

    @ApiModelProperty("管理节点id")
    private Integer mgrIndex;

    @ApiModelProperty("是否可用")
    private Boolean enable = true;

    @ApiModelProperty(value = "当前节点ip")
    private String mgrIp = "127.0.0.1";

    @ApiModelProperty(value = "心跳时间")
    private String heartBeatCron = "0/5 * * * * ?";

    @ApiModelProperty(value = "心跳失败的最大数,达到这个失败数量后 节点会强制下线 直到失败数归0后才会恢复")
    private Integer heartBeatFailNum = 5;


}
