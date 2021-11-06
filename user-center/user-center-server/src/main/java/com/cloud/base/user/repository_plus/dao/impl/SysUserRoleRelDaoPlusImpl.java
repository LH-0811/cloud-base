package com.cloud.base.user.repository_plus.dao.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.base.user.repository_plus.dao.mapper.SysUserRoleRelMapper;
import com.cloud.base.user.repository_plus.entity.SysUserRoleRel;
import com.cloud.base.user.repository_plus.dao.SysUserRoleRelDao;
import org.springframework.stereotype.Service;

/**
 * 系统-用户-角色关系表(SysUserRoleRel)表数据库访问层加强
 *
 * @author lh0811
 * @since 2021-11-05 09:29:02
 */
@Service
public class SysUserRoleRelDaoPlusImpl extends ServiceImpl<SysUserRoleRelMapper,SysUserRoleRel> implements SysUserRoleRelDao {

}
