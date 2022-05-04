package com.cloud.base.common.youji.cronjob.worker.properties;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @author lh0811
 * @date 2021/11/14
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "youji.worker")
public class Youji2WorkerProperties {


    @ApiModelProperty(value = "酉鸡定时任务管理节点")
    private String[] manageIpPort = new String[]{"localhost:8889", "localhost:8890"};

    @ApiModelProperty(value = "当前工作节点ip")
    private String currentWorkerIp = "localhost";

    @ApiModelProperty(value = "当前工作节点端口")
    private Integer currentWorkerPort = 9301;

}
