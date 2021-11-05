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
 * 系统-用户-角色关系表(SysUserRoleRel)实体类
 *
 * @author lh0811
 * @since 2021-11-03 22:30:54
 */
@Setter
@Getter
@TableName("sys_user_role_rel")
@AllArgsConstructor
@NoArgsConstructor
public class SysUserRoleRel implements Serializable {

    /**
     * 用户-角色关系表
     */
    @TableId(type = IdType.NONE)
    @ApiModelProperty(value="用户-角色关系表")
    private Long id;

    /**
     * 用户id
     */
    @ApiModelProperty(value="用户id")
    private Long userId;
    /**
     * 角色id
     */
    @ApiModelProperty(value="角色id")
    private Long roleId;


}
