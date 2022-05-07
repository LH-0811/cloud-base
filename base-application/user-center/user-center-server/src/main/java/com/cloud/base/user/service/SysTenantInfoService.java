package com.cloud.base.user.service;

import com.cloud.base.common.xugou.core.model.entity.SecurityAuthority;
import com.cloud.base.user.param.*;
import com.cloud.base.user.repository.entity.SysTenantInfo;
import com.cloud.base.user.repository.entity.SysUser;
import com.cloud.base.user.vo.SysTenantInfoVo;
import com.cloud.base.user.vo.SysUserVo;
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
    SysTenantInfoVo tenantInfoCreate(SysTenantInfoCreateParam param, SecurityAuthority securityAuthority) throws Exception;

    /**
     * 删除租户信息(软删)
     */
    void tenantInfoDelete(Long tenantInfoId, SecurityAuthority securityAuthority) throws Exception;

    /**
     * 更新租户信息
     */
    SysTenantInfoVo tenantInfoUpdate(SysTenantInfoUpdateParam param, SecurityAuthority securityAuthority) throws Exception;

    /**
     * 查询租户信息
     */
    PageInfo<SysTenantInfoVo> tenantInfoQuery(SysTenantInfoQueryParam param, SecurityAuthority securityAuthority) throws Exception;

    /**
     * 创建该租户的系统管理员
     */
    SysUserVo getTenantMgrUser(Long tenantId, SecurityAuthority securityAuthority) throws Exception;

    /**
     * 创建租户管理员
     */
    SysUserVo genTenantMgrUser(SysTenantMgrUserCreateParam param, SecurityAuthority securityAuthority) throws Exception;

    /**
     * 更新租户管理员信息
     */
    void updateTenantMgrUserInfo(SysTenantMgrUserUpdateParam param, SecurityAuthority securityAuthority) throws Exception;
}
