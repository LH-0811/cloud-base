package com.cloud.base.user.param;

import com.cloud.base.core.common.entity.CommonEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

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
public class SysUserQueryParam implements Serializable {


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
    private String email;

    /**
     * 是否可用
     */
    @ApiModelProperty(value = "是否可用")
    private Boolean activeFlag;

    /**
     * 最后登录时间
     */
    @ApiModelProperty(value = "最后登录时间最早")
    private Date lastLoginLow;


    /**
     * 最后登录时间
     */
    @ApiModelProperty(value = "最后登录时间最晚")
    private Date lastLoginUp;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间最早")
    private Date createTimeLow;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间最晚")
    private Date createTimeUp;


    @ApiModelProperty(value="页码 默认1")
    private Integer pageNum = CommonEntity.pageNum;

    @ApiModelProperty(value="每页条数 默认15")
    private Integer pageSize = CommonEntity.pageSize;

}
