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

    @ApiModelProperty(value = "是否是叶子节点")
    private Boolean isLeaf;

    /**
     * 路由
     */
    @ApiModelProperty(value = "路由")
    private String router;

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

}
