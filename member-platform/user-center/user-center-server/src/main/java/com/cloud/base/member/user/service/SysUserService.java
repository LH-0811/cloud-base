package com.cloud.base.member.user.service;


import com.cloud.base.member.user.repository.entity.SysRes;
import com.cloud.base.member.user.repository.entity.SysRole;
import com.cloud.base.member.user.repository.entity.SysUser;
import com.cloud.base.member.user.param.SysUserUpdatePasswordParam;
import com.cloud.base.member.user.vo.MenuVo;

import java.util.List;

/**
 * 系统-系统用户表(SysUser)表数据库 服务接口
 *
 * @author lh0811
 * @date 2021/1/18
 */
public interface SysUserService {

    /**
     * 完成 获取角色
     *
     * @param userId
     * @return
     * @throws Exception
     */
    List<SysRole> getUserRoleList(Long userId) throws Exception;

    /**
     * 用户修改密码
     *
     * @param param
     * @param sysUser
     * @throws Exception
     */
    void updateUserPassword(SysUserUpdatePasswordParam param, SysUser sysUser) throws Exception;

    /**
     * 获取用户资源树
     *
     * @param sysUser 系统用户
     * @throws Exception 异常
     */
    List<SysRes> getResTreeByUser(SysUser sysUser) throws Exception;

    /**
     * 获取用户菜单树
     *
     * @param sysUser
     * @throws Exception
     */
    List<MenuVo> getMenuTreeByUser(SysUser sysUser) throws Exception;

    /**
     * 获取用户资源列表
     *
     * @param sysUser
     * @throws Exception
     */
    List<SysRes> getResListByUser(SysUser sysUser) throws Exception;

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


}
