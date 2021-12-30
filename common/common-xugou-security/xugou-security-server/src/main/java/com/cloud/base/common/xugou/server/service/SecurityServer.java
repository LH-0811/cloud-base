package com.cloud.base.common.xugou.server.service;

import com.cloud.base.common.xugou.core.model.param.TokenParam;
import com.cloud.base.common.xugou.core.model.vo.AuthenticationVo;
import com.cloud.base.common.xugou.core.server.api.voucher.SecurityVoucher;

/**
 * @author lh0811
 * @date 2021/5/23
 */
public interface SecurityServer {
    /**
     * 授权服务 用户授权
     */
    AuthenticationVo authorize(SecurityVoucher param) throws Exception;

    /**
     * 授权服务 销毁token
     */
    void tokenDestroy(TokenParam param) throws Exception;
}
