package com.cloud.base.user.repository_plus.dao;

import com.cloud.base.user.repository_plus.entity.SysRoleResRel;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Repository;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
/**
 * 系统-角色-权限关系表(SysRoleResRel)表数据库访问层加强
 *
 * @author lh0811
 * @since 2021-11-05 09:29:02
 */
public interface SysRoleResRelDao extends IService<SysRoleResRel> {
    @Repository
    public interface SysRoleResRelMapper extends BaseMapper<SysRoleResRel> {

    }
}