package com.cloud.base.member.user.service;


import com.cloud.base.core.modules.lh_security.core.entity.SecurityAuthority;
import com.cloud.base.member.user.param.SysUserRegisterParam;
import com.cloud.base.member.user.param.UsernamePasswordVerificationParam;
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
}
