package com.cloud.base.user.service;

import com.cloud.base.user.param.SysPositionCreateParam;
import com.cloud.base.user.param.SysPositionQueryParam;
import com.cloud.base.user.repository.entity.SysPosition;
import com.cloud.base.user.repository.entity.SysUser;
import com.github.pagehelper.PageInfo;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author lh0811
 * @date 2021/8/17
 */
public interface SysPositionService {
    /**
     * 创建岗位信息
     */
    void createPosition(SysPositionCreateParam param, SysUser sysUser) throws Exception;

    /**
     * 删除岗位信息
     */
    void deletePosition(Long positionId, SysUser sysUser) throws Exception;

    /**
     * 查询岗位信息
     */
    PageInfo<SysPosition> queryPosition(SysPositionQueryParam param, SysUser sysUser) throws Exception;
}