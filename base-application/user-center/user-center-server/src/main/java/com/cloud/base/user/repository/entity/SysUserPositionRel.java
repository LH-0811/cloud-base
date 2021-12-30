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
 * 用户中心-用户岗位信息关系表
 * 
 * @author lh0811
 * @email lh0811
 * @date 2021-11-10 11:01:40
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_user_position_rel")
public class SysUserPositionRel implements Serializable {

	/**
	 * 用户岗位关系id
	 */
	@TableId(type= IdType.NONE)
	@ApiModelProperty(value="用户岗位关系id")
	private Long id;
	/**
	 * 用户id
	 */
	@ApiModelProperty(value="用户id")
	private Long userId;
	/**
	 * 岗位id
	 */
	@ApiModelProperty(value="岗位id")
	private Long positionId;

}
