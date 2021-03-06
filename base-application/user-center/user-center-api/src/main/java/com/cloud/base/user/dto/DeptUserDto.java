package com.cloud.base.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * 部门用户Dto
 *
 * @author lh0811
 * @date 2021/8/7
 */
@Getter
@Setter
public class DeptUserDto {

    // 部门信息

    /**
     * 部门id
     */
    @ApiModelProperty(value="部门id")
    private Long deptId;

    /**
     * 部门编号
     */
    @ApiModelProperty(value="部门编号")
    private String deptNo;
    /**
     * 部门名称
     */
    @ApiModelProperty(value="部门名称")
    private String deptName;

    // 用户信息

    /**
     * 系统用户id
     */
    @ApiModelProperty(value = "系统用户id")
    private Long userId;

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

    @ApiModelProperty(value = "性别 0-保密 1-男 2-女")
    private Integer gender;

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

    @ApiModelProperty(value = "角色id列表")
    private List<Long> roleIdList;

    @ApiModelProperty(value = "岗位id列表")
    private List<Long> positionIdList;


}
