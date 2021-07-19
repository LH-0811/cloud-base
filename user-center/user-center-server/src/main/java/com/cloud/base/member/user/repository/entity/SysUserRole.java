package com.cloud.base.member.user.repository.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 系统-用户-角色关系表(SysUserRole)实体类
 *
 * @author lh0811
 * @since 2021-01-18 10:29:35
 */
@Setter
@Getter
@Table(name="sys_user_role")
public class SysUserRole implements Serializable {

    /**
     * 用户-角色关系表
     */
    @Id
    @KeySql(useGeneratedKeys = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
