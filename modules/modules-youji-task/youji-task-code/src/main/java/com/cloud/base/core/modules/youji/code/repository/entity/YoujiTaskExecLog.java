package com.cloud.base.core.modules.youji.code.repository.entity;

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
 * @author lh0811
 * @email lh0811
 * @date 2021-11-17 20:34:07
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@TableName("youji_task_exec_log")
public class YoujiTaskExecLog implements Serializable {

	/**
	 * 主键id
	 */
	@TableId(type= IdType.NONE)
	@ApiModelProperty(value="主键id")
	private Long id;
	/**
	 * 工作节点id
	 */
	@ApiModelProperty(value="工作节点id")
	private Long workerId;
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
	 * 任务执行表达式
	 */
	@ApiModelProperty(value="任务执行表达式")
	private String corn;
	/**
	 * 任务执行触发的url地址
	 */
	@ApiModelProperty(value="任务执行触发的url地址")
	private String taskUrl;
	/**
	 * 应用上下文执行中对应的全限定类名
	 */
	@ApiModelProperty(value="应用上下文执行中对应的全限定类名")
	private String taskBeanName;
	/**
	 * 应用上下文中对应的方法名，或者url的请求类型
	 */
	@ApiModelProperty(value="应用上下文中对应的方法名，或者url的请求类型")
	private String taskMethod;
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
