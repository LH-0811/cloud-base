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
 * 系统-角色表(SysRole)实体类
 *
 * @author lh0811
 * @since 2021-11-03 22:29:26
 */
@Setter
@Getter
@TableName("sys_role")
public class SysRole implements Serializable {

    /**
     * 角色id
     */
    @TableId(type = IdType.NONE)
    @ApiModelProperty(value="角色id")
    private Long id;

    /**
     * 角色名称
     */
    @ApiModelProperty(value="角色名称")
    private String name;
    /**
     * 角色编码
     */
    @ApiModelProperty(value="角色编码")
    private String no;
    /**
     * 是否可用
     */
    @ApiModelProperty(value="是否可用")
    private Boolean activeFlag;
    /**
     * 排序（升序）
     */
    @ApiModelProperty(value="排序（升序）")
    private Integer sortNum;
    /**
     * 角色备注
     */
    @ApiModelProperty(value="角色备注")
    private String notes;
    /**
     * 创建时间
     */
    @ApiModelProperty(value="创建时间")
    private Date createTime;
    /**
     * 更新时间
     */
    @ApiModelProperty(value="更新时间")
    private Date updateTime;
    /**
     * 创建人
     */
    @ApiModelProperty(value="创建人")
    private Long createBy;
    /**
     * 更新人
     */
    @ApiModelProperty(value="更新人")
    private Long updateBy;


}