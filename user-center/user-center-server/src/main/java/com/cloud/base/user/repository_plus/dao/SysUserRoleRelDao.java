package com.cloud.base.user.repository_plus.dao;

import com.cloud.base.user.repository_plus.entity.SysUserRoleRel;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
/**
 * 系统-用户-角色关系表(SysUserRoleRel)表数据库访问层加强
 *
 * @author lh0811
 * @since 2021-11-05 09:29:02
 */
public interface SysUserRoleRelDao extends IService<SysUserRoleRel> {

}
