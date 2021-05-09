package com.cloud.base.core.modules.sercurity.defense.adapter.impl;

import com.cloud.base.core.common.exception.CommonException;
import com.cloud.base.core.common.response.ServerResponse;
import com.cloud.base.core.modules.sercurity.defense.adapter.LhitSecurityUserVerificationAdapter;
import com.cloud.base.core.modules.sercurity.defense.adapter.LhitSecurityVerificationDispatchAdapter;
import com.cloud.base.core.modules.sercurity.defense.mgr.UserVerificationCollection;
import com.cloud.base.core.modules.sercurity.defense.pojo.verification.LhitSecurityUserVerification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Method;
import java.util.Map;

@Slf4j
public class DefaultLhitSecurityVerificationDispatchAdapter implements LhitSecurityVerificationDispatchAdapter<LhitSecurityUserVerification> {


    @Autowired
    private UserVerificationCollection userVerificationCollection;

    @Override
    public LhitSecurityUserVerificationAdapter dispatchVerification(LhitSecurityUserVerification verification) throws Exception {

        // 获取到全部的验证适配器类
        Map<String, LhitSecurityUserVerificationAdapter> userVerificationBeans = userVerificationCollection.getUserVerificationBeans();

        // 获取到当前的凭证类
        Class<? extends LhitSecurityUserVerification> aClass = verification.getClass();

        for (Map.Entry<String, LhitSecurityUserVerificationAdapter> adapterEntry : userVerificationBeans.entrySet()) {

            LhitSecurityUserVerificationAdapter adapter = adapterEntry.getValue();
            try {
                Method method = adapter.getClass().getMethod("verification", aClass);
                if (method != null) {
                    log.info("匹配到凭证认证器:传入凭证类型:{},与认证适配器类型:{}", aClass.getSimpleName(), adapter.getClass().getSimpleName());
                    return adapter;
                }
            } catch (NoSuchMethodException e) {
                log.info("传入凭证类型:{},与认证适配器类型:{},不匹配", aClass.getSimpleName(), adapter.getClass().getSimpleName());
            }
        }
        throw CommonException.create(ServerResponse.createByError("未找到" + aClass.getSimpleName() + "匹配的验证器"));
    }

}
