package com.cloud.base.user.repository.entity;

import java.util.Date;
import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import io.swagger.annotations.ApiModelProperty;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.*;

/**
 * 用户中心-部门表(SysDept)实体类
 *
 * @author lh0811
 * @since 2021-08-07 19:01:55
 */
@Setter
@Getter
@Table(name = "sys_dept")
public class SysDept implements Serializable {

    /**
     * 部门id
     */
    @Id
    @KeySql(useGeneratedKeys = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "部门id")
    private Long id;


    @ApiModelProperty(value = "父级部门id")
    private Long parentId;

    /**
     * 部门编号
     */
    @ApiModelProperty(value = "部门编号")
    private String no;
    /**
     * 部门名称
     */
    @ApiModelProperty(value = "部门名称")
    private String name;
    /**
     * 部门备注
     */
    @ApiModelProperty(value = "部门备注")
    private String notes;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private Long createBy;
    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
    /**
     * 更新人
     */
    @ApiModelProperty(value = "更新人")
    private Long updateBy;

    ////////////////////子数据
    @Transient
    @ApiModelProperty(value = "子资源列表")
    private List<SysDept> children;

    @Transient
    @ApiModelProperty(value = "是否是叶子节点")
    private Boolean isLeaf;
    ////////////////////树形数据相关参数
    @Transient
    @ApiModelProperty(value = "父节点")
    private SysDept parent;

    @Transient
    @ApiModelProperty(value = "标题")
    private String title;

    @Transient
    @ApiModelProperty(value = "本节点key")
    private String key;

    @Transient
    @ApiModelProperty(value = "父级key")
    private String pkey;


    ////////////////////树形数据相关参数
    @Transient
    @ApiModelProperty(value = "级联值")
    private String value;

    @Transient
    @ApiModelProperty(value = "级联文本")
    private String label;
}
