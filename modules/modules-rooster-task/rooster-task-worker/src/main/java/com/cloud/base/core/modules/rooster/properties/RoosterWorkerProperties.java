package com.cloud.base.core.modules.rooster.properties;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author lh0811
 * @date 2021/11/14
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "rooster.worker")
public class RoosterWorkerProperties {


    @ApiModelProperty(value = "管理服务主机")
    private String manageHost = "127.0.0.1";

    @ApiModelProperty(value = "管理服务端口")
    private Integer managePort = 9301;

}
