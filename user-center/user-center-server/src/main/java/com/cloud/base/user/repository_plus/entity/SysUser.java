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
 * 用户中心-管理员用户表(SysUser)实体类
 *
 * @author lh0811
 * @since 2021-11-03 22:28:44
 */
@Setter
@Getter
@TableName("sys_user")
public class SysUser implements Serializable {

    /**
     * 系统管理员用户
     */
    @TableId(type = IdType.NONE)
    @ApiModelProperty(value="系统管理员用户")
    private Long id;

    /**
     * 用户名
     */
    @ApiModelProperty(value="用户名")
    private String username;
    /**
     * 密码
     */
    @ApiModelProperty(value="密码")
    private String password;
    /**
     * 0-保密 1-男 2-女
     */
    @ApiModelProperty(value="0-保密 1-男 2-女")
    private Integer gender;
    /**
     * 密码盐
     */
    @ApiModelProperty(value="密码盐")
    private String salt;
    /**
     * 昵称
     */
    @ApiModelProperty(value="昵称")
    private String nickName;
    /**
     * 手机号
     */
    @ApiModelProperty(value="手机号")
    private String phone;
    /**
     * 邮箱
     */
    @ApiModelProperty(value="邮箱")
    private String email;
    /**
     * 登录口令
     */
    @ApiModelProperty(value="登录口令")
    private String token;
    /**
     * 是否可用
     */
    @ApiModelProperty(value="是否可用")
    private Boolean activeFlag;
    /**
     * 是否删除
     */
    @ApiModelProperty(value="是否删除")
    private Boolean delFlag;
    /**
     * 最后登录时间
     */
    @ApiModelProperty(value="最后登录时间")
    private Date lastLogin;
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