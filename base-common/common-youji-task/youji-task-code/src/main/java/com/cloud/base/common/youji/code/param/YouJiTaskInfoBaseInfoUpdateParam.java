package com.cloud.base.common.youji.code.param;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
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
public class YouJiTaskInfoBaseInfoUpdateParam implements Serializable {

	/**
	 * 主键
	 */
	@NotNull(message = "未上传任务主键id")
	@ApiModelProperty(value="主键")
	private Long id;

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
	 * 执行参数(参数:只支持字符串类型参数)
	 */
	@ApiModelProperty(value="执行参数(参数:只支持字符串类型参数)")
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

}
