package com.cloud.base.user.service;

import com.cloud.base.user.param.SysResCreateParam;
import com.cloud.base.user.repository.entity.SysRes;
import com.cloud.base.user.repository.entity.SysUser;
import com.cloud.base.user.vo.SysResVo;

import java.util.List;

/**
 * @author lh0811
 * @date 2021/8/17
 */
public interface SysResService {

    // //////////////// 资源管理

    /**
     * 创建权限
     */
    void createRes(SysResCreateParam param, SysUser sysUser) throws Exception;

    /**
     * 删除权限信息
     */
    void deleteRes(Long resId, SysUser sysUser) throws Exception;

    /**
     * 获取全部资源树
     */
    List<SysResVo> getAllResTree() throws Exception;


}
