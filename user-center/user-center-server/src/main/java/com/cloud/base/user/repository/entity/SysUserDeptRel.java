package com.cloud.base.user.repository.entity;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import io.swagger.annotations.ApiModelProperty;
import tk.mybatis.mapper.annotation.KeySql;
import javax.persistence.*;
/**
 * 用户中心-用户部门信息关系表(SysUserDeptRelation)实体类
 *
 * @author lh0811
 * @since 2021-08-10 21:55:23
 */
@Setter
@Getter
@Table(name="sys_user_dept_rel")
public class SysUserDeptRel implements Serializable {

    /**
     * 用户部门关系id
     */
    @Id
    @KeySql(useGeneratedKeys = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value="用户部门关系id")
    private Long id;

    /**
     * 用户id
     */
    @ApiModelProperty(value="用户id")
    private Long userId;
    /**
     * 部门id
     */
    @ApiModelProperty(value="部门id")
    private Long deptId;


}
