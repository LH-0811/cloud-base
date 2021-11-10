package com.cloud.base.user.repository.dao.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.base.user.repository.dao.SysRoleDao;
import com.cloud.base.user.repository.dao.mapper.SysRoleMapper;
import com.cloud.base.user.repository.entity.SysRole;
import org.springframework.stereotype.Service;

/**
 * 系统-角色表
 *
 * @author lh0811
 * @email lh0811
 * @date 2021-11-10 11:01:37
 */
@Service
public class SysRoleDaoImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleDao {

}
