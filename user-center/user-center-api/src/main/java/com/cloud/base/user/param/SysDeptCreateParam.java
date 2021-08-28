package com.cloud.base.user.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 系统部门创建参数
 *
 * @author lh0811
 * @date 2021/8/10
 */
@Getter
@Setter
public class SysDeptCreateParam implements Serializable {

    @NotNull(message = "未上传 父级部门id")
    @ApiModelProperty(value = "父级部门id",required = true)
    private Long parentId = 0L;

    @ApiModelProperty(value = "部门编号")
    private String no;

    @NotBlank(message = "未上传 部门名称")
    @ApiModelProperty(value = "部门名称",required = true)
    private String name;

    @ApiModelProperty(value = "部门备注")
    private String notes;


}
