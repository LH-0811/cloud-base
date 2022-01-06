package com.cloud.base.user.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 系统管理-租户信息管理
 * 
 * @author lh0811
 * @email lh0811
 * @date 2022-01-05 18:01:20
 */
@Setter
@Getter
public class SysTenantInfoCreateParam implements Serializable {

	/**
	 * 租户名称
	 */
	@NotBlank(message = "租户名称不能为空")
	@ApiModelProperty(value="租户名称",readOnly = true)
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
	private Boolean activeFlag = Boolean.FALSE;
	/**
	 * 备注
	 */
	@ApiModelProperty(value="备注")
	private String remark;

}
