package com.cloud.base.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cloud.base.common.core.exception.CommonException;
import com.cloud.base.common.core.response.ServerResponse;
import com.cloud.base.common.core.util.IdWorker;
import com.cloud.base.common.core.util.Md5Util;
import com.cloud.base.common.xugou.core.model.entity.SecurityAuthority;
import com.cloud.base.user.constant.UCConstant;
import com.cloud.base.user.param.SysUserCreateParam;
import com.cloud.base.user.param.SysUserQueryParam;
import com.cloud.base.user.param.SysUserUpdateParam;
import com.cloud.base.user.repository.dao.*;
import com.cloud.base.user.repository.entity.*;
import com.cloud.base.user.service.SysUserService;
import com.cloud.base.user.vo.SysDeptVo;
import com.cloud.base.user.vo.SysPositionVo;
import com.cloud.base.user.vo.SysRoleVo;
import com.cloud.base.user.vo.SysUserVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 系统-系统用户表(SysUser)表数据库 服务接口实现
 *
 * @author lh0811
 * @date 2021/1/18
 */
@Slf4j
@Service("sysUserService")
public class SysUserServiceImpl implements SysUserService {

    @Resource
    private SysUserRoleRelDao sysUserRoleRelDao;

    @Resource
    private SysRoleDao sysRoleDao;

    @Resource
    private SysUserPositionRelDao sysUserPositionRelDao;

    @Resource
    private SysUserDeptRelDao sysUserDeptRelDao;

    @Resource
    private SysDeptDao sysDeptDao;

    @Resource
    private SysUserDao sysUserDao;

    @Resource
    private SysPositionDao sysPositionDao;

    @Resource
    private IdWorker idWorker;


    // //////////////// 用户管理

    /**
     * 创建用户
     *
     * @param param
     * @param securityAuthority
     * @throws Exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createUser(SysUserCreateParam param, SecurityAuthority securityAuthority) throws Exception {
        log.info("开始 创建用户");
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

        try {
            // 保存用户信息
            SysUser sysUserNew = new SysUser();
            BeanUtils.copyProperties(param, sysUserNew);
            sysUserNew.setId(idWorker.nextId());
            sysUserNew.setTenantNo(securityAuthority.getSecurityUser().getTenantNo());
            String salt = RandomStringUtils.random(4);
            sysUserNew.setSalt(salt);
            sysUserNew.setPassword(Md5Util.getMD5Str(UCConstant.DefaultPassword, salt));
            sysUserNew.setActiveFlag(true);
            sysUserNew.setDelFlag(false);
            sysUserNew.setCreateBy(Long.valueOf(securityAuthority.getSecurityUser().getId()));
            sysUserNew.setCreateTime(new Date());
            sysUserDao.save(sysUserNew);

            // 保存用户部门信息
            if (param.getDeptId() != null) {
                SysUserDeptRel sysUserDeptRel = new SysUserDeptRel();
                sysUserDeptRel.setId(idWorker.nextId());
                sysUserDeptRel.setTenantNo(securityAuthority.getSecurityUser().getTenantNo());
                sysUserDeptRel.setUserId(sysUserNew.getId());
                sysUserDeptRel.setDeptId(param.getDeptId());
                sysUserDeptRelDao.save(sysUserDeptRel);
            }

            // 保存用户岗位信息
            if (CollectionUtils.isNotEmpty(param.getPositionIdList())) {
                List<SysUserPositionRel> sysUserPositionRelList = param.getPositionIdList().stream().map(ele -> new SysUserPositionRel(idWorker.nextId(), securityAuthority.getSecurityUser().getTenantNo(), sysUserNew.getId(), ele)).collect(Collectors.toList());
                sysUserPositionRelDao.saveBatch(sysUserPositionRelList);
            }

            // 保存用户角色信息
            if (CollectionUtils.isNotEmpty(param.getRoleIdList())) {
                List<SysUserRoleRel> sysUserRoleRelList = param.getRoleIdList().stream().map(ele -> new SysUserRoleRel(idWorker.nextId(), securityAuthority.getSecurityUser().getTenantNo(), sysUserNew.getId(), ele)).collect(Collectors.toList());
                sysUserRoleRelDao.saveBatch(sysUserRoleRelList);
            }

            log.info("完成 创建用户");
        } catch (Exception e) {
            throw CommonException.create(e, ServerResponse.createByError("创建用户失败,请联系管理员"));
        }
    }

    /**
     * 修改用户
     *
     * @param param
     * @param securityAuthority
     * @throws Exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(SysUserUpdateParam param, SecurityAuthority securityAuthority) throws Exception {
        log.info("开始 修改用户");
        try {
            // 更新用户信息
            SysUser updateUser = new SysUser();
            BeanUtils.copyProperties(param, updateUser);
            updateUser.setId(param.getUserId());
            updateUser.setUpdateBy(Long.valueOf(securityAuthority.getSecurityUser().getId()));
            updateUser.setUpdateTime(new Date());
            sysUserDao.updateById(updateUser);

            // 保存用户岗位信息
            if (CollectionUtils.isNotEmpty(param.getPositionIdList())) {

                QueryWrapper<SysUserPositionRel> delSysUserPositionRel = new QueryWrapper<>();
                delSysUserPositionRel.lambda().eq(SysUserPositionRel::getUserId, param.getUserId());
                sysUserPositionRelDao.remove(delSysUserPositionRel);

                List<SysUserPositionRel> sysUserPositionRelList = param.getPositionIdList().stream().map(ele -> new SysUserPositionRel(idWorker.nextId(), securityAuthority.getSecurityUser().getTenantNo(), param.getUserId(), ele)).collect(Collectors.toList());
                sysUserPositionRelDao.saveBatch(sysUserPositionRelList);
            }

            // 保存用户角色信息
            if (CollectionUtils.isNotEmpty(param.getRoleIdList())) {
                QueryWrapper<SysUserRoleRel> delWrapper = new QueryWrapper<>();
                delWrapper.lambda().eq(SysUserRoleRel::getUserId, param.getUserId());
                sysUserRoleRelDao.remove(delWrapper);

                List<SysUserRoleRel> sysUserRoleRelList = param.getRoleIdList().stream().map(ele -> new SysUserRoleRel(idWorker.nextId(), securityAuthority.getSecurityUser().getTenantNo(), param.getUserId(), ele)).collect(Collectors.toList());
                sysUserRoleRelDao.saveBatch(sysUserRoleRelList);
            }
            log.info("完成 修改用户");
        } catch (Exception e) {
            throw CommonException.create(e, ServerResponse.createByError("修改用户失败,请联系管理员"));
        }
    }

    /**
     * 查询用户
     *
     * @param param
     * @param securityAuthority
     * @return
     * @throws Exception
     */
    @Override
    public PageInfo<SysUserVo> queryUser(SysUserQueryParam param, SecurityAuthority securityAuthority) throws Exception {
        log.info("开始 查询用户");
        try {
            QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
            LambdaQueryWrapper<SysUser> lambda = queryWrapper.lambda();
            lambda.orderByDesc(SysUser::getCreateTime);
            lambda.eq(SysUser::getDelFlag, Boolean.FALSE);
            lambda.eq(SysUser::getTenantNo, securityAuthority.getSecurityUser().getTenantNo());
            if (StringUtils.isNotBlank(param.getUsername())) {
                lambda.like(SysUser::getUsername, "%" + param.getUsername() + "%");
            }
            if (StringUtils.isNotBlank(param.getNickName())) {
                lambda.like(SysUser::getNickName, "%" + param.getNickName() + "%");
            }
            if (StringUtils.isNotBlank(param.getPhone())) {
                lambda.like(SysUser::getPhone, "%" + param.getPhone() + "%");
            }
            if (param.getActiveFlag() != null) {
                lambda.eq(SysUser::getActiveFlag, param.getActiveFlag());
            }
            if (param.getLastLoginLow() != null) {
                lambda.ge(SysUser::getLastLogin, param.getLastLoginLow());
            }
            if (param.getLastLoginUp() != null) {
                lambda.le(SysUser::getLastLogin, param.getLastLoginUp());
            }
            if (param.getCreateTimeLow() != null) {
                lambda.ge(SysUser::getCreateTime, param.getCreateTimeLow());
            }
            if (param.getCreateTimeUp() != null) {
                lambda.le(SysUser::getCreateTime, param.getCreateTimeUp());
            }
            PageHelper.startPage(param.getPageNum(), param.getPageSize());
            List<SysUser> sysUsers = sysUserDao.list(queryWrapper);
            PageInfo pageInfo = new PageInfo(sysUsers);
            PageHelper.clearPage();

            // 补充vo信息
            List<SysUser> sysUserList = pageInfo.getList();
            List<SysUserVo> sysUserVoStream = sysUserList.stream().map(ele -> {
                SysUserVo sysUserVo = new SysUserVo();
                BeanUtils.copyProperties(ele, sysUserVo);

                // 部门信息
                QueryWrapper<SysUserDeptRel> sysUserDeptQueryWrapper = new QueryWrapper();
                sysUserDeptQueryWrapper.lambda().eq(SysUserDeptRel::getUserId, ele.getId());
                SysUserDeptRel sysUserDeptRel = sysUserDeptRelDao.getOne(sysUserDeptQueryWrapper);
                if (sysUserDeptRel != null) {
                    SysDept sysDept = sysDeptDao.getById(sysUserDeptRel.getDeptId());
                    SysDeptVo sysDeptVo = new SysDeptVo();
                    BeanUtils.copyProperties(sysDept, sysDeptVo);
                    sysUserVo.setDeptInfo(sysDeptVo);
                }

                // 岗位信息
                QueryWrapper<SysUserPositionRel> userPositionRelQueryWrapper = new QueryWrapper<>();
                userPositionRelQueryWrapper.lambda().eq(SysUserPositionRel::getUserId, ele.getId());
                List<SysUserPositionRel> sysUserPositionRelList = sysUserPositionRelDao.list(userPositionRelQueryWrapper);
                if (CollectionUtils.isNotEmpty(sysUserPositionRelList)) {
                    List<Long> positionIdList = sysUserPositionRelList.stream().map(userPositionRel -> userPositionRel.getPositionId()).collect(Collectors.toList());
                    List<SysPosition> sysPositions = sysPositionDao.listByIds(positionIdList);
                    if (CollectionUtils.isNotEmpty(sysPositions)) {
                        List<SysPositionVo> sysPositionVoList = JSONArray.parseArray(JSON.toJSONString(sysPositions), SysPositionVo.class);
                        sysUserVo.setPositionList(sysPositionVoList);
                    }
                }

                // 角色信息
                QueryWrapper<SysUserRoleRel> sysUserRoleRelQueryWrapper = new QueryWrapper<>();
                sysUserRoleRelQueryWrapper.lambda().eq(SysUserRoleRel::getUserId, ele.getId());
                List<SysUserRoleRel> sysUserRoleRelList = sysUserRoleRelDao.list(sysUserRoleRelQueryWrapper);
                if (CollectionUtils.isNotEmpty(sysUserRoleRelList)) {
                    List<Long> sysRoleIdList = sysUserRoleRelList.stream().map(sysUserRoleRel -> sysUserRoleRel.getRoleId()).collect(Collectors.toList());
                    List<SysRole> sysRoles = sysRoleDao.listByIds(sysRoleIdList);
                    if (CollectionUtils.isNotEmpty(sysRoles)) {
                        List<SysRoleVo> sysRoleVoList = JSONArray.parseArray(JSON.toJSONString(sysRoles), SysRoleVo.class);
                        sysUserVo.setRoleList(sysRoleVoList);
                    }
                }
                return sysUserVo;
            }).collect(Collectors.toList());
            pageInfo.setList(sysUserVoStream);
            log.info("完成 查询用户");
            return pageInfo;
        } catch (Exception e) {
            throw CommonException.create(e, ServerResponse.createByError("查询用户失败,请联系管理员"));
        }
    }

    /**
     * 删除用户
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delUser(Long userId, SecurityAuthority securityAuthority) throws Exception {
        log.info("开始 删除用户");
        SysUser checkUser = sysUserDao.getById(userId);
        if (checkUser == null) {
            throw CommonException.create(ServerResponse.createByError("用户信息不存在"));
        }
        if (!checkUser.getTenantNo().equals(securityAuthority.getSecurityUser().getTenantNo())) {
            throw CommonException.create(ServerResponse.createByError("非法操作"));
        }
        try {
            // 删除用户
            checkUser.setDelFlag(true);
            checkUser.setUpdateBy(Long.valueOf(securityAuthority.getSecurityUser().getId()));
            checkUser.setUpdateTime(new Date());
            sysUserDao.updateById(checkUser);
            log.info("完成 删除用户");
        } catch (Exception e) {
            throw CommonException.create(e, ServerResponse.createByError("删除用户失败,请联系管理员"));
        }
    }


    /**
     * 重置用户密码
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resetPassword(Long userId, SecurityAuthority securityAuthority) throws Exception {
        log.info("开始 重置用户密码");
        SysUser checkUser = sysUserDao.getById(userId);
        if (checkUser == null) {
            throw CommonException.create(ServerResponse.createByError("用户信息不存在"));
        }
        if (!checkUser.getTenantNo().equals(securityAuthority.getSecurityUser().getTenantNo())) {
            throw CommonException.create(ServerResponse.createByError("非法操作"));
        }
        try {
            // 重置用户名密码
            SysUser updateUser = new SysUser();
            updateUser.setId(checkUser.getId());
            updateUser.setPassword(Md5Util.getMD5Str(UCConstant.DefaultPassword, checkUser.getSalt()));
            updateUser.setUpdateBy(Long.valueOf(securityAuthority.getSecurityUser().getId()));
            updateUser.setUpdateTime(new Date());
            sysUserDao.updateById(updateUser);
            log.info("完成 重置用户密码");
        } catch (Exception e) {
            throw CommonException.create(e, ServerResponse.createByError("重置用户密码失败,请联系管理员"));
        }
    }
}
