package com.cloud.base.user.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 系统权限创建参数 bean
 *
 * @auth lh0811
 * @date 2020/11/2
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SysResCreateParam implements Serializable {

    @NotNull(message = "父节点id不能为空")
    @ApiModelProperty(value = "父节点id",required = true)
    private Long parentId=0L;

    @NotBlank(message = "权限名称不能为空")
    @ApiModelProperty(value = "权限名称",required = true)
    private String name;

    @ApiModelProperty(value = "类型：0-静态资源 1-目录 2-菜单 3-按钮 4-接口 5-数据")
    private Integer type;

    @ApiModelProperty(value = "资源路径")
    private String url;

    @ApiModelProperty(value = "图标")
    private String icon;

    @ApiModelProperty(value = "排序")
    private String orderNo;

    @ApiModelProperty(value = "备注")
    private String remark;

}
