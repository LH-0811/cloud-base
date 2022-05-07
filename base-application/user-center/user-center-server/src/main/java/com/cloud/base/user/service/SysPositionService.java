package com.cloud.base.user.service;

import com.cloud.base.common.xugou.core.model.entity.SecurityAuthority;
import com.cloud.base.user.param.SysPositionCreateParam;
import com.cloud.base.user.param.SysPositionQueryParam;
import com.cloud.base.user.repository.entity.SysUser;
import com.cloud.base.user.vo.SysPositionVo;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author lh0811
 * @date 2021/8/17
 */
public interface SysPositionService {
    /**
     * 创建岗位信息
     */
    void createPosition(SysPositionCreateParam param, SecurityAuthority securityAuthority) throws Exception;

    /**
     * 删除岗位信息
     */
    void deletePosition(Long positionId, SecurityAuthority securityAuthority) throws Exception;

    /**
     * 查询岗位信息
     */
    PageInfo<SysPositionVo> queryPosition(SysPositionQueryParam param, SecurityAuthority securityAuthority) throws Exception;

    /**
     * 获取全部岗位列表
     */
    List<SysPositionVo> queryAllPosition(SecurityAuthority securityAuthority) throws Exception;
}
