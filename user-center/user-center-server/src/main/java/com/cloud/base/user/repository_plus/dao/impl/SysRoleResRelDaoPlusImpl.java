package com.cloud.base.user.repository_plus.dao.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.base.user.repository_plus.entity.SysRoleResRel;
import com.cloud.base.user.repository_plus.dao.SysRoleResRelDao;
import com.cloud.base.user.repository_plus.dao.SysRoleResRelDaoPlus;
import org.springframework.stereotype.Service;

/**
 * 系统-角色-权限关系表(SysRoleResRel)表数据库访问层加强
 *
 * @author lh0811
 * @since 2021-11-05 09:29:02
 */
@Service
public class SysRoleResRelDaoPlusImpl extends ServiceImpl<SysRoleResRelDao.SysRoleResRelMapper,SysRoleResRel> implements SysRoleResRelDaoPlus {

}