package com.cloud.base.common.xugou.core.server.api.voucher;

import io.swagger.annotations.ApiModelProperty;

/**
 * 凭证类接口
 *
 * @author lh0811
 * @date 2021/5/8
 */
public abstract class SecurityVoucher {

    @ApiModelProperty(value = "客户端类型,登录时上传")
    public String clientType = "unknown";


    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }
}
