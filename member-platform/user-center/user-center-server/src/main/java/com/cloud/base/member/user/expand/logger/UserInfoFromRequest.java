package com.cloud.base.member.user.expand.logger;

import com.cloud.base.core.modules.lh_security.client.service.SecurityClient;
import com.cloud.base.core.modules.lh_security.core.entity.SecurityAuthority;
import com.cloud.base.core.modules.logger.adapter.LhitLoggerUserInfoFromRequestAdapter;
import com.cloud.base.member.user.repository.dao.SysUserDao;
import com.cloud.base.member.user.repository.entity.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @author lh0811
 * @date 2021/2/26
 */
@Component
public class UserInfoFromRequest implements LhitLoggerUserInfoFromRequestAdapter<SysUser> {

    @Autowired
    private SysUserDao sysUserDao;

    @Autowired
    private SecurityClient securityClient;

    @Override
    public SysUser getUserInfoFromRequest(HttpServletRequest request) throws Exception {
        SecurityAuthority securityAuthority = securityClient.tokenToAuthority();
        if (securityAuthority != null && securityAuthority.getSecurityUser() != null) {
            SysUser sysUser = sysUserDao.selectByPrimaryKey(Long.valueOf(securityAuthority.getSecurityUser().getId()));
            return sysUser;
        }
        return new SysUser();
    }
}
