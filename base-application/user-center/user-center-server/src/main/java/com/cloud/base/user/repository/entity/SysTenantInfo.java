package com.cloud.base.user.repository.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 系统管理-租户信息管理
 * 
 * @author lh0811
 * @email lh0811
 * @date 2022-01-05 18:01:20
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_tenant_info")
public class SysTenantInfo implements Serializable {

	/**
	 * 主键id
	 */
	@TableId(type= IdType.NONE)
	@ApiModelProperty(value="主键id")
	private Long id;
	/**
	 * 租户No
	 */
	@ApiModelProperty(value="租户No")
	private String tenantNo;
	/**
	 * 租户名称
	 */
	@ApiModelProperty(value="租户名称")
	private String tenantName;
	/**
	 * 租户简称
	 */
	@ApiModelProperty(value="租户简称")
	private String tenantSimpleName;
	/**
	 * 租户英文名称
	 */
	@ApiModelProperty(value="租户英文名称")
	private String tenantEnglishName;
	/**
	 * 租户英文简称
	 */
	@ApiModelProperty(value="租户英文简称")
	private String tenantEnglishSimpleName;

	@ApiModelProperty(value="联系人名")
	private String contactsName;

	@ApiModelProperty(value="联系人电话")
	private String contactsPhone;

	@ApiModelProperty(value="联系人邮箱")
	private String contactsEmail;

	/**
	 * 是否可用
	 */
	@ApiModelProperty(value="是否可用")
	private Boolean activeFlag;
	/**
	 * 备注
	 */
	@ApiModelProperty(value="备注")
	private String remark;
	/**
	 * 创建时间
	 */
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
	@ApiModelProperty(value="创建时间")
	private Date createTime;
	/**
	 * 创建人
	 */
	@ApiModelProperty(value="创建人")
	private Long createBy;
	/**
	 * 更新时间
	 */
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
	@ApiModelProperty(value="更新时间")
	private Date updateTime;
	/**
	 * 更新人
	 */
	@ApiModelProperty(value="更新人")
	private Long updateBy;
	/**
	 * 是否删除
	 */
	@ApiModelProperty(value="是否删除")
	private Boolean delFlag;

}
