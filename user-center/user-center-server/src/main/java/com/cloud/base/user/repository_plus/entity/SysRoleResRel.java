package com.cloud.base.user.repository_plus.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import io.swagger.annotations.ApiModelProperty;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 系统-角色-权限关系表(SysRoleResRel)实体类
 *
 * @author lh0811
 * @since 2021-11-03 22:29:44
 */
@Setter
@Getter
@TableName("sys_role_res_rel")
@AllArgsConstructor
@NoArgsConstructor
public class SysRoleResRel implements Serializable {

    /**
     * 角色权限关系id
     */
    @TableId(type = IdType.NONE)
    @ApiModelProperty(value="角色权限关系id")
    private Long id;

    /**
     * 角色id
     */
    @ApiModelProperty(value="角色id")
    private Long roleId;
    /**
     * 权限id
     */
    @ApiModelProperty(value="权限id")
    private Long resId;


}
