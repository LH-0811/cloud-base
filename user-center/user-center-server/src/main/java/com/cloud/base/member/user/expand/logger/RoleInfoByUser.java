package com.cloud.base.member.user.expand.logger;

import com.cloud.base.core.common.entity.CommonMethod;
import com.cloud.base.core.modules.logger.adapter.LhitLoggerRoleInfoByUserAdapter;
import com.cloud.base.member.user.repository.entity.SysRole;
import com.cloud.base.member.user.repository.entity.SysUser;
import com.cloud.base.member.user.service.SysUserService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author lh0811
 * @date 2021/2/26
 */
@Slf4j
@Component
public class RoleInfoByUser implements LhitLoggerRoleInfoByUserAdapter<SysRole, SysUser> {

    @Autowired
    private SysUserService sysUserService;

    @Override
    public List<SysRole> getRoleInfoByUserInfo(SysUser user) {
        try {
            return sysUserService.getUserRoleList(user.getId());
        } catch (Exception e) {
            log.error(CommonMethod.getTrace(e));
            return Lists.newArrayList(new SysRole());
        }
    }
}
