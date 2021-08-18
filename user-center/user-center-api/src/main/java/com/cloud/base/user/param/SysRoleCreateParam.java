package com.cloud.base.user.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

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
public class SysRoleCreateParam implements Serializable {

    @NotNull(message = "角色名称不能为空")
    @ApiModelProperty(value = "角色名称", required = true)
    private String name;

    @ApiModelProperty(value = "角色编号")
    private String no;

    @ApiModelProperty(value = "状态 是否可用默认可用", required = true)
    private Boolean activeFlag = true;

    @ApiModelProperty(value = "排序 (升序)")
    private Integer sortNum;

    @ApiModelProperty(value = "备注")
    private String notes;

    @ApiModelProperty(value = "资源id列表")
    private List<Long> resIdList;
}
