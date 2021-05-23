package com.cloud.base.core.modules.lh_security.server.service;

import com.cloud.base.core.common.response.ServerResponse;
import com.cloud.base.core.common.vo.LoginVo;
import com.cloud.base.core.modules.lh_security.core.param.TokenParam;
import com.cloud.base.core.modules.lh_security.server.voucher.SecurityVoucher;

/**
 * @author lh0811
 * @date 2021/5/23
 */
public interface SecurityService {
    /**
     * 授权服务 用户授权
     */
    LoginVo authorize(SecurityVoucher param) throws Exception;

    /**
     * 授权服务 销毁token
     */
    void tokenDestroy(TokenParam param) throws Exception;
}
