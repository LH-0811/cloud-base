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
 * 酉鸡定时任务-管理节点表
 * 
 * @author 
 * @email 
 * @date 2022-04-13 16:54:54
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@TableName("youji2_task_mgr")
public class Youji2TaskMgr implements Serializable {

	/**
	 * 管理节点id主键
	 */
	@TableId(type= IdType.NONE)
	@ApiModelProperty(value="管理节点id主键")
	private Long id;

	/**
	 * 管理节点自定义id
	 */
	@ApiModelProperty(value="管理节点自定义index")
	private Integer mgrIndex;

	/**
	 * 负责监听的mgrId
	 */
	@ApiModelProperty(value="负责监听的mgr index")
	private Integer pingMgrIndex;

	/**
	 * 管理节点应用名称，配置文件中spring.application.name的配置
	 */
	@ApiModelProperty(value="管理节点应用名称，配置文件中spring.application.name的配置")
	private String mgrServerName;
	/**
	 * 管理节点ip
	 */
	@ApiModelProperty(value="管理节点ip")
	private String mgrIp;
	/**
	 * 管理节点端口号
	 */
	@ApiModelProperty(value="管理节点端口号")
	private Integer mgrPort;
	/**
	 * 管理节点是否可用
	 */
	@ApiModelProperty(value="管理节点是否可用")
	private Boolean enableFlag;

	/**
	 * 心跳失败累计数(成功-1，失败+1 到0为止)
	 */
	@ApiModelProperty(value="心跳失败累计数(成功-1，失败+1 到0为止)")
	private Integer beatFailNum;
	/**
	 * 最后一次心跳时间
	 */
	@ApiModelProperty(value="最后一次心跳时间")
	private Date lastHeartBeatTime;

}
