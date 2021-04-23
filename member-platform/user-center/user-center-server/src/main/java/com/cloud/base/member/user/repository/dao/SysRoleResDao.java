package com.cloud.base.member.user.repository.dao;

import com.cloud.base.member.user.repository.entity.SysRoleRes;
import tk.mybatis.mapper.additional.insert.InsertListMapper;
import tk.mybatis.mapper.common.Mapper;

/**
 * 系统-角色-权限关系表(SysRoleRes)表数据库访问层
 *
 * @author lh0811
 * @since 2021-01-18 10:29:35
 */
public interface SysRoleResDao extends Mapper<SysRoleRes>, InsertListMapper<SysRoleRes> {


}
