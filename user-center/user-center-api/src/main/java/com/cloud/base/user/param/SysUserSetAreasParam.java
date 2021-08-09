package com.cloud.base.user.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * 设置用户区域列表参数
 *
 * @author lh0811
 * @date 2021/1/18
 */
@Setter
@Getter
public class SysUserSetAreasParam {

    @NotNull(message = "未上传 用户id")
    @ApiModelProperty(value = "用户id",required = true)
    private Long userId;

    @ApiModelProperty(value = "区域code")
    private String regionCode;
}
