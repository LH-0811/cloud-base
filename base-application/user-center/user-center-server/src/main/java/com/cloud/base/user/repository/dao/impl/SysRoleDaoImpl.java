package com.cloud.base.user.repository.dao.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.base.user.repository.dao.SysRoleDao;
import com.cloud.base.user.repository.dao.mapper.SysRoleMapper;
import com.cloud.base.user.repository.entity.SysRole;
import org.springframework.stereotype.Service;

/**
 * 用户中心-角色表
 *
 * @author lh0811
 * @email lh0811
 * @date 2022-01-05 18:01:20
 */
@Service
public class SysRoleDaoImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleDao {

}
