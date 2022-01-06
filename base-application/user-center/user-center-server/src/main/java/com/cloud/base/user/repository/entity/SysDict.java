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
 * 系统表-字典表
 * 
 * @author lh0811
 * @email lh0811
 * @date 2022-01-05 18:01:19
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_dict")
public class SysDict implements Serializable {

	/**
	 * 字典id
	 */
	@TableId(type= IdType.NONE)
	@ApiModelProperty(value="字典id")
	private Long id;
	/**
	 * 字典类型
	 */
	@ApiModelProperty(value="字典类型")
	private String type;
	/**
	 * 字典键
	 */
	@ApiModelProperty(value="字典键")
	private String dictKey;
	/**
	 * 字典名
	 */
	@ApiModelProperty(value="字典名")
	private String dictName;
	/**
	 * 字典值
	 */
	@ApiModelProperty(value="字典值")
	private String dictValue;

}
