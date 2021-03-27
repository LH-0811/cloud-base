package com.cloud.base.core.modules.sercurity.defense.adapter;


import com.cloud.base.core.modules.sercurity.defense.pojo.verification.LhitSecurityUserVerification;

/**
 * 根据验证凭证的类型 分发到不同的凭证验证适配器
 *
 * @param <T> 验证凭证
 */
public interface LhitSecurityVerificationDispatchAdapter<T extends LhitSecurityUserVerification> {

    /**
     * 分发凭证类 到 凭证验证器
     *
     * @param verification
     */
    LhitSecurityUserVerificationAdapter dispatchVerification(T verification) throws Exception;

}
