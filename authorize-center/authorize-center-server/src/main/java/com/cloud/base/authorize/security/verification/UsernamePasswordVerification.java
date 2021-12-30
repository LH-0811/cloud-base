package com.cloud.base.authorize.security.verification;

import com.cloud.base.common.xugou.core.server.api.voucher.SecurityVoucher;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsernamePasswordVerification implements SecurityVoucher {

    // 用户名
    @NotBlank(message = "未上传 username")
    @ApiModelProperty(value = "username")
    private String username;

    @NotBlank(message = "密码不能为空")
    @ApiModelProperty(value = "密码")
    private String password;
}
