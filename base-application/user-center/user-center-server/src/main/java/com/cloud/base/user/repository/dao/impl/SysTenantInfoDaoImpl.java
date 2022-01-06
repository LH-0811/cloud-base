package com.cloud.base.user.repository.dao.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.base.user.repository.dao.SysTenantInfoDao;
import com.cloud.base.user.repository.dao.mapper.SysTenantInfoMapper;
import com.cloud.base.user.repository.entity.SysTenantInfo;
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

}
