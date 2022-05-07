package com.cloud.base.common.youji.cronjob.core.param;

import com.cloud.base.common.core.entity.CommonEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

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
public class Youji2TaskInfoQueryParam implements Serializable {

	/**
	 * （1-通过URL触发 2-遍历容器中的bean触发）
	 */
	@ApiModelProperty(value="（1-通过URL触发 2-遍历容器中的bean触发）")
	private String taskType;

	/**
	 * 执行方式 (1-单节点执行 2-全节点执行)
	 */
	@ApiModelProperty(value="执行方式 (1-单节点执行 2-全节点执行)")
	private String execType;

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


	@ApiModelProperty(value = "页码")
	private Integer pageNum = CommonEntity.pageNum;

	@ApiModelProperty(value = "每页条数")
	private Integer pageSize = CommonEntity.pageSize;

}