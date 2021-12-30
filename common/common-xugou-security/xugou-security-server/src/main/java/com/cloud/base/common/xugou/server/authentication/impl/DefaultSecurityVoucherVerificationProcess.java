package com.cloud.base.common.xugou.server.authentication.impl;

import com.cloud.base.common.core.exception.CommonException;
import com.cloud.base.common.core.response.ServerResponse;
import com.cloud.base.common.xugou.core.server.api.authentication.SecurityVoucherVerificationProcess;
import com.cloud.base.common.xugou.core.server.api.authentication.SecurityVoucherVerification;
import com.cloud.base.common.xugou.core.model.entity.SecurityAuthority;
import com.cloud.base.common.xugou.server.token.TokenManager;
import com.cloud.base.common.xugou.core.server.api.voucher.SecurityVoucher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * 默认实现类
 *
 * @author lh0811
 * @date 2021/5/9
 */
@Slf4j
public class DefaultSecurityVoucherVerificationProcess implements SecurityVoucherVerificationProcess {

    // token 管理类
    @Autowired
    private TokenManager tokenManager;

    // spring 容器
    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public String voucherVerificationProcess(SecurityVoucher voucher) throws Exception {
        // 找到凭证类对应的认证器
        SecurityVoucherVerification voucherVerification = find(voucher);
        // 认证凭证
        SecurityAuthority securityAuthority = voucherVerification.verification(voucher);
        // 生成token
        String token = tokenManager.tokenGenerateAndSave(securityAuthority);

        return token;
    }


    private SecurityVoucherVerification find(SecurityVoucher voucher) throws Exception {
        // 找到所有的凭证认证器
        Map<String, SecurityVoucherVerification> voucherVerificationMap = applicationContext.getBeansOfType(SecurityVoucherVerification.class);
        // 获取到当前的凭证类
        Class<? extends SecurityVoucher> aClass = voucher.getClass();
        for (Map.Entry<String, SecurityVoucherVerification> verificationEntry : voucherVerificationMap.entrySet()) {
            SecurityVoucherVerification voucherVerification = verificationEntry.getValue();
            try {
                Method method = voucherVerification.getClass().getMethod("verification", aClass);
                if (method != null) {
                    log.info("匹配到凭证认证器:传入凭证类型:{},与认证适配器类型:{}", aClass.getSimpleName(), voucherVerification.getClass().getSimpleName());
                    return voucherVerification;
                }
            } catch (NoSuchMethodException e) {
                log.info("传入凭证类型:{},与认证适配器类型:{},不匹配", aClass.getSimpleName(), voucherVerification.getClass().getSimpleName());
            }
        }
        throw CommonException.create(ServerResponse.createByError("未找到" + aClass.getSimpleName() + "匹配的验证器"));
    }


}
