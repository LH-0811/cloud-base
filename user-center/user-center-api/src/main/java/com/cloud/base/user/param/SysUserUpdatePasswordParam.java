package com.cloud.base.user.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author lh0811
 * @date 2021/1/25
 */
@Getter
@Setter
public class SysUserUpdatePasswordParam implements Serializable {

    @NotNull(message = "未上传 旧密码")
    @ApiModelProperty(value = "旧密码",required = true)
    private String oldPwd;

    @NotNull(message = "未上传 新密码")
    @ApiModelProperty(value = "新密码",required = true)
    private String newPwd;

    @NotNull(message = "未上传 重复密码")
    @ApiModelProperty(value = "重复密码",required = true)
    private String rePwd;

}
