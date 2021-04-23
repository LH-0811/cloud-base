package com.cloud.base.member.user.repository.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 系统-行政区域划分表(SysRegion)实体类
 *
 * @author lh0811
 * @since 2021-01-18 10:29:35
 */
@Setter
@Getter
public class SysRegionUpdateParam implements Serializable {

    @NotNull(message = "未上传 行政区划代码")
    @ApiModelProperty(value = "行政区划代码",required = true)
    private String code;

    @ApiModelProperty(value = "名称")
    private String name;


}
