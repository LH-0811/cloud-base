package com.cloud.base.user.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 用户中心-岗位信息(SysPosition)实体类
 *
 * @author lh0811
 * @since 2021-08-10 21:55:23
 */
@Setter
@Getter
public class SysPositionCreateParam implements Serializable {

    /**
     * 岗位编号
     */
    @NotBlank(message = "未上传 岗位编号")
    @ApiModelProperty(value="岗位编号",required = true)
    private String no;

    /**
     * 岗位名称
     */
    @NotBlank(message = "未上传 岗位名称")
    @ApiModelProperty(value="岗位名称",required = true)
    private String name;
    /**
     * 岗位备注
     */
    @ApiModelProperty(value="岗位备注")
    private String notes;


}
