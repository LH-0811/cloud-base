package com.cloud.base.user.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 系统角色修改 bean
 *
 * @auth lh0811
 * @date 2020/11/2
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SysRoleUpdateParam implements Serializable {

    @NotNull(message = "角色id不能为空")
    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "角色名称")
    private String name;

    @ApiModelProperty(value = "角色编号")
    private String no;

    @ApiModelProperty(value = "状态 是否可用")
    private Boolean status;

    @ApiModelProperty(value = "备注")
    private String notes;

}
