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
 * 用户中心-部门表(SysDept)实体类
 *
 * @author lh0811
 * @since 2021-11-03 22:27:36
 */
@Setter
@Getter
@TableName("sys_dept")
public class SysDept implements Serializable {

    /**
     * 部门id
     */
    @TableId(type = IdType.NONE)
    @ApiModelProperty(value="部门id")
    private Long id;

    /**
     * 父级部门id
     */
    @ApiModelProperty(value="父级部门id")
    private Long parentId;
    /**
     * 部门编号
     */
    @ApiModelProperty(value="部门编号")
    private String no;
    /**
     * 部门名称
     */
    @ApiModelProperty(value="部门名称")
    private String name;
    /**
     * 是否是叶子结点
     */
    @ApiModelProperty(value="是否是叶子结点")
    private Boolean isLeaf;
    /**
     * 路径
     */
    @ApiModelProperty(value="路径")
    private String router;
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