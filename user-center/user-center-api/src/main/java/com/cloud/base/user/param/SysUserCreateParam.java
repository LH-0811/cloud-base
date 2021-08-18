package com.cloud.base.user.param;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 系统-系统用户表(SysUser)实体类
 *
 * @author lh0811
 * @since 2021-01-18 10:29:35
 */
@Setter
@Getter
public class SysUserCreateParam implements Serializable {


    /**
     * 昵称
     */
    @ApiModelProperty(value = "昵称")
    private String nickName;

    /**
     * 所属部门id
     */
    @NotBlank(message = "未上传 所属部门id")
    @ApiModelProperty(value = "所属部门id",required = true)
    private Long deptId;

    /**
     * 电话
     */
    @NotBlank(message = "未上传 电话")
    @ApiModelProperty(value = "电话",required = true)
    private String phone;

    /**
     * 邮箱
     */
    @ApiModelProperty(value = "邮箱")
    private String eMail;

    /**
     * 用户名
     */
    @NotBlank(message = "未上传 用户名")
    @ApiModelProperty(value = "用户名",required = true)
    private String username;


    /**
     * 性别 0-保密 1-男 2-女
     */
    @ApiModelProperty(value = "性别 0-保密 1-男 2-女")
    private Integer gender;

    /**
     * 岗位id列表
     */
    @ApiModelProperty(value = "岗位id列表")
    private List<Long> positionIdList;

    /**
     * 岗位id列表
     */
    @ApiModelProperty(value = "角色id列表")
    private List<Long> roleIdList;

    /**
     * 是否可用
     */
    @ApiModelProperty(value = "是否可用")
    private Boolean activeFlag;








}
