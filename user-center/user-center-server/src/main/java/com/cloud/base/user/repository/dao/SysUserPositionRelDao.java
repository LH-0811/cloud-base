package com.cloud.base.user.repository.dao;

import com.cloud.base.user.repository.entity.SysUserPositionRel;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.additional.insert.InsertListMapper;
import tk.mybatis.mapper.common.Mapper;

/**
 * 用户中心-用户岗位信息关系表(SysUserPositionRelation)表数据库访问层
 *
 * @author lh0811
 * @since 2021-08-10 21:55:23
 */
public interface SysUserPositionRelDao extends Mapper<SysUserPositionRel>, IdListMapper<SysUserPositionRel,Long>, InsertListMapper<SysUserPositionRel> {


}
