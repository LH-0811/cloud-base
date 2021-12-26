package com.cloud.base.modules.xugou.core.server.api.authentication;


import com.cloud.base.modules.xugou.core.model.entity.SecurityAuthority;
import com.cloud.base.modules.xugou.core.server.api.voucher.SecurityVoucher;

/**
 * 用户认证适配器
 * <p>
 * 该适配器 指定如何验证用户身份
 * <p>
 * 参数是用户提供的凭证
 * 比如 默认的 用户名密码方式
 *
 * @param <T>
 * <p>
 * <p>
 * 提供默认的使用用户名密码 凭证来验证用户身份的实现 默认的用户名密码 是 user user
 */
public interface SecurityVoucherVerification<T extends SecurityVoucher> {

    SecurityAuthority verification(T verification) throws Exception;

}
