package com.cloud.base.user.repository.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 系统-角色表(SysRole)实体类
 *
 * @author lh0811
 * @since 2021-01-18 10:29:35
 */
@Setter
@Getter
@Table(name="sys_role")
public class SysRole implements Serializable {

    /**
     * 角色id
     */
    @Id
    @KeySql(useGeneratedKeys = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    private Boolean status;
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
