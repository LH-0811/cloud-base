package com.cloud.base.user.repository.dao.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.base.user.repository.dao.SysUserDao;
import com.cloud.base.user.repository.dao.mapper.SysUserMapper;
import com.cloud.base.user.repository.entity.SysUser;
import org.springframework.stereotype.Service;

/**
 * 用户中心-管理员用户表
 *
 * @author lh0811
 * @email lh0811
 * @date 2022-01-05 18:01:20
 */
@Service
public class SysUserDaoImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserDao {

}
