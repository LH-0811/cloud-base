package com.cloud.base.user.repository_plus.dao;

import com.cloud.base.user.repository_plus.entity.SysPosition;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Repository;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
/**
 * 用户中心-岗位信息(SysPosition)表数据库访问层加强
 *
 * @author lh0811
 * @since 2021-11-05 09:29:02
 */
public interface SysPositionDao extends IService<SysPosition> {
    @Repository
    public interface SysPositionMapper extends BaseMapper<SysPosition> {

    }
}