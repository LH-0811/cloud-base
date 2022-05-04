package com.cloud.base.common.youji.cronjob.core.param;

import com.cloud.base.common.youji.cronjob.core.repository.entity.Youji2TaskInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author lh0811
 * @date 2022/4/14
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MgrTaskBindParam {

    @ApiModelProperty(value = "管理节点自定义id")
    private String mgrId;

    @ApiModelProperty(value = "任务详情")
    private Youji2TaskInfo taskInfo;

}
