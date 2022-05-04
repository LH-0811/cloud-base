package com.cloud.base.common.youji.cronjob.core.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 定时任务表
 * 
 * @author lh0811
 * @email lh0811
 * @date 2021-11-13 14:59:16
 */
@Valid
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Youji2TaskInfoCronUpdateParam implements Serializable {

	/**
	 * 主键
	 */
	@NotNull(message = "未上传任务主键id")
	@ApiModelProperty(value="主键")
	private Long id;

	@NotBlank(message = "未上传执行计划表达式")
	@ApiModelProperty(value="执行计划表达式")
	private String cron;

}
