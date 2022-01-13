package com.cloud.base.user.repository.dao;

import com.cloud.base.user.repository.entity.SysTenantInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cloud.base.user.repository.entity.SysUser;

/**
 * 系统管理-租户信息管理
 *
 * @author lh0811
 * @email lh0811
 * @date 2022-01-05 18:01:20
 */
public interface SysTenantInfoDao extends IService<SysTenantInfo> {

    /**
     * 获取当前租户的系统管理员
     */
    SysUser getTenantSysMgrUser(String tenantNo) throws Exception;
}
