package com.cloud.base.user.service;

import com.cloud.base.common.xugou.core.model.entity.SecurityAuthority;
import com.cloud.base.user.dto.DeptUserDto;
import com.cloud.base.user.param.SysDeptUserQueryParam;
import com.cloud.base.user.param.SysUserUpdatePasswordParam;
import com.cloud.base.user.param.UsernamePasswordVerificationParam;
import com.cloud.base.user.repository.entity.SysRes;
import com.cloud.base.user.repository.entity.SysRole;
import com.cloud.base.user.repository.entity.SysUser;
import com.cloud.base.user.vo.MenuVo;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author lh0811
 * @date 2021/12/29
 */
public interface CurrentUserService {


    /**
     * 完成 获取角色
     */
    List<SysRole> getUserRoleList(SysUser sysUser) throws Exception;

    /**
     * 用户修改密码
     */
    void updateUserPassword(SysUserUpdatePasswordParam param, SysUser sysUser) throws Exception;

    /**
     * 获取用户菜单树
     */
    List<MenuVo> getMenuTreeByUser(SysUser sysUser) throws Exception;

    /**
     * 获取用户资源列表
     */
    List<SysRes> getResListByUser(SysUser sysUser) throws Exception;

    /**
     * 获取部门用户信息
     *
     * @param param
     * @return
     * @throws Exception
     */
    PageInfo<DeptUserDto> selectDeptUser(SysDeptUserQueryParam param, SysUser sysUser) throws Exception;


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

}
