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
 * 
 * 
 * @author 
 * @email 
 * @date 2022-04-15 16:18:59
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@TableName("youji2_config")
public class Youji2Config implements Serializable {

	/**
	 * 配置主键
	 */
	@TableId(type= IdType.NONE)
	@ApiModelProperty(value="配置主键")
	private Long id;
	/**
	 * 配置的key
	 */
	@ApiModelProperty(value="配置的key")
	private String cfgKey;
	/**
	 * 配置值
	 */
	@ApiModelProperty(value="配置值")
	private String cfgValue;

}
