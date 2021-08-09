package com.cloud.base.user.service;


import com.cloud.base.core.modules.lh_security.core.entity.SecurityAuthority;
import com.cloud.base.user.dto.DeptUserDto;
import com.cloud.base.member.user.param.*;
import com.cloud.base.user.repository.entity.SysRes;
import com.cloud.base.user.repository.entity.SysRole;
import com.cloud.base.user.repository.entity.SysUser;
import com.cloud.base.user.vo.MenuVo;
import com.cloud.base.user.param.*;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 系统-系统用户表(SysUser)表数据库 服务接口
 *
 * @author lh0811
 * @date 2021/1/18
 */
public interface SysUserService {

    /**
     * 获取部门角色信息
     *
     * @param param
     * @return
     * @throws Exception
     */
    PageInfo<DeptUserDto> selectDeptUser(SysDeptUserQueryParam param, SysUser sysUser) throws Exception;

    /**
     * 完成 获取角色
     */
    List<SysRole> getUserRoleList(Long userId) throws Exception;

    /**
     * 用户修改密码
     */
    void updateUserPassword(SysUserUpdatePasswordParam param, Long userId) throws Exception;

    /**
     * 获取用户资源树
     */
    List<SysRes> getResTreeByUser(Long userId) throws Exception;

    /**
     * 获取用户菜单树
     */
    List<MenuVo> getMenuTreeByUser(Long userId) throws Exception;

    /**
     * 获取用户资源列表
     *
     */
    List<SysRes> getResListByUser(Long userId) throws Exception;

    /**
     * 根据用户id获取用户信息
     *
     * @param userId
     * @return
     */
    SysUser getUserByUserId(Long userId) throws Exception;

    /**
     * 通过用户名 密码获取用户信息
     *
     * @param username
     * @param password
     * @return
     * @throws Exception
     */
    SysUser getUserByUsernameAndPassword(String username, String password) throws Exception;


    /**
     * 验证用户口令
     *
     * @param param
     * @return
     * @throws Exception
     */
    SecurityAuthority verification(UsernamePasswordVerificationParam param) throws Exception;

    /**
     * 注册用户
     *
     * @throws Exception
     */
    void registerUser(SysUserRegisterParam param) throws Exception;


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
