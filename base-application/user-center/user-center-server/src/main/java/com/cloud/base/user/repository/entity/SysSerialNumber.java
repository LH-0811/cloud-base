package com.cloud.base.user.repository.entity;

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
 * 系统管理-序号表 前缀+批次+序号
 * 
 * @author lh0811
 * @email lh0811
 * @date 2022-01-05 18:01:20
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_serial_number")
public class SysSerialNumber implements Serializable {

	/**
	 * 主键
	 */
	@TableId(type= IdType.NONE)
	@ApiModelProperty(value="主键")
	private Long id;
	/**
	 * 业务类型
	 */
	@ApiModelProperty(value="业务类型")
	private String bizType;
	/**
	 * 每次获取多少个序号
	 */
	@ApiModelProperty(value="每次获取多少个序号")
	private Integer step;
	/**
	 * 前缀
	 */
	@ApiModelProperty(value="前缀")
	private String prefix;
	/**
	 * 批次
	 */
	@ApiModelProperty(value="批次")
	private Integer batch;
	/**
	 * 批次长度
	 */
	@ApiModelProperty(value="批次长度")
	private Integer batchLen;
	/**
	 * 当前序号
	 */
	@ApiModelProperty(value="当前序号")
	private Integer currentNum;
	/**
	 * 序号长度
	 */
	@ApiModelProperty(value="序号长度")
	private Integer numLen;
	/**
	 * 批次开始的数值
	 */
	@ApiModelProperty(value="批次开始的数值")
	private Integer startNum;
	/**
	 * 批次结束的数值(到达该值后，增加一个批次)
	 */
	@ApiModelProperty(value="批次结束的数值(到达该值后，增加一个批次)")
	private Integer endNum;
	/**
	 * 批次或序号长度不足时填充符号
	 */
	@ApiModelProperty(value="批次或序号长度不足时填充符号")
	private String fillChar;

}
