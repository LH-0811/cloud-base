package com.cloud.base.common.youji.code.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
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
public class YouJiTaskWorkerEnableUpdateParam implements Serializable {

	/**
	 * 主键
	 */
	@NotNull(message = "未上传工作节点id")
	@ApiModelProperty(value="主键")
	private Long id;

	@NotNull(message = "未上传是否可用")
	@ApiModelProperty(value="是否可用")
	private Boolean enableFlag;

}
