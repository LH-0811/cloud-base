package com.cloud.base.member.user.expand.logger;

import com.cloud.base.core.modules.logger.adapter.LhitLoggerUserInfoFromRequestAdapter;
import com.cloud.base.core.modules.sercurity.defense.adapter.LhitSecurityTokenManagerAdapter;
import com.cloud.base.core.modules.sercurity.defense.pojo.entity.LhitSecurityRole;
import com.cloud.base.core.modules.sercurity.defense.pojo.entity.LhitSecurityUserPerms;
import com.cloud.base.core.modules.sercurity.defense.pojo.user.LhitSecurityUser;
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
    private LhitSecurityTokenManagerAdapter<LhitSecurityUser, LhitSecurityRole> lhitSecurityTokenManagerAdapter;

    @Autowired
    private SysUserDao sysUserDao;

    @Override
    public SysUser getUserInfoFromRequest(HttpServletRequest request) {
        String lhtoken = request.getHeader("LHTOKEN");
        if (StringUtils.isNotEmpty(lhtoken)) {
            LhitSecurityUserPerms<LhitSecurityRole, LhitSecurityUser> permsByToken = lhitSecurityTokenManagerAdapter.getPermsByToken(lhtoken);
            if (permsByToken != null && permsByToken.getUser() != null) {
                return sysUserDao.selectByPrimaryKey(Long.parseLong(permsByToken.getUser().getUserId()));
            }
        }
        return new SysUser();
    }
}
