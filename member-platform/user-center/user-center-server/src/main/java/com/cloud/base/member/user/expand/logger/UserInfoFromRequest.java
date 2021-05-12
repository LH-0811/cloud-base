package com.cloud.base.member.user.expand.logger;

import com.cloud.base.core.modules.lh_security.core.entity.SecurityAuthority;
import com.cloud.base.core.modules.lh_security.server.token.TokenManager;
import com.cloud.base.core.modules.logger.adapter.LhitLoggerUserInfoFromRequestAdapter;
import com.cloud.base.member.user.repository.dao.SysUserDao;
import com.cloud.base.member.user.repository.entity.SysUser;
import org.apache.commons.lang3.StringUtils;
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
    private TokenManager tokenManager;

    @Autowired
    private SysUserDao sysUserDao;

    @Override
    public SysUser getUserInfoFromRequest(HttpServletRequest request) throws Exception {
        String lhtoken = request.getHeader("LHTOKEN");
        if (StringUtils.isNotEmpty(lhtoken)) {
            SecurityAuthority securityAuthority = tokenManager.getSecurityAuthorityByToken(lhtoken);
            if (securityAuthority != null && securityAuthority.getSecurityUser() != null) {
                SysUser sysUser = sysUserDao.selectByPrimaryKey(Long.valueOf(securityAuthority.getSecurityUser().getId()));
                return sysUser;
            }
        }
        return new SysUser();
    }
}
