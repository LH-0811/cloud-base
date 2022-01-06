package com.cloud.base.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cloud.base.common.core.exception.CommonException;
import com.cloud.base.common.core.response.ServerResponse;
import com.cloud.base.common.core.util.IdWorker;
import com.cloud.base.user.constant.UCConstant;
import com.cloud.base.user.param.SysTenantInfoCreateParam;
import com.cloud.base.user.param.SysTenantInfoQueryParam;
import com.cloud.base.user.param.SysTenantInfoUpdateParam;
import com.cloud.base.user.repository.dao.SysTenantInfoDao;
import com.cloud.base.user.repository.entity.SysTenantInfo;
import com.cloud.base.user.repository.entity.SysUser;
import com.cloud.base.user.service.SysSerialService;
import com.cloud.base.user.service.SysTenantInfoService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 租户管理服务
 *
 * @author lh0811
 * @date 2022/1/6
 */
@Slf4j
@Service
public class SysTenantInfoServiceImpl implements SysTenantInfoService {

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private SysTenantInfoDao sysTenantInfoDao;

    @Autowired
    private SysSerialService sysSerialService;


    /**
     * 创建租户信息
     */
    @Override
    public SysTenantInfo tenantInfoCreate(SysTenantInfoCreateParam param, SysUser sysUser) throws Exception {
        log.info("[租户信息管理] 创建租户信息 参数:{}", JSON.toJSONString(param));
        // 判断租户名称是否已经存在
        checkTenantName(param.getTenantName());
        // 创建租户信息
        SysTenantInfo sysTenantInfo = new SysTenantInfo();
        BeanUtils.copyProperties(param, sysTenantInfo);
        // 设置id
        sysTenantInfo.setId(idWorker.nextId());
        // 设置业务序号
        sysTenantInfo.setTenantNo(sysSerialService.getNextNumByBizType(UCConstant.SerialBizType.TenantNo));
        // 创建信息
        sysTenantInfo.setCreateBy(sysUser.getId());
        sysTenantInfo.setCreateTime(new Date());
        sysTenantInfo.setDelFlag(Boolean.FALSE);
        // 保存数据
        sysTenantInfoDao.save(sysTenantInfo);
        return sysTenantInfo;
    }

    /**
     * 删除租户信息(软删)
     */
    @Override
    public void tenantInfoDelete(Long tenantInfoId, SysUser sysUser) throws Exception {
        log.info("[租户信息管理] 删除租户信息 参数:{}", tenantInfoId);
        SysTenantInfo sysTenantInfo = sysTenantInfoDao.getById(tenantInfoId);
        if (sysTenantInfo == null) {
            throw CommonException.create(ServerResponse.createByError("租户信息不存在"));
        }
        SysTenantInfo updateInfo = new SysTenantInfo();
        updateInfo.setId(tenantInfoId);
        updateInfo.setDelFlag(Boolean.TRUE);
        sysTenantInfo.setUpdateBy(sysUser.getId());
        sysTenantInfo.setUpdateTime(new Date());
        sysTenantInfoDao.updateById(updateInfo);
    }

    /**
     * 更新租户信息
     */
    @Override
    public SysTenantInfo tenantInfoUpdate(SysTenantInfoUpdateParam param, SysUser sysUser) throws Exception {
        log.info("[租户信息管理] 更新租户信息 参数:{}", JSON.toJSONString(param));
        SysTenantInfo sysTenantInfo = sysTenantInfoDao.getById(param.getId());
        if (sysTenantInfo == null) {
            throw CommonException.create(ServerResponse.createByError("租户信息不存在"));
        }
        SysTenantInfo updateInfo = new SysTenantInfo();
        BeanUtils.copyProperties(param, updateInfo);
        sysTenantInfo.setUpdateBy(sysUser.getId());
        sysTenantInfo.setUpdateTime(new Date());
        sysTenantInfoDao.updateById(updateInfo);
        return sysTenantInfoDao.getById(param.getId());
    }

    /**
     * 查询租户信息
     */
    @Override
    public PageInfo<SysTenantInfo> tenantInfoQuery(SysTenantInfoQueryParam param, SysUser sysUser) throws Exception {
        log.info("[租户信息管理] 查询租户信息 参数:{}", JSON.toJSONString(param));
        QueryWrapper<SysTenantInfo> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<SysTenantInfo> lambda = queryWrapper.lambda();

        // 创建时间倒序
        lambda.orderByDesc(SysTenantInfo::getCreateTime);
        // 删除标志
        lambda.eq(SysTenantInfo::getDelFlag, Boolean.FALSE);

        if (StringUtils.isNotBlank(param.getTenantNo())) {
            lambda.like(SysTenantInfo::getTenantNo, "%" + param.getTenantNo() + "%");
        }

        if (StringUtils.isNotBlank(param.getTenantName())) {
            lambda.like(SysTenantInfo::getTenantName, "%" + param.getTenantName() + "%");
        }

        if (StringUtils.isNotBlank(param.getContactsName())) {
            lambda.like(SysTenantInfo::getContactsName, "%" + param.getContactsName() + "%");
        }

        if (StringUtils.isNotBlank(param.getContactsPhone())) {
            lambda.like(SysTenantInfo::getContactsPhone, "%" + param.getContactsPhone() + "%");
        }

        if (StringUtils.isNotBlank(param.getContactsEmail())) {
            lambda.like(SysTenantInfo::getContactsEmail, "%" + param.getContactsEmail() + "%");
        }

        if (param.getActiveFlag() != null) {
            lambda.eq(SysTenantInfo::getActiveFlag, param.getActiveFlag());
        }

        if (param.getCreateTimeLow() != null) {
            lambda.ge(SysTenantInfo::getCreateTime, param.getCreateTimeLow());
        }
        if (param.getCreateTimeUp() != null) {
            lambda.le(SysTenantInfo::getCreateTime, param.getCreateTimeUp());
        }
        PageHelper.startPage(param.getPageNum(), param.getPageSize());
        List<SysTenantInfo> list = sysTenantInfoDao.list(queryWrapper);
        PageInfo<SysTenantInfo> pageInfo = new PageInfo<>(list);
        PageHelper.clearPage();
        return pageInfo;
    }


    // =========================== 私有方法 ===========================

    /**
     * 检查租户名称是重复
     *
     * @param tenantName
     * @throws CommonException
     */
    private void checkTenantName(String tenantName) throws CommonException {
        if (StringUtils.isBlank(tenantName)) {
            throw CommonException.create(ServerResponse.createByError("未上传租户名称"));
        }
        QueryWrapper<SysTenantInfo> queryWrapperByName = new QueryWrapper<>();
        queryWrapperByName.lambda()
                .eq(SysTenantInfo::getTenantName, tenantName)
                .eq(SysTenantInfo::getDelFlag, Boolean.FALSE);
        if (sysTenantInfoDao.count(queryWrapperByName) > 0) {
            throw CommonException.create(ServerResponse.createByError("租户名称已经存在"));
        }
    }


}
