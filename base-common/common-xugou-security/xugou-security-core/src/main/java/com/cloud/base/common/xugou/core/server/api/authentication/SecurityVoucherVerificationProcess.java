package com.cloud.base.common.xugou.core.server.api.authentication;

import com.cloud.base.common.xugou.core.server.api.voucher.SecurityVoucher;

/**
 * 安全框架 凭证认证过程
 * <p>
 * 接收到凭证后 找到对应的凭证认证方法
 * 认证完成后 根据认证完成后的用户权限信息类 完成token颁发
 *
 * @author lh0811
 * @date 2021/5/9
 */
public interface SecurityVoucherVerificationProcess {


    /**
     * 处理凭证类
     *
     * @param voucher
     * @return
     * @throws Exception
     */
    String voucherVerificationProcess(SecurityVoucher voucher) throws Exception;


}
