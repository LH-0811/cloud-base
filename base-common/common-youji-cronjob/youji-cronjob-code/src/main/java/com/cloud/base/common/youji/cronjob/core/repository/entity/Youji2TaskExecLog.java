package com.cloud.base.common.youji.cronjob.core.repository.entity;

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
 * 定时任务表日志表
 * 
 * @author 
 * @email 
 * @date 2022-04-13 16:54:53
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@TableName("youji2_task_exec_log")
public class Youji2TaskExecLog implements Serializable {

	/**
	 * 主键id
	 */
	@TableId(type= IdType.NONE)
	@ApiModelProperty(value="主键id")
	private Long id;
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
	 * 工作节点id
	 */
	@ApiModelProperty(value="工作节点id")
	private Long workerId;
	/**
	 * 工作节点ip
	 */
	@ApiModelProperty(value="工作节点ip")
	private String workerIp;
	/**
	 * 工作节点端口号
	 */
	@ApiModelProperty(value="工作节点端口号")
	private Integer workerPort;
	/**
	 * 参数:只支持字符串类型参数
	 */
	@ApiModelProperty(value="参数:只支持字符串类型参数")
	private String taskParam;
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
	/**
	 * 是否执行完成
	 */
	@ApiModelProperty(value="是否执行完成")
	private Boolean finishFlag;
	/**
	 * 任务执行结果信息
	 */
	@ApiModelProperty(value="任务执行结果信息")
	private String resultMsg;
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

}
