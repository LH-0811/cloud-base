package com.cloud.base.user.repository_plus.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import io.swagger.annotations.ApiModelProperty;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 用户中心-用户岗位信息关系表(SysUserPositionRel)实体类
 *
 * @author lh0811
 * @since 2021-11-03 22:30:33
 */
@Setter
@Getter
@TableName("sys_user_position_rel")
@NoArgsConstructor
@AllArgsConstructor
public class SysUserPositionRel implements Serializable {

    /**
     * 用户岗位关系id
     */
    @TableId(type = IdType.NONE)
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
