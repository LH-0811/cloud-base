package com.cloud.base.core.modules.youji.code.param;

import com.cloud.base.core.common.entity.CommonEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 定时任务表
 * 
 * @author lh0811
 * @email lh0811
 * @date 2021-11-13 14:59:16
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class YouJiTaskInfoLogQueryParam implements Serializable {


	/**
	 * 任务编号
	 */
	@ApiModelProperty(value="任务编号")
	private String taskNo;

	/**
	 * 任务名称
	 */
	@ApiModelProperty(value="任务名称")
	private String taskName;

	/**
	 * 联系人
	 */
	@ApiModelProperty(value="联系人")
	private String contactsName;
	/**
	 * 联系人电话
	 */
	@ApiModelProperty(value="联系人电话")
	private String contactsPhone;
	/**
	 * 联系人邮箱
	 */
	@ApiModelProperty(value="联系人邮箱")
	private String contactsEmail;

	@ApiModelProperty(value="执行时间(起)")
	private Date execTimeLow;

	@ApiModelProperty(value="执行时间(止)")
	private Date execTimeUp;

	/**
	 * 是否执行完成
	 */
	@ApiModelProperty(value="是否执行完成")
	private Boolean finishFlag;

	@ApiModelProperty(value = "页码")
	private Integer pageNum = CommonEntity.pageNum;

	@ApiModelProperty(value = "每页条数")
	private Integer pageSize = CommonEntity.pageSize;

}
