package com.cloud.base.user.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;
import java.util.List;

/**
 * @author lh0811
 * @date 2021/5/26
 */
@Getter
@Setter
public class SysUserVo {
    /**
     * 系统用户id
     */
    @ApiModelProperty(value = "系统用户id")
    private Long id;
    /**
     * 租户no
     */
    @ApiModelProperty(value="租户no")
    private String tenantNo;
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

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "部门信息")
    private SysDeptVo deptInfo;

    @ApiModelProperty(value = "岗位信息")
    private List<SysPositionVo> positionList;

    @ApiModelProperty(value = "角色信息")
    private List<SysRoleVo> roleList;


}
