package com.cloud.base.member.user.service;

import com.cloud.base.member.user.repository.entity.SysRegion;
import com.cloud.base.member.user.repository.entity.SysRes;
import com.cloud.base.member.user.repository.entity.SysRole;
import com.cloud.base.member.user.repository.entity.SysUser;
import com.cloud.base.member.user.repository.param.*;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author lh0811
 * @date 2021/3/1
 */
public interface SysAdminService {

// //////////////// 用户管理


    /**
     * 创建用户
     *
     * @param param
     * @param sysUser
     * @throws Exception
     */
    void createUser(SysUserCreateParam param, SysUser sysUser) throws Exception;

    /**
     * 修改用户
     *
     * @param param
     * @param sysUser
     * @throws Exception
     */
    void updateUser(SysUserUpdateParam param, SysUser sysUser) throws Exception;

    /**
     * 查询用户
     *
     * @param param
     * @param sysUser
     * @return
     * @throws Exception
     */
    PageInfo<SysUser> queryUser(SysUserQueryParam param, SysUser sysUser) throws Exception;

    /**
     * 设置用户角色列表
     */
    void setUserRoleList(SysUserRoleSetParam param, SysUser sysUser) throws Exception;

    /**
     * 删除用户
     */
    void delUser(Long userId, SysUser sysUser) throws Exception;

// //////////////// 资源管理
    /**
     * 创建权限
     */
    void createRes(SysResCreateParam param, SysUser sysUser) throws Exception;

    /**
     * 删除权限信息
     */
    void deleteRes(Long resId, SysUser sysUser) throws Exception;

    /**
     * 获取全部资源树
     */
    List<SysRes> getAllResTree() throws Exception;


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
