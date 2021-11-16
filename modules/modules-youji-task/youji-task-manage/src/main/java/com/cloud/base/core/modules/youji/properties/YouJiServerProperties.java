package com.cloud.base.core.modules.youji.properties;

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
@ConfigurationProperties(prefix = "youji.server")
public class YouJiServerProperties {


    @ApiModelProperty(value = "心跳检测频率")
    private String heartBeatCorn = "0/5 * * * * ?";

    /**
     * 如果定义了 5秒一次的心跳检测，超过50秒没有反馈则就是10次心跳没有反馈认为工作节点废弃，从rooster_task_worker表中删除
     */
    @ApiModelProperty(value = "超过N秒没有心跳反馈后认为该工作节点已废弃，从记录表中删除掉")
    private Integer dieNoHeartBeatTime = 99999999;

}
