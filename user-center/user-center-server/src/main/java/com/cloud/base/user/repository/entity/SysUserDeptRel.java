package com.cloud.base.user.repository.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 用户中心-用户部门信息关系表
 * 
 * @author lh0811
 * @email lh0811
 * @date 2021-11-10 11:01:40
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_user_dept_rel")
public class SysUserDeptRel implements Serializable {

	/**
	 * 用户部门关系id
	 */
	@TableId(type= IdType.NONE)
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
