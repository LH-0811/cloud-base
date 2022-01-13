package com.cloud.base.user.service;

import com.cloud.base.user.param.*;
import com.cloud.base.user.repository.entity.SysTenantInfo;
import com.cloud.base.user.repository.entity.SysUser;
import com.github.pagehelper.PageInfo;

/**
 * 租户管理服务接口
 *
 * @author lh0811
 * @date 2022/1/6
 */
public interface SysTenantInfoService {


    /**
     * 创建租户信息
     */
    SysTenantInfo tenantInfoCreate(SysTenantInfoCreateParam param, SysUser sysUser) throws Exception;

    /**
     * 删除租户信息(软删)
     */
    void tenantInfoDelete(Long tenantInfoId, SysUser sysUser) throws Exception;

    /**
     * 更新租户信息
     */
    SysTenantInfo tenantInfoUpdate(SysTenantInfoUpdateParam param, SysUser sysUser) throws Exception;

    /**
     * 查询租户信息
     */
    PageInfo<SysTenantInfo> tenantInfoQuery(SysTenantInfoQueryParam param, SysUser sysUser) throws Exception;

    /**
     * 创建该租户的系统管理员
     */
    SysUser getTenantMgrUser(Long tenantId, SysUser sysUser) throws Exception;

    /**
     * 创建租户管理员
     */
    SysUser genTenantMgrUser(SysTenantMgrUserCreateParam param, SysUser sysUser) throws Exception;

    /**
     * 更新租户管理员信息
     */
    void updateTenantMgrUserInfo(SysTenantMgrUserUpdateParam param, SysUser currentSysUser) throws Exception;
}
