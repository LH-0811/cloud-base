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
 * 用户中心-角色表
 * 
 * @author lh0811
 * @email lh0811
 * @date 2022-01-05 18:01:20
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_role")
public class SysRole implements Serializable {

	/**
	 * 角色id
	 */
	@TableId(type= IdType.NONE)
	@ApiModelProperty(value="角色id")
	private Long id;
	/**
	 * 租户no
	 */
	@ApiModelProperty(value="租户no")
	private String tenantNo;
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
	private Boolean activeFlag;
	/**
	 * 排序（升序）
	 */
	@ApiModelProperty(value="排序（升序）")
	private Integer sortNum;
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
	/**
	 * 更新时间
	 */
	@ApiModelProperty(value="更新时间")
	private Date updateTime;
	/**
	 * 创建人
	 */
	@ApiModelProperty(value="创建人")
	private Long createBy;
	/**
	 * 更新人
	 */
	@ApiModelProperty(value="更新人")
	private Long updateBy;

}
