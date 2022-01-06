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
 * 用户中心-权限关系表
 * 
 * @author lh0811
 * @email lh0811
 * @date 2022-01-05 18:01:20
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_role_res_rel")
public class SysRoleResRel implements Serializable {

	/**
	 * 角色权限关系id
	 */
	@TableId(type= IdType.NONE)
	@ApiModelProperty(value="角色权限关系id")
	private Long id;
	/**
	 * 租户no
	 */
	@ApiModelProperty(value="租户no")
	private String tenantNo;
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
