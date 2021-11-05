package com.cloud.base.user.repository_plus.entity;

import java.util.Date;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import io.swagger.annotations.ApiModelProperty;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 用户中心-岗位信息(SysPosition)实体类
 *
 * @author lh0811
 * @since 2021-11-03 22:26:52
 */
@Setter
@Getter
@TableName("sys_position")
public class SysPosition implements Serializable {

    /**
     * 岗位id
     */
    @TableId(type = IdType.NONE)
    @ApiModelProperty(value="岗位id")
    private Long id;

    /**
     * 岗位编号
     */
    @ApiModelProperty(value="岗位编号")
    private String no;
    /**
     * 岗位名称
     */
    @ApiModelProperty(value="岗位名称")
    private String name;
    /**
     * 部门备注
     */
    @ApiModelProperty(value="部门备注")
    private String notes;
    /**
     * 创建时间
     */
    @ApiModelProperty(value="创建时间")
    private Date createTime;
    /**
     * 创建人
     */
    @ApiModelProperty(value="创建人")
    private Long createBy;
    /**
     * 更新时间
     */
    @ApiModelProperty(value="更新时间")
    private Date updateTime;
    /**
     * 更新人
     */
    @ApiModelProperty(value="更新人")
    private Long updateBy;


}