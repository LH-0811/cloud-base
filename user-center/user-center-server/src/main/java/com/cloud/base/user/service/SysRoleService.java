package com.cloud.base.user.service;

import com.cloud.base.user.param.SysRoleCreateParam;
import com.cloud.base.user.param.SysRoleQueryParam;
import com.cloud.base.user.param.SysRoleResSaveParam;
import com.cloud.base.user.param.SysRoleUpdateParam;
import com.cloud.base.user.repository.entity.SysRes;
import com.cloud.base.user.repository.entity.SysRole;
import com.cloud.base.user.repository.entity.SysUser;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 角色管理服务接口
 *
 * @author lh0811
 * @date 2021/8/17
 */
public interface SysRoleService {


// //////////////// 角色管理

    /**
     * 创建系统角色信息
     */
    void createRole(SysRoleCreateParam param, SysUser sysUser) throws Exception;

    /**
     * 修改系统角色信息
     */
    void updateRole(SysRoleUpdateParam param, SysUser sysUser) throws Exception;

    /**
     * 删除系统角色信息
     */
    void deleteRole(Long roleId, SysUser sysUser) throws Exception;

    /**
     * 查询系统角色信息
     */
    PageInfo<SysRole> queryRole(SysRoleQueryParam param, SysUser sysUser) throws Exception;

    /**
     * 获取角色列表
     *
     * @return
     * @throws Exception
     */
    List<SysRole> getRoleList() throws Exception;

    /**
     * 保存角色权限
     */
    void saveRoleRes(SysRoleResSaveParam param, SysUser sysUser) throws Exception;

    /**
     * 查询角色资源列表
     */
    List<SysRes> getSysResListByRoleId(Long roleId, SysUser sysUser) throws Exception;


}
