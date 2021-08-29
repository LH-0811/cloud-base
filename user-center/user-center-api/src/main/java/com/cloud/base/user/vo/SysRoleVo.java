package com.cloud.base.user.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

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

    @ApiModelProperty(value = "排序 (升序)")
    private Integer sortNum;

    /**
     * 角色编码
     */
    @ApiModelProperty(value="角色编码")
    private String no;
    /**
     * 是否可用
     */
    @ApiModelProperty(value="是否可用")
    private Boolean activeFlag;
    /**
     * 角色备注
     */
    @ApiModelProperty(value="角色备注")
    private String notes;

    @ApiModelProperty(value="角色资源列表")
    private List<SysResVo> sysResList;

    @ApiModelProperty(value="角色资源数")
    private List<SysResVo> sysResTree;

    /**
     * 创建时间
     */
    @ApiModelProperty(value="创建时间")
    private Date createTime;


}
