package com.cloud.base.user.repository_plus.dao.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.base.user.repository_plus.entity.SysUser;
import com.cloud.base.user.repository_plus.dao.SysUserDao;
import com.cloud.base.user.repository_plus.dao.SysUserDaoPlus;
import org.springframework.stereotype.Service;

/**
 * 用户中心-管理员用户表(SysUser)表数据库访问层加强
 *
 * @author lh0811
 * @since 2021-11-05 09:29:02
 */
@Service
public class SysUserDaoPlusImpl extends ServiceImpl<SysUserDao.SysUserMapper,SysUser> implements SysUserDaoPlus {

}