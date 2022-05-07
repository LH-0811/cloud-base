package com.cloud.base.user.service;

import com.cloud.base.common.xugou.core.model.entity.SecurityAuthority;
import com.cloud.base.user.param.SysResCreateParam;
import com.cloud.base.user.repository.entity.SysUser;
import com.cloud.base.user.vo.SysResSimpleVo;
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
    void createRes(SysResCreateParam param, SecurityAuthority securityAuthority) throws Exception;

    /**
     * 删除权限信息
     */
    void deleteRes(Long resId, SecurityAuthority securityAuthority) throws Exception;

    /**
     * 获取全部资源树
     */
    List<SysResVo> getAllResTree(SecurityAuthority securityAuthority) throws Exception;


    /**
     * 获取全部资源树(简单属性)
     *
     * @throws Exception 异常
     */
    List<SysResSimpleVo> getAllResTreeSimple() throws Exception;
}
