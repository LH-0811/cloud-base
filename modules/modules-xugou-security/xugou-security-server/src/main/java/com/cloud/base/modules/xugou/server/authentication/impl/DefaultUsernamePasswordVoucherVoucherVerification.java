package com.cloud.base.modules.xugou.server.authentication.impl;

import com.cloud.base.common.exception.CommonException;
import com.cloud.base.common.response.ServerResponse;
import com.cloud.base.modules.xugou.server.voucher.DefaultUsernamePasswordVoucher;
import com.cloud.base.modules.xugou.core.server.api.authentication.SecurityVoucherVerification;
import com.cloud.base.modules.xugou.core.model.entity.SecurityAuthority;
import com.cloud.base.modules.xugou.core.model.entity.SecurityRes;
import com.cloud.base.modules.xugou.core.model.entity.SecurityRole;
import com.cloud.base.modules.xugou.core.model.entity.SecurityUser;
import com.google.common.collect.Lists;

/**
 * 默认用户校验适配器
 *
 * @author lh0811
 * @date 2021/5/8
 */
public class DefaultUsernamePasswordVoucherVoucherVerification implements SecurityVoucherVerification<DefaultUsernamePasswordVoucher> {
    @Override
    public SecurityAuthority verification(DefaultUsernamePasswordVoucher defaultUsernamePasswordVoucher) throws Exception {
        if (defaultUsernamePasswordVoucher.getUsername().equals("admin") && defaultUsernamePasswordVoucher.getPassword().equals("123456")) {
            SecurityAuthority securityAuthority = new SecurityAuthority();
            securityAuthority.setSecurityUser(new SecurityUser("1", "admin"));
            securityAuthority.setSecurityRoleList(Lists.newArrayList(new SecurityRole(0L,"roleNo","管理员")));
            securityAuthority.setSecurityResList(Lists.newArrayList(SecurityRes.allCodeRes(), SecurityRes.allUrlRes()));
            return securityAuthority;
        }
        throw CommonException.create(ServerResponse.createByError("用户名或密码错误，默认admin-123456"));
    }
}
