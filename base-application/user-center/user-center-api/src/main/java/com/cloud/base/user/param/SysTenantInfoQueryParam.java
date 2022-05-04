package com.cloud.base.user.param;

import com.cloud.base.common.core.entity.CommonEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 系统管理-租户信息管理
 * 
 * @author lh0811
 * @email lh0811
 * @date 2022-01-05 18:01:20
 */
@Setter
@Getter
public class SysTenantInfoQueryParam implements Serializable {

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
	 * 是否可用
	 */
	@ApiModelProperty(value="是否可用")
	private Boolean activeFlag;

	@ApiModelProperty(value="联系人名")
	private String contactsName;

	@ApiModelProperty(value="联系人电话")
	private String contactsPhone;

	@ApiModelProperty(value="联系人邮箱")
	private String contactsEmail;

	/**
	 * 创建时间
	 */
	@ApiModelProperty(value="创建时间(起)")
	private String createTimeLow;

	/**
	 * 创建时间
	 */
	@ApiModelProperty(value="创建时间(止)")
	private String createTimeUp;

	@ApiModelProperty(value = "页码 默认1")
	private Integer pageNum= CommonEntity.pageNum;

	@ApiModelProperty(value = "每页条数 默认15")
	private Integer pageSize = CommonEntity.pageSize;

}
