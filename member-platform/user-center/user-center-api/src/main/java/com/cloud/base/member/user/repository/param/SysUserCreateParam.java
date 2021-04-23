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
public class SysUserCreateParam implements Serializable {

    /**
     * 用户名
     */
    @NotNull(message = "未上传 用户名")
    @ApiModelProperty(value="用户名",required = true)
    private String name;
    /**
     * 联系电话
     */
    @NotNull(message = "未上传 联系电话")
    @ApiModelProperty(value="联系电话",required = true)
    private String phone;
//    /**
//     * 密码
//     */
//    @NotNull(message = "未上传 密码")
//    @ApiModelProperty(value="密码",required = true)
//    private String password;
//
//    /**
//     * 确认密码
//     */
//    @NotNull(message = "未上传 确认密码")
//    @ApiModelProperty(value="确认密码",required = true)
//    private String repwd;

}
