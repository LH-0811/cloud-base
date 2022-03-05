package com.cloud.base.user.expand.logger;

import com.cloud.base.common.xugou.client.service.SecurityClient;
import com.cloud.base.common.xugou.core.model.entity.SecurityAuthority;
import com.cloud.base.common.logger.adapter.LhitLoggerUserInfoFromRequestAdapter;
import com.cloud.base.user.repository.dao.SysUserDao;
import com.cloud.base.user.repository.entity.SysUser;
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
            SecurityAuthority securityAuthority = securityClient.tokenToAuthority(true);
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
