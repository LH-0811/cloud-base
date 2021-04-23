package com.cloud.base.member.user.repository.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 系统-系统用户表(SysUser)实体类
 *
 * @author lh0811
 * @since 2021-01-18 10:29:35
 */
@Setter
@Getter
public class SysUserUpdateParam implements Serializable {

    /**
     * 系统用户id
     */
    @NotNull(message = "未上传 系统用户id")
    @ApiModelProperty(value="系统用户id",required = true)
    private Long id;

    /**
     * 用户名
     */
    @ApiModelProperty(value="用户名")
    private String name;

    /**
     * 联系电话
     */
    @ApiModelProperty(value="联系电话")
    private String phone;


    /**
     * 备注
     */
    @ApiModelProperty(value="备注")
    private String notes;



}
