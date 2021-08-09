package com.cloud.base.user.repository.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 系统-角色-权限关系表(SysRoleRes)实体类
 *
 * @author lh0811
 * @since 2021-01-18 10:29:35
 */
@Setter
@Getter
@Table(name="sys_role_res")
@NoArgsConstructor
@AllArgsConstructor
public class SysRoleRes implements Serializable {

    /**
     * 角色权限关系id
     */
    @Id
    @KeySql(useGeneratedKeys = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
