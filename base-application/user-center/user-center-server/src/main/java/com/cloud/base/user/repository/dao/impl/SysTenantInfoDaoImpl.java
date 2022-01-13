package com.cloud.base.user.repository.dao.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.base.common.core.exception.CommonException;
import com.cloud.base.common.core.response.ServerResponse;
import com.cloud.base.common.core.util.IdWorker;
import com.cloud.base.user.param.SysTenantMgrUserCreateParam;
import com.cloud.base.user.repository.dao.SysTenantInfoDao;
import com.cloud.base.user.repository.dao.mapper.SysTenantInfoMapper;
import com.cloud.base.user.repository.entity.SysTenantInfo;
import com.cloud.base.user.repository.entity.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 系统管理-租户信息管理
 *
 * @author lh0811
 * @email lh0811
 * @date 2022-01-05 18:01:20
 */
@Service
public class SysTenantInfoDaoImpl extends ServiceImpl<SysTenantInfoMapper, SysTenantInfo> implements SysTenantInfoDao {

    @Autowired
    private SysTenantInfoMapper sysTenantInfoMapper;

    @Override
    /**
     * 获取当前租户的系统管理员
     */
    public SysUser getTenantSysMgrUser(String tenantNo) throws Exception {
        return sysTenantInfoMapper.getTenantSysMgrUser(tenantNo);
    }

}
