package com.cloud.base.common.xugou.server.voucher;

import com.cloud.base.common.xugou.core.server.api.voucher.SecurityVoucher;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author lh0811
 * @date 2021/5/8
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "用户名密码凭证类")
public class DefaultUsernamePasswordVoucher implements SecurityVoucher {

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "密码")
    private String password;

}
