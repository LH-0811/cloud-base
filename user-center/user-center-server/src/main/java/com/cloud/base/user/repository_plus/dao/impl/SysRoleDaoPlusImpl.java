package com.cloud.base.user.repository_plus.dao.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.base.user.repository_plus.dao.mapper.SysRoleMapper;
import com.cloud.base.user.repository_plus.entity.SysRole;
import com.cloud.base.user.repository_plus.dao.SysRoleDao;
import org.springframework.stereotype.Service;

/**
 * 系统-角色表(SysRole)表数据库访问层加强
 *
 * @author lh0811
 * @since 2021-11-05 09:29:02
 */
@Service
public class SysRoleDaoPlusImpl extends ServiceImpl<SysRoleMapper,SysRole> implements SysRoleDao {

}
