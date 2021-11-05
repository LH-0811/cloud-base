package com.cloud.base.user.repository_plus.entity;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import io.swagger.annotations.ApiModelProperty;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 用户中心-用户部门信息关系表(SysUserDeptRel)实体类
 *
 * @author lh0811
 * @since 2021-11-03 22:30:12
 */
@Setter
@Getter
@TableName("sys_user_dept_rel")
public class SysUserDeptRel implements Serializable {

    /**
     * 用户部门关系id
     */
    @TableId(type = IdType.NONE)
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