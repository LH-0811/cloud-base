package com.cloud.base.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cloud.base.common.core.exception.CommonException;
import com.cloud.base.common.core.response.ServerResponse;
import com.cloud.base.common.core.util.IdWorker;
import com.cloud.base.common.core.util.Md5Util;
import com.cloud.base.common.xugou.core.model.entity.SecurityAuthority;
import com.cloud.base.user.constant.UCConstant;
import com.cloud.base.user.param.*;
import com.cloud.base.user.repository.dao.SysTenantInfoDao;
import com.cloud.base.user.repository.dao.SysUserDao;
import com.cloud.base.user.repository.dao.SysUserRoleRelDao;
import com.cloud.base.user.repository.entity.*;
import com.cloud.base.user.service.SysSerialService;
import com.cloud.base.user.service.SysTenantInfoService;
import com.cloud.base.user.vo.SysTenantInfoVo;
import com.cloud.base.user.vo.SysUserVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    private SysUserDao sysUserDao;

    @Autowired
    private SysUserRoleRelDao sysUserRoleRelDao;


    /**
     * 创建租户信息
     */
    @Override
    public SysTenantInfoVo tenantInfoCreate(SysTenantInfoCreateParam param, SecurityAuthority securityAuthority) throws Exception {
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
        sysTenantInfo.setCreateBy(Long.valueOf(securityAuthority.getSecurityUser().getId()));
        sysTenantInfo.setCreateTime(new Date());
        sysTenantInfo.setDelFlag(Boolean.FALSE);
        // 保存数据
        sysTenantInfoDao.save(sysTenantInfo);

        SysTenantInfoVo sysTenantInfoVo = new SysTenantInfoVo();
        BeanUtils.copyProperties(sysTenantInfo,sysTenantInfoVo);
        return sysTenantInfoVo;
    }

    /**
     * 删除租户信息(软删)
     */
    @Override
    public void tenantInfoDelete(Long tenantInfoId, SecurityAuthority securityAuthority) throws Exception {
        log.info("[租户信息管理] 删除租户信息 参数:{}", tenantInfoId);
        SysTenantInfo sysTenantInfo = sysTenantInfoDao.getById(tenantInfoId);
        if (sysTenantInfo == null) {
            throw CommonException.create(ServerResponse.createByError("租户信息不存在"));
        }
        SysTenantInfo updateInfo = new SysTenantInfo();
        updateInfo.setId(tenantInfoId);
        updateInfo.setDelFlag(Boolean.TRUE);
        sysTenantInfo.setUpdateBy(Long.valueOf(securityAuthority.getSecurityUser().getId()));
        sysTenantInfo.setUpdateTime(new Date());
        sysTenantInfoDao.updateById(updateInfo);
    }

    /**
     * 更新租户信息
     */
    @Override
    public SysTenantInfoVo tenantInfoUpdate(SysTenantInfoUpdateParam param, SecurityAuthority securityAuthority) throws Exception {
        log.info("[租户信息管理] 更新租户信息 参数:{}", JSON.toJSONString(param));
        SysTenantInfo sysTenantInfo = sysTenantInfoDao.getById(param.getId());
        if (sysTenantInfo == null) {
            throw CommonException.create(ServerResponse.createByError("租户信息不存在"));
        }
        SysTenantInfo updateInfo = new SysTenantInfo();
        BeanUtils.copyProperties(param, updateInfo);
        sysTenantInfo.setUpdateBy(Long.valueOf(securityAuthority.getSecurityUser().getId()));
        sysTenantInfo.setUpdateTime(new Date());
        sysTenantInfoDao.updateById(updateInfo);
        SysTenantInfo newInfo = sysTenantInfoDao.getById(param.getId());
        SysTenantInfoVo sysTenantInfoVo = new SysTenantInfoVo();
        BeanUtils.copyProperties(newInfo,sysTenantInfoVo);
        return sysTenantInfoVo;
    }

    /**
     * 查询租户信息
     */
    @Override
    public PageInfo<SysTenantInfoVo> tenantInfoQuery(SysTenantInfoQueryParam param, SecurityAuthority securityAuthority) throws Exception {
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
            lambda.ge(SysTenantInfo::getCreateTime, param.getCreateTimeLow() + " 00:00:01");
        }
        if (param.getCreateTimeUp() != null) {
            lambda.le(SysTenantInfo::getCreateTime, param.getCreateTimeUp() + " 23:59:59");
        }
        PageHelper.startPage(param.getPageNum(), param.getPageSize());
        List<SysTenantInfo> list = sysTenantInfoDao.list(queryWrapper);
        PageInfo pageInfo = new PageInfo<>(list);
        PageHelper.clearPage();
        if (CollectionUtils.isNotEmpty(list)) {
            List<SysTenantInfoVo> voList = list.stream().map(ele -> {
                SysTenantInfoVo sysTenantInfoVo = new SysTenantInfoVo();
                BeanUtils.copyProperties(ele, sysTenantInfoVo);
                return sysTenantInfoVo;
            }).collect(Collectors.toList());
            pageInfo.setList(voList);
        }
        return pageInfo;
    }


    /**
     * 创建该租户的系统管理员
     */
    @Override
    public SysUserVo getTenantMgrUser(Long tenantId, SecurityAuthority securityAuthority) throws Exception {
        log.info("[租户信息管理] 查询租户信息 参数:tenantId={}", tenantId);
        SysTenantInfo sysTenantInfo = sysTenantInfoDao.getById(tenantId);
        if (sysTenantInfo == null) {
            throw CommonException.create(ServerResponse.createByError("租户信息不存在"));
        }
        SysUser tenantSysMgrUser = sysTenantInfoDao.getTenantSysMgrUser(sysTenantInfo.getTenantNo());
        if (tenantSysMgrUser != null) {
            SysUserVo sysUserVo = new SysUserVo();
            BeanUtils.copyProperties(tenantSysMgrUser,sysUserVo);
            return sysUserVo;
        }
        throw CommonException.create(ServerResponse.createByError("当前租户不存在管理员,请先创建"));
    }


    /**
     * 创建租户管理员
     */
    @Override
    public SysUserVo genTenantMgrUser(SysTenantMgrUserCreateParam param, SecurityAuthority securityAuthority) throws Exception {
        log.info("[租户信息管理] 创建租户系统管理员 参数:{}", JSON.toJSONString(param));
        SysTenantInfo sysTenantInfo = sysTenantInfoDao.getById(param.getTenantId());
        if (sysTenantInfo == null) {
            throw CommonException.create(ServerResponse.createByError("租户信息不存在"));
        }
        SysUser tenantSysMgrUser = sysTenantInfoDao.getTenantSysMgrUser(sysTenantInfo.getTenantNo());
        if (tenantSysMgrUser != null) {
            throw CommonException.create(ServerResponse.createByError("当前租户已存在管理员"));
        }
        QueryWrapper<SysUser> sysUserNameQueryWrapper = new QueryWrapper<>();
        sysUserNameQueryWrapper.lambda().eq(SysUser::getDelFlag, false).eq(SysUser::getUsername, param.getUsername());
        if (sysUserDao.count(sysUserNameQueryWrapper) > 0) {
            throw CommonException.create(ServerResponse.createByError("用户名已经存在"));
        }
        QueryWrapper<SysUser> sysUserPhoneQueryWrapper = new QueryWrapper<>();
        sysUserPhoneQueryWrapper.lambda().eq(SysUser::getDelFlag, false).eq(SysUser::getPhone, param.getPhone());
        if (sysUserDao.count(sysUserPhoneQueryWrapper) > 0) {
            throw CommonException.create(ServerResponse.createByError("手机号已经存在"));
        }
        // 保存用户信息
        SysUser sysUserNew = new SysUser();
        BeanUtils.copyProperties(param, sysUserNew);
        sysUserNew.setId(idWorker.nextId());
        String salt = RandomStringUtils.random(4);
        sysUserNew.setSalt(salt);
        sysUserNew.setTenantNo(securityAuthority.getSecurityUser().getTenantNo());
        sysUserNew.setCreateBy(Long.valueOf(securityAuthority.getSecurityUser().getId()));
        sysUserNew.setCreateTime(new Date());
        sysUserNew.setPassword(Md5Util.getMD5Str(UCConstant.DefaultPassword, salt));
        sysUserNew.setActiveFlag(true);
        sysUserNew.setDelFlag(false);
        sysUserNew.setCreateBy(Long.valueOf(securityAuthority.getSecurityUser().getId()));
        sysUserNew.setCreateTime(new Date());
        sysUserDao.save(sysUserNew);
        // 保存用户角色信息
        SysUserRoleRel sysUserRoleRel = new SysUserRoleRel(idWorker.nextId(), sysTenantInfo.getTenantNo(), sysUserNew.getId(), UCConstant.RoleType.SystemMgr.getRoleId());
        sysUserRoleRelDao.save(sysUserRoleRel);
        SysUserVo sysUserVo = new SysUserVo();
        BeanUtils.copyProperties(sysUserNew,sysUserVo);
        return sysUserVo;
    }

    /**
     * 更新租户管理员信息
     */
    @Override
    public void updateTenantMgrUserInfo(SysTenantMgrUserUpdateParam param, SecurityAuthority securityAuthority) throws Exception {
        log.info("[租户信息管理] 更新租户管理员用户信息 参数:{}", JSON.toJSONString(param));
        SysUser sysUser = sysUserDao.getById(param.getUserId());
        if (sysUser == null) {
            throw CommonException.create(ServerResponse.createByError("用户不存在"));
        }
        QueryWrapper<SysUserRoleRel> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SysUserRoleRel::getUserId, param.getUserId());
        List<SysUserRoleRel> sysUserRoleRelList = sysUserRoleRelDao.list(queryWrapper);
        if (CollectionUtils.isEmpty(sysUserRoleRelList)) {
            throw CommonException.create(ServerResponse.createByError("未获取到用户角色信息"));
        }

        SysUserRoleRel sysUserRoleRel = sysUserRoleRelList.stream().filter(ele -> ele.getRoleId().equals(UCConstant.RoleType.SystemMgr.getRoleId())).findFirst().orElse(null);
        if (sysUserRoleRel == null) {
            throw CommonException.create(ServerResponse.createByError("该用户没有系统管理员角色"));
        }
        SysUser updateInfo = new SysUser();
        BeanUtils.copyProperties(param, updateInfo);
        updateInfo.setUpdateBy(sysUser.getId());
        updateInfo.setUpdateTime(new Date());
        sysUserDao.updateById(updateInfo);
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
