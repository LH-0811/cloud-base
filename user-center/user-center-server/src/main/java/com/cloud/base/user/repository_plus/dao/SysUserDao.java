package com.cloud.base.user.repository_plus.dao;

import com.cloud.base.user.repository_plus.entity.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Repository;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
/**
 * 用户中心-管理员用户表(SysUser)表数据库访问层加强
 *
 * @author lh0811
 * @since 2021-11-05 09:29:02
 */
public interface SysUserDao extends IService<SysUser> {
    @Repository
    public interface SysUserMapper extends BaseMapper<SysUser> {

    }
}