package com.cloud.base.user.service;


import com.cloud.base.modules.xugou.core.model.entity.SecurityAuthority;
import com.cloud.base.user.dto.DeptUserDto;
import com.cloud.base.user.param.*;
import com.cloud.base.user.repository.entity.SysRes;
import com.cloud.base.user.repository.entity.SysRole;
import com.cloud.base.user.repository.entity.SysUser;
import com.cloud.base.user.vo.MenuVo;
import com.cloud.base.user.vo.SysUserVo;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 系统-系统用户表(SysUser)表数据库 服务接口
 *
 * @author lh0811
 * @date 2021/1/18
 */
public interface SysUserService {


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
    PageInfo<SysUserVo> queryUser(SysUserQueryParam param, SysUser sysUser) throws Exception;


    /**
     * 删除用户
     */
    void delUser(Long userId, SysUser sysUser) throws Exception;

    /**
     * 重置用户密码
     */
    void resetPassword(Long userId, SysUser sysUser) throws Exception;

}
