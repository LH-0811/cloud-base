package com.cloud.base.member.user.expand.security.verification.username_password;

import com.cloud.base.core.modules.sercurity.defense.pojo.verification.LhitSecurityUserVerification;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsernamePasswordVerification implements LhitSecurityUserVerification {

    // 用户名
    @NotBlank(message = "未上传 username")
    @ApiModelProperty(value = "username")
    private String username;

    @NotBlank(message = "密码不能为空")
    @ApiModelProperty(value = "密码")
    private String password;
}
