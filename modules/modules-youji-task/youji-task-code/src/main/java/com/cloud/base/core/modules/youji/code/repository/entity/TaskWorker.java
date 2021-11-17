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
 * 定时任务的工作节点信息
 * 
 * @author lh0811
 * @email lh0811
 * @date 2021-11-14 18:54:24
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@TableName("youji_task_worker")
public class TaskWorker implements Serializable {

	/**
	 * 工作节点id主键
	 */
	@TableId(type= IdType.NONE)
	@ApiModelProperty(value="工作节点id主键")
	private Long id;
	/**
	 * 任务id
	 */
	@ApiModelProperty(value="任务id")
	private Long taskId;
	/**
	 * 任务编号
	 */
	@ApiModelProperty(value="任务编号")
	private String taskNo;
	/**
	 * 工作节点应用名称，配置文件中spring.application.name的配置
	 */
	@ApiModelProperty(value="工作节点应用名称，配置文件中spring.application.name的配置")
	private String workerAppName;
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
	 * 工作节点是否可用
	 */
	@ApiModelProperty(value="工作节点是否可用")
	private Boolean enableFlag;

	/**
	 * 工作节点是否在线
	 */
	@ApiModelProperty(value="工作节点是否在线")
	private Boolean onlineFlag;


	/**
	 * 心跳失败累计数(成功-1，失败+1 到0为止)
	 */
	@ApiModelProperty(value = "心跳失败累计数(成功-1，失败+1 到0为止)")
	private Integer beatFailNum;

	/**
	 * 最后一次心跳时间
	 */
	@ApiModelProperty(value="最后一次心跳时间")
	private Date lastHeartBeatTime;


	/**
	 * 执行任务的次数
	 */
	@ApiModelProperty(value="执行任务的次数")
	private Integer execTaskNum  ;//      int        default 0 not null comment '执行任务的次数',

	/**
	 * 最后一次执行任务的时间
	 */
	@ApiModelProperty(value="最后一次执行任务的时间")
	private Date lastExecTime ;//      int                  null comment '最后一次执行任务的时间'
}
