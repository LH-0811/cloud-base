package com.cloud.base.user.repository_plus.dao;

import com.cloud.base.user.repository_plus.entity.SysDict;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Repository;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
/**
 * 系统表-字典表(SysDict)表数据库访问层加强
 *
 * @author lh0811
 * @since 2021-11-05 09:29:02
 */
public interface SysDictDao extends IService<SysDict> {
    @Repository
    public interface SysDictMapper extends BaseMapper<SysDict> {

    }
}