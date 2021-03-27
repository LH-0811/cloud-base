package com.cloud.base.core.modules.sercurity.defense.adapter;


import com.cloud.base.core.modules.sercurity.defense.pojo.entity.LhitSecurityUserPerms;
import com.cloud.base.core.modules.sercurity.defense.pojo.verification.LhitSecurityUserVerification;

/**
 * 用户认证适配器
 * <p>
 * 该适配器 指定如何验证用户身份
 * <p>
 * 参数是用户提供的凭证
 * 比如 默认的 用户名密码方式
 *
 * @param <T>
 * @see DefaultUsernamePasswordUserVerification
 * <p>
 * <p>
 * 提供默认的使用用户名密码 凭证来验证用户身份的实现 默认的用户名密码 是 user user
 * @see DefaultLhitSecurityUserVerificationImpl
 */
public interface LhitSecurityUserVerificationAdapter<T extends LhitSecurityUserVerification> {

    LhitSecurityUserPerms verification(T verification) throws Exception;

}
