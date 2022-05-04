package com.cloud.base.common.youji.cronjob.mgr.entity;

import com.cloud.base.common.youji.cronjob.core.repository.entity.Youji2TaskInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.ScheduledFuture;

/**
 * @author lh0811
 * @date 2021/11/15
 */
@Getter
@Setter
@ApiModel(value = "酉鸡-定时任务描述实体类")
public class Youji2SchedulerEntity {

    @ApiModelProperty(value = "定时任务编号")
    private String taskNo;

    @ApiModelProperty(value = "定时任务信息")
    private Youji2TaskInfo taskInfo;

    @ApiModelProperty(value = "定时任务执行任务")
    private ScheduledFuture<?> future;

}
