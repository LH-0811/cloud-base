package com.cloud.base.user.expand.logger;

import com.cloud.base.core.modules.lh_security.client.service.SecurityClient;
import com.cloud.base.core.modules.lh_security.core.entity.SecurityAuthority;
import com.cloud.base.core.modules.logger.adapter.LhitLoggerUserInfoFromRequestAdapter;
import com.cloud.base.user.repository_plus.dao.SysUserDao;
import com.cloud.base.user.repository_plus.entity.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @author lh0811
 * @date 2021/2/26
 */
@Slf4j
@Component
public class UserInfoFromRequest implements LhitLoggerUserInfoFromRequestAdapter<SysUser> {

    @Autowired
    private SysUserDao sysUserDao;

    @Autowired
    private SecurityClient securityClient;

    @Override
    public SysUser getUserInfoFromRequest(HttpServletRequest request) throws Exception {

        try {
            SecurityAuthority securityAuthority = securityClient.tokenToAuthority();
            if (securityAuthority != null && securityAuthority.getSecurityUser() != null) {
                SysUser sysUser = sysUserDao.getById(Long.valueOf(securityAuthority.getSecurityUser().getId()));
                return sysUser;
            }
        } catch (Exception e) {
            log.info("日志组件获取用户信息失败:{}",e);
        }
        return new SysUser();
    }
}
