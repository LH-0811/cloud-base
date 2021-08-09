package com.cloud.base.user.repository.dao;

import com.cloud.base.user.repository.entity.SysUserRole;
import tk.mybatis.mapper.additional.insert.InsertListMapper;
import tk.mybatis.mapper.common.Mapper;

/**
 * 系统-用户-角色关系表(SysUserRole)表数据库访问层
 *
 * @author lh0811
 * @since 2021-01-18 10:29:35
 */
public interface SysUserRoleDao extends Mapper<SysUserRole>, InsertListMapper<SysUserRole> {


}
