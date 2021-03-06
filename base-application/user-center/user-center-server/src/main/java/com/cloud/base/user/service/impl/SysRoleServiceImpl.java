package com.cloud.base.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cloud.base.common.core.exception.CommonException;
import com.cloud.base.common.core.response.ServerResponse;
import com.cloud.base.common.core.util.IdWorker;
import com.cloud.base.common.xugou.core.model.entity.SecurityAuthority;
import com.cloud.base.user.param.SysRoleCreateParam;
import com.cloud.base.user.param.SysRoleQueryParam;
import com.cloud.base.user.param.SysRoleUpdateParam;
import com.cloud.base.user.repository.dao.SysResDao;
import com.cloud.base.user.repository.dao.SysRoleDao;
import com.cloud.base.user.repository.dao.SysRoleResRelDao;
import com.cloud.base.user.repository.dao.SysUserRoleRelDao;
import com.cloud.base.user.repository.entity.*;
import com.cloud.base.user.service.SysRoleService;
import com.cloud.base.user.vo.SysResVo;
import com.cloud.base.user.vo.SysRoleVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色管理服务实现
 *
 * @author lh0811
 * @date 2021/8/17
 */
@Slf4j
@Service("sysRoleService")
public class SysRoleServiceImpl implements SysRoleService {

    @Resource
    private SysRoleDao sysRoleDao;

    @Resource
    private SysRoleResRelDao sysRoleResRelDao;

    @Resource
    private SysUserRoleRelDao sysUserRoleRelDao;

    @Resource
    private SysResDao sysResDao;

    @Resource
    private IdWorker idWorker;

// //////////////// 角色管理

    /**
     * 创建系统角色信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createRole(SysRoleCreateParam param, SecurityAuthority securityAuthority) throws Exception {
        log.info("进入 SysAdminServiceImpl.createRole:" + JSON.toJSONString(param));
        QueryWrapper<SysRole> roleNameQueryWrapper = new QueryWrapper<>();
        roleNameQueryWrapper.lambda().eq(SysRole::getName, param.getName());
        if (sysRoleDao.count(roleNameQueryWrapper) > 0) {
            throw CommonException.create(ServerResponse.createByError("角色名称已经存在"));
        }

        try {
            // 创建角色信息
            SysRole sysRole = new SysRole();
            BeanUtils.copyProperties(param, sysRole);
            sysRole.setId(idWorker.nextId());
            sysRole.setTenantNo(securityAuthority.getSecurityUser().getTenantNo());
            sysRole.setCreateTime(new Date());
            sysRole.setCreateBy(Long.valueOf(securityAuthority.getSecurityUser().getId()));
            sysRoleDao.save(sysRole);

            // 添加角色与资源之间的关系
            if (CollectionUtils.isNotEmpty(param.getResIdList())) {
                List<SysRoleResRel> sysRoleResRelList = param.getResIdList().stream().map(resId -> new SysRoleResRel(idWorker.nextId(), securityAuthority.getSecurityUser().getTenantNo(), sysRole.getId(), resId)).collect(Collectors.toList());
                sysRoleResRelDao.saveBatch(sysRoleResRelList);
            }

            log.info("完成创建");
        } catch (Exception e) {
            log.info("完成 SysAdminServiceImpl.createRole:" + JSON.toJSONString(param));
            throw CommonException.create(e, ServerResponse.createByError("创建用户失败"));
        }
    }

    /**
     * 修改系统角色信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRole(SysRoleUpdateParam param, SecurityAuthority securityAuthority) throws Exception {
        log.info("进入 SysAdminServiceImpl.updateRole:" + JSON.toJSONString(param));
        SysRole sysRole = sysRoleDao.getById(param.getId());
        if (sysRole == null) {
            log.info("角色信息不存在");
            throw CommonException.create(ServerResponse.createByError("角色信息不存在"));
        }
        if (!sysRole.getTenantNo().equals(securityAuthority.getSecurityUser().getTenantNo())) {
            throw CommonException.create(ServerResponse.createByError("非法操作"));
        }

        try {
            BeanUtils.copyProperties(param, sysRole);
            sysRole.setUpdateBy(Long.valueOf(securityAuthority.getSecurityUser().getId()));
            sysRole.setUpdateTime(new Date());
            sysRoleDao.updateById(sysRole);
            // 添加角色与资源之间的关系
            if (CollectionUtils.isNotEmpty(param.getResIdList())) {

                QueryWrapper<SysRoleResRel> roleResDelQueryWrapper = new QueryWrapper<>();
                roleResDelQueryWrapper.lambda().eq(SysRoleResRel::getRoleId, param.getId());
                sysRoleResRelDao.remove(roleResDelQueryWrapper);

                List<SysRoleResRel> sysRoleResRelList = param.getResIdList().stream().map(resId -> new SysRoleResRel(idWorker.nextId(), securityAuthority.getSecurityUser().getTenantNo(), sysRole.getId(), resId)).collect(Collectors.toList());
                sysRoleResRelDao.saveBatch(sysRoleResRelList);
            }

            log.info("完成 SysAdminServiceImpl.updateRole:" + JSON.toJSONString(param));
        } catch (Exception e) {
            log.info("修改角色信息错误");
            throw CommonException.create(e, ServerResponse.createByError("修改角色信息错误"));
        }
    }

    /**
     * 删除系统角色信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRole(Long roleId, SecurityAuthority securityAuthority) throws Exception {
        log.info("进入 SysAdminServiceImpl.deleteRole:" + roleId);
        SysRole sysRole = sysRoleDao.getById(roleId);
        if (sysRole == null) {
            log.info("角色信息不存在");
            throw CommonException.create(ServerResponse.createByError("角色信息不存在"));
        }
        if (!sysRole.getTenantNo().equals(securityAuthority.getSecurityUser().getTenantNo())) {
            throw CommonException.create(ServerResponse.createByError("非法操作"));
        }


        QueryWrapper<SysUserRoleRel> userRoleExistQueryWrapper = new QueryWrapper<>();
        userRoleExistQueryWrapper.lambda()
                .eq(SysUserRoleRel::getRoleId, roleId)
                .eq(SysUserRoleRel::getTenantNo, securityAuthority.getSecurityUser().getTenantNo());
        if (sysUserRoleRelDao.count(userRoleExistQueryWrapper) > 0) {
            log.info("角色有关联用户不可删除");
            throw CommonException.create(ServerResponse.createByError("角色有关联用户不可删除"));
        }

        try {
            sysRoleDao.removeById(roleId);
            SysRoleResRel deleteByRoleId = new SysRoleResRel();
            deleteByRoleId.setRoleId(roleId);
            QueryWrapper<SysRoleResRel> roleResDelWrapper = new QueryWrapper<>();
            roleResDelWrapper.lambda()
                    .eq(SysRoleResRel::getRoleId, roleId)
                    .eq(SysRoleResRel::getTenantNo, securityAuthority.getSecurityUser().getTenantNo());
            sysRoleResRelDao.remove(roleResDelWrapper);
            log.info("完成 SysAdminServiceImpl.deleteRole:" + roleId);
        } catch (Exception e) {
            log.info("删除角色失败");
            throw CommonException.create(e, ServerResponse.createByError("删除角色失败"));
        }
    }

    /**
     * 查询系统角色信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public PageInfo<SysRoleVo> queryRole(SysRoleQueryParam param, SecurityAuthority securityAuthority) throws Exception {
        log.info("进入 SysAdminServiceImpl.queryRole:" + JSON.toJSONString(param));
        try {
            QueryWrapper<SysRole> queryWrapper = new QueryWrapper<>();
            LambdaQueryWrapper<SysRole> lambda = queryWrapper.lambda();
            lambda.orderByDesc(SysRole::getSortNum, SysRole::getCreateTime);

            lambda.eq(SysRole::getTenantNo, securityAuthority.getSecurityUser().getTenantNo());
            if (StringUtils.isNotEmpty(param.getName())) {
                lambda.like(SysRole::getName, "%" + param.getName() + "%");
            }
            if (StringUtils.isNotEmpty(param.getNo())) {
                lambda.eq(SysRole::getNo, param.getNo());
            }
            if (param.getActiveFlag() != null) {
                lambda.eq(SysRole::getActiveFlag, param.getActiveFlag());
            }
            PageHelper.startPage(param.getPageNum(), param.getPageSize());
            List<SysRole> sysRoles = sysRoleDao.list(queryWrapper);
            PageInfo pageInfo = new PageInfo<>(sysRoles);
            PageHelper.clearPage();

            // 增加vo信息
            List<SysRole> roleList = pageInfo.getList();
            List<SysRoleVo> roleVoList = roleList.stream().map(role -> {
                SysRoleVo sysRoleVo = new SysRoleVo();
                BeanUtils.copyProperties(role, sysRoleVo);
                // 设置角色资源列表
                SysRoleResRel queryParam = new SysRoleResRel();
                queryParam.setRoleId(role.getId());
                QueryWrapper<SysRoleResRel> roleResQuery = new QueryWrapper<>();
                roleResQuery.lambda().eq(SysRoleResRel::getRoleId, role.getId());
                List<SysRoleResRel> sysRoleResRels = sysRoleResRelDao.list(roleResQuery);
                if (CollectionUtils.isNotEmpty(sysRoleResRels)) {
                    List<Long> resIds = sysRoleResRels.stream().map(sysRoleResRel -> sysRoleResRel.getResId()).collect(Collectors.toList());
                    List<SysRes> sysResList = sysResDao.listByIds(resIds);
                    List<SysResVo> sysResVoList = JSONArray.parseArray(JSON.toJSONString(sysResList), SysResVo.class);
                    sysRoleVo.setSysResList(sysResVoList);
                }
                return sysRoleVo;
            }).collect(Collectors.toList());

            pageInfo.setList(roleVoList);
            log.info("完成 SysAdminServiceImpl.queryRole:" + JSON.toJSONString(param));
            return pageInfo;
        } catch (Exception e) {
            log.info("查询角色失败");
            throw CommonException.create(e, ServerResponse.createByError("查询角色失败"));
        }
    }

    /**
     * 获取角色列表
     *
     * @return
     * @throws Exception
     */
    @Override
    public List<SysRoleVo> getRoleList(String roleName, SecurityAuthority securityAuthority) throws Exception {
        log.info("开始 获取角色列表");
        try {
            QueryWrapper<SysRole> queryWrapper = new QueryWrapper<>();
            LambdaQueryWrapper<SysRole> lambda = queryWrapper.lambda();
            lambda.orderByDesc(SysRole::getSortNum, SysRole::getCreateTime);
            lambda.eq(SysRole::getTenantNo, securityAuthority.getSecurityUser().getTenantNo());
            if (StringUtils.isNotBlank(roleName)) {
                lambda.eq(SysRole::getName, roleName);
            }
            List<SysRole> roles = sysRoleDao.list(queryWrapper);
            if (CollectionUtils.isNotEmpty(roles)) {
                List<SysRoleVo> voList = roles.stream().map(ele -> {
                    SysRoleVo sysRoleVo = new SysRoleVo();
                    BeanUtils.copyProperties(ele, sysRoleVo);
                    return sysRoleVo;
                }).collect(Collectors.toList());
                return voList;
            }else {
                return Lists.newArrayList();
            }
        } catch (Exception e) {
            throw CommonException.create(e, ServerResponse.createByError("获取角色列表失败,请联系管理员"));
        }
    }

    /**
     * 查询角色资源列表
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<SysResVo> getSysResListByRoleId(Long roleId, SecurityAuthority securityAuthority) throws Exception {
        log.info("进入 查询角色资源列表:" + roleId);
        // 检查角色是否存在
        SysRole sysRole = sysRoleDao.getById(roleId);
        if (sysRole == null) {
            throw CommonException.create(ServerResponse.createByError("角色不存在"));
        }
        try {
            QueryWrapper<SysRoleResRel> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda()
                    .eq(SysRoleResRel::getRoleId, roleId)
                    .eq(SysRoleResRel::getTenantNo, securityAuthority.getSecurityUser().getTenantNo());
            List<SysRoleResRel> roleResList = sysRoleResRelDao.list(queryWrapper);
            if (org.apache.commons.collections4.CollectionUtils.isEmpty(roleResList)) {
                return Lists.newArrayList();
            }
            List<SysRes> sysRes = sysResDao.listByIds(roleResList.stream().map(ele -> ele.getResId()).collect(Collectors.toList()));
            log.info("完成 查询角色资源列表");
            if (CollectionUtils.isNotEmpty(sysRes)) {
                List<SysResVo> voList = sysRes.stream().map(ele -> {
                    SysResVo sysResVo = new SysResVo();
                    BeanUtils.copyProperties(ele, sysResVo);
                    return sysResVo;
                }).collect(Collectors.toList());
                return voList;
            }else {
                return Lists.newArrayList();
            }

        } catch (Exception e) {
            throw CommonException.create(e, ServerResponse.createByError("获取角色资源列表失败,请联系管理员"));
        }
    }
}
