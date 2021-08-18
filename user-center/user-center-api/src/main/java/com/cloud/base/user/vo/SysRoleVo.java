package com.cloud.base.user.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author lh0811
 * @date 2021/8/18
 */
@Getter
@Setter
public class SysRoleVo {
    /**
     * 角色id
     */
    @ApiModelProperty(value="角色id")
    private Long id;

    /**
     * 角色名称
     */
    @ApiModelProperty(value="角色名称")
    private String name;
    /**
     * 角色编码
     */
    @ApiModelProperty(value="角色编码")
    private String no;
    /**
     * 是否可用
     */
    @ApiModelProperty(value="是否可用")
    private Boolean status;
    /**
     * 角色备注
     */
    @ApiModelProperty(value="角色备注")
    private String notes;
    /**
     * 创建时间
     */
    @ApiModelProperty(value="创建时间")
    private Date createTime;


}
