package com.cloud.base.user.param;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Setter
@Getter
public class SysUserRegisterParam implements Serializable {

    /**
     * 用户类型 1-系统管理员 2-商户 3-C端客户
     */
    @NotNull(message = "未上传用户类型")
    @Range(min = 2,max = 3,message = "用户类型不合法")
    @ApiModelProperty(value = "用户类型 2-商户 3-C端客户")
    private Integer userType;

    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名，登录名")
    private String username;

    /**
     * 昵称
     */
    @ApiModelProperty(value = "昵称")
    private String nickName;

    /**
     * 电话
     */
    @NotBlank(message = "未上传联系电话")
    @ApiModelProperty(value = "电话")
    private String phone;

    /**
     * 邮箱
     */
    @ApiModelProperty(value = "邮箱")
    private String eMail;

    /**
     * 用户密码
     */
    @NotBlank(message = "密码不能为空")
    @ApiModelProperty(value = "用户密码")
    private String password;


    /**
     * 用户密码
     */
    @NotBlank(message = "确认密码不能为空")
    @ApiModelProperty(value = "确认密码")
    private String repassword;

}
