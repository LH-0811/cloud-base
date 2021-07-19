package com.cloud.base.member.user.repository.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 系统-系统用户表(SysUser)实体类
 *
 * @author lh0811
 * @since 2021-01-18 10:29:35
 */
@Setter
@Getter
@Table(name = "sys_user")
public class SysUser implements Serializable {

    /**
     * 系统用户id
     */
    @Id
    @KeySql(useGeneratedKeys = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "系统用户id")
    private Long id;

    /**
     * 用户类型 1-系统管理员 2-商户 3-C端客户
     */
    @ApiModelProperty(value = "用户类型 1-系统管理员 2-商户 3-C端客户")
    private Integer userType;

    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名")
    private String username;
    /**
     * 用户密码
     */
    @JsonIgnore
    @ApiModelProperty(value = "用户密码")
    private String password;
    /**
     * 密码盐
     */
    @ApiModelProperty(value = "密码盐")
    private String salt;

    /**
     * 昵称
     */
    @ApiModelProperty(value = "昵称")
    private String nickName;

    /**
     * 电话
     */
    @ApiModelProperty(value = "电话")
    private String phone;

    /**
     * 邮箱
     */
    @ApiModelProperty(value = "邮箱")
    private String eMail;

    /**
     * 是否可用
     */
    @ApiModelProperty(value = "是否可用")
    private Boolean activeFlag;

    /**
     * 是否已经删除
     */
    @ApiModelProperty(value = "是否已经删除")
    private Boolean delFlag;

    /**
     * 最后登录时间
     */
    @ApiModelProperty(value = "最后登录时间")
    private Date lastLogin;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private Long createBy;
    /**
     * 更新人
     */
    @ApiModelProperty(value = "更新人")
    private Long updateBy;

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public enum UserType {
        ADMIN(1, "管理员用户"),
        MERCHANT(2, "商户用户"),
        CLIENT(3, "客户端用户");

        private Integer code;
        private String name;
    }


}
