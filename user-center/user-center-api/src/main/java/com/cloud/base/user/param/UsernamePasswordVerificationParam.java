package com.cloud.base.user.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UsernamePasswordVerificationParam {

    // 用户名
    @NotBlank(message = "未上传 username")
    @ApiModelProperty(value = "username")
    private String username;

    @NotBlank(message = "密码不能为空")
    @ApiModelProperty(value = "密码")
    private String password;
}
