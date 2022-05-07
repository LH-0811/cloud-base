package com.cloud.base.user.service;


import com.cloud.base.common.xugou.core.model.entity.SecurityAuthority;
import com.cloud.base.user.param.*;
import com.cloud.base.user.repository.entity.SysUser;
import com.cloud.base.user.vo.SysUserVo;
import com.github.pagehelper.PageInfo;

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
     * @param securityAuthority
     * @throws Exception
     */
    void createUser(SysUserCreateParam param, SecurityAuthority securityAuthority) throws Exception;

    /**
     * 修改用户
     *
     * @param param
     * @param securityAuthority
     * @throws Exception
     */
    void updateUser(SysUserUpdateParam param, SecurityAuthority securityAuthority) throws Exception;

    /**
     * 查询用户
     *
     * @param param
     * @param securityAuthority
     * @return
     * @throws Exception
     */
    PageInfo<SysUserVo> queryUser(SysUserQueryParam param, SecurityAuthority securityAuthority) throws Exception;


    /**
     * 删除用户
     */
    void delUser(Long userId, SecurityAuthority securityAuthority) throws Exception;

    /**
     * 重置用户密码
     */
    void resetPassword(Long userId, SecurityAuthority securityAuthority) throws Exception;

}
