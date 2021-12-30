package com.cloud.base.user.expand.logger;

import com.cloud.base.common.core.entity.CommonMethod;
import com.cloud.base.common.logger.adapter.LhitLoggerRoleInfoByUserAdapter;
import com.cloud.base.user.repository.entity.SysRole;
import com.cloud.base.user.repository.entity.SysUser;
import com.cloud.base.user.service.CurrentUserService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author lh0811
 * @date 2021/2/26
 */
@Slf4j
@Component
public class RoleInfoByUser implements LhitLoggerRoleInfoByUserAdapter<SysRole, SysUser> {

    @Resource
    private CurrentUserService currentUserService;

    @Override
    public List<SysRole> getRoleInfoByUserInfo(SysUser user) {
        try {
            return currentUserService.getUserRoleList(user);
        } catch (Exception e) {
            log.error(CommonMethod.getTrace(e));
            return Lists.newArrayList(new SysRole());
        }
    }
}
