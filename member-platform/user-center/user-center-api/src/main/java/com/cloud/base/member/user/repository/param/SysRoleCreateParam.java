package com.cloud.base.member.user.repository.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * 创建系统角色参数
 *
 * @auth lh0811
 * @date 2020/11/13
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SysRoleCreateParam {

    @NotNull(message = "角色名称不能为空")
    @ApiModelProperty(value = "角色名称", required = true)
    private String name;

    @ApiModelProperty(value = "角色编号")
    private String no;

    @ApiModelProperty(value = "状态 是否可用默认可用", required = true)
    private Boolean status = true;

    @ApiModelProperty(value = "备注")
    private String notes;
}
