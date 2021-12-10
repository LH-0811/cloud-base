package com.cloud.base.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cloud.base.common.entity.CommonMethod;
import com.cloud.base.common.exception.CommonException;
import com.cloud.base.common.response.ServerResponse;
import com.cloud.base.common.util.IdWorker;
import com.cloud.base.common.util.Md5Util;
import com.cloud.base.modules.security.core.entity.SecurityAuthority;
import com.cloud.base.modules.security.core.entity.SecurityRes;
import com.cloud.base.modules.security.core.entity.SecurityRole;
import com.cloud.base.modules.security.core.entity.SecurityUser;
import com.cloud.base.user.constant.UCConstant;
import com.cloud.base.user.dto.DeptUserDto;
import com.cloud.base.user.param.*;
import com.cloud.base.user.repository.dao.*;
import com.cloud.base.user.repository.dao.mapper.DeptUserCustomDao;
import com.cloud.base.user.repository.dao.mapper.SysUserMapper;
import com.cloud.base.user.repository.entity.*;
import com.cloud.base.user.service.SysUserService;
import com.cloud.base.user.vo.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
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
    private SysRoleResRelDao sysRoleResRelDao;

    @Resource
    private SysUserRoleRelDao sysUserRoleRelDao;

    @Resource
    private SysRoleDao sysRoleDao;

    @Resource
    private SysResDao sysResDao;

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
    private DeptUserCustomDao deptUserCustomDao;

    @Resource
    private IdWorker idWorker;

    @Resource
    private SysUserMapper sysUserMapper;


    /**
     * 获取用户角色列表
     */
    @Override
    public List<SysRole> getUserRoleList(Long userId) throws Exception {
        log.info("开始  获取用户角色列表");
        SysUser sysUser = sysUserDao.getById(userId);
        if (sysUser == null) {
            throw CommonException.create(ServerResponse.createByError("角色列表不存在"));
        }
        try {
            QueryWrapper<SysUserRoleRel> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(SysUserRoleRel::getUserId, userId);
            List<SysUserRoleRel> userRoleList = sysUserRoleRelDao.list(queryWrapper);
            List<SysRole> sysRoles = sysRoleDao.listByIds(userRoleList.stream().map(ele -> ele.getRoleId()).collect(Collectors.toList()));
            log.info("完成  获取用户角色列表");
            return sysRoles;
        } catch (Exception e) {
            throw CommonException.create(e, ServerResponse.createByError("获取用户角色列表失败,请联系管理员"));
        }
    }

    /**
     * 用户修改密码
     */
    @Override
    public void updateUserPassword(SysUserUpdatePasswordParam param, Long userId) throws Exception {
        log.info("开始 用户修改密码");
        if (!param.getRePwd().equals(param.getNewPwd())) {
            throw CommonException.create(ServerResponse.createByError("两次输入密码不一致"));
        }

        SysUser currentUser = sysUserDao.getById(userId);
        if (currentUser == null) {
            throw CommonException.create(ServerResponse.createByError("当前用户不存在"));
        }
        if (!currentUser.getPassword().equals(Md5Util.getMD5Str(param.getOldPwd()))) {
            throw CommonException.create(ServerResponse.createByError("旧密码不正确"));
        }
        try {
            SysUser updateUser = new SysUser();
            updateUser.setId(currentUser.getId());
            updateUser.setPassword(Md5Util.getMD5Str(param.getNewPwd()));
            sysUserDao.updateById(updateUser);
            log.info("完成 用户修改密码");
        } catch (Exception e) {
            throw CommonException.create(e, ServerResponse.createByError("用户修改密码失败,请联系管理员"));
        }
    }

    /**
     * 获取用户菜单树
     */
    @Override
    public List<MenuVo> getMenuTreeByUser(Long userId) throws Exception {
        log.info("进入 获取用户菜单树");
        try {
            // 获取用户角色
            SysUserRoleRel roleSelect = new SysUserRoleRel();
            roleSelect.setUserId(userId);
            QueryWrapper<SysUserRoleRel> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(SysUserRoleRel::getUserId, userId);
            List<SysUserRoleRel> roleList = sysUserRoleRelDao.list(queryWrapper);
            if (CollectionUtils.isEmpty(roleList)) {
                return Lists.newArrayList();
            }
            // 角色id列表
            List<Long> roleIds = roleList.stream().map(ele -> ele.getRoleId()).collect(Collectors.toList());
            List<Long> resIds = null;
            if (roleIds.contains(1L)) {
                resIds = sysResDao.list(new QueryWrapper<>()).stream().map(ele -> ele.getId()).collect(Collectors.toList());
            } else {
                QueryWrapper<SysRoleResRel> queryWrapper1 = new QueryWrapper<>();
                queryWrapper1.lambda().in(SysRoleResRel::getRoleId, roleIds);
                List<SysRoleResRel> sysRoleReRels = sysRoleResRelDao.list(queryWrapper1);
                if (CollectionUtils.isEmpty(sysRoleReRels)) {
                    return Lists.newArrayList();
                }
                // 资源id列表
                resIds = sysRoleReRels.stream().map(ele -> ele.getResId()).collect(Collectors.toList());
            }

            // 所有的资源列表
            List<SysRes> sysResList = sysResDao.listByIds(resIds).stream().filter(ele -> ele.getType().equals(UCConstant.Type.GROUP.getCode()) || ele.getType().equals(UCConstant.Type.MENU.getCode())).collect(Collectors.toList());
            List<SysResVo> sysResVoList = JSONArray.parseArray(JSON.toJSONString(sysResList),SysResVo.class);
            for (SysResVo sysRes : sysResVoList) {
                sysRes.setParent(JSON.parseObject(JSON.toJSONString(sysResDao.getById(sysRes.getParentId())),SysResVo.class) );
                sysRes.setTitle(sysRes.getName() + "[" + UCConstant.Type.getDescByCode(sysRes.getType()) + "]");
                sysRes.setKey(String.valueOf(sysRes.getId()));
                sysRes.setPkey(String.valueOf(sysRes.getParentId()));
            }

            // 资源转menuVo
            List<MenuVo> menuVoList = sysResVoList.stream().map(ele -> {
                MenuVo menuVo = new MenuVo();
                menuVo.setId(ele.getId());
                menuVo.setParentId(ele.getParentId());
                JSONObject icon = new JSONObject();
                icon.put("type", "icon");
                icon.put("value", ele.getIcon());
                menuVo.setIcon(icon);
                menuVo.setGroup(ele.getType().equals(UCConstant.Type.GROUP.getCode()));
                menuVo.setText(ele.getName());
                menuVo.setLink(ele.getUrl());
                return menuVo;
            }).collect(Collectors.toList());

            // 组装未tree数据
            JSONArray jsonArray = CommonMethod.listToTree(menuVoList, "0", "parentId", "id", "children");
            log.info("完成 获取用户菜单树");
            return jsonArray.toJavaList(MenuVo.class);
        } catch (Exception e) {
            throw CommonException.create(e, ServerResponse.createByError("获取资源树失败"));
        }
    }

    /**
     * 获取用户资源列表
     */
    @Override
    public List<SysRes> getResListByUser(Long userId) throws Exception {
        log.info("进入  获取用户资源列表");
        try {
            // 获取用户角色
            QueryWrapper<SysUserRoleRel> sysUserRoleRelQuery = new QueryWrapper<>();
            sysUserRoleRelQuery.lambda()
                    .eq(SysUserRoleRel::getUserId, userId);
            List<SysUserRoleRel> roleList = sysUserRoleRelDao.list(sysUserRoleRelQuery);
            if (CollectionUtils.isEmpty(roleList)) {
                return Lists.newArrayList();
            }
            // 角色id列表
            List<Long> roleIds = roleList.stream().map(ele -> ele.getRoleId()).collect(Collectors.toList());

            QueryWrapper<SysRoleResRel> roleResRelQueryWrapper = new QueryWrapper<>();
            roleResRelQueryWrapper.lambda().in(SysRoleResRel::getRoleId, roleIds);
            List<SysRoleResRel> sysRoleReRels = sysRoleResRelDao.list(roleResRelQueryWrapper);
            if (CollectionUtils.isEmpty(sysRoleReRels)) {
                return Lists.newArrayList();
            }
            // 资源id列表
            List<Long> resIds = sysRoleReRels.stream().map(ele -> ele.getResId()).collect(Collectors.toList());

            // 所有的资源列表
            List<SysRes> sysRes = sysResDao.listByIds(resIds);

            log.info("完成 获取用户资源列表");
            return sysRes;
        } catch (Exception e) {
            throw CommonException.create(e, ServerResponse.createByError("获取资源树失败"));
        }
    }

    /**
     * 根据用户id 获取用户基本信息
     */
    @Override
    public SysUser getUserByUserId(Long userId) throws Exception {
        log.info("进入 根据用户id获取用户信息接口");
        try {
            SysUser sysUser = sysUserDao.getById(userId);
            log.info("完成 根据用户id获取用户信息接口");
            return sysUser;
        } catch (Exception e) {
            throw CommonException.create(e, ServerResponse.createByError("根据用户id获取用户信息接口失败,请联系管理员"));
        }
    }

    /**
     * 通过用户名 密码获取用户信息
     *
     * @param username
     * @param password
     * @return
     * @throws Exception
     */
    @Override
    public SysUser getUserByUsernameAndPassword(String username, String password) throws Exception {
        log.info("开始 通过用户名 密码获取用户信息");

        SysUser currentUser = null;
        try {
            QueryWrapper<SysUser> sysUserQueryWrapper = new QueryWrapper<>();
//            sysUserQueryWrapper.eq("delFlag",Boolean.FALSE);
//            sysUserQueryWrapper.eq("username",username);
            LambdaQueryWrapper<SysUser> eq = sysUserQueryWrapper.lambda().eq(SysUser::getDelFlag, Boolean.FALSE).eq(SysUser::getUsername, username);
            SysUser sysUser = sysUserMapper.selectById(1L);
            currentUser = sysUserDao.getOne(eq);
        } catch (Exception e) {
            throw CommonException.create(e, ServerResponse.createByError("当前用户不存在"));
        }

        if (currentUser == null) {
            throw CommonException.create(ServerResponse.createByError("当前用户不存在"));
        }

        // 用户输入密码
        String md5Str = Md5Util.getMD5Str(password, currentUser.getSalt());

        if (!currentUser.getPassword().equals(md5Str)) {
            throw CommonException.create(ServerResponse.createByError("密码错误"));
        }
        log.info("完成 通过用户名 密码获取用户信息");
        return currentUser;
    }

    /**
     * 获取部门用户信息
     */
    @Override
    public PageInfo<DeptUserDto> selectDeptUser(SysDeptUserQueryParam param, SysUser sysUser) throws Exception {
        log.info("开始 获取部门角色信息：param=" + JSON.toJSONString(param));
        try {
            PageHelper.startPage(param.getPageNum(), param.getPageSize());
            List<DeptUserDto> deptUserDtos = deptUserCustomDao.selectDeptUser(param);
            PageInfo<DeptUserDto> pageInfo = new PageInfo(deptUserDtos);
            PageHelper.clearPage();

            for (DeptUserDto deptUserDto : deptUserDtos) {
                QueryWrapper<SysUserRoleRel> userRoleRelQueryWrapper = new QueryWrapper<>();
                userRoleRelQueryWrapper.lambda().eq(SysUserRoleRel::getUserId, deptUserDto.getUserId());
                List<SysUserRoleRel> sysUserRoleRelList = sysUserRoleRelDao.list(userRoleRelQueryWrapper);
                if (CollectionUtils.isNotEmpty(sysUserRoleRelList)) {
                    deptUserDto.setRoleIdList(sysUserRoleRelList.stream().map(ele -> ele.getRoleId()).collect(Collectors.toList()));
                }
                QueryWrapper<SysUserPositionRel> userPositionRelQueryWrapper = new QueryWrapper<>();
                userPositionRelQueryWrapper.lambda().eq(SysUserPositionRel::getUserId, deptUserDto.getUserId());
                List<SysUserPositionRel> sysUserPositionRelList = sysUserPositionRelDao.list(userPositionRelQueryWrapper);
                if (CollectionUtils.isNotEmpty(sysUserPositionRelList)) {
                    deptUserDto.setPositionIdList(sysUserPositionRelList.stream().map(ele -> ele.getPositionId()).collect(Collectors.toList()));
                }
            }

            log.info("完成 开始 获取部门角色信息");
            return pageInfo;
        } catch (Exception e) {
            throw CommonException.create(e, ServerResponse.createByError("获取部门角色信息失败,请联系管理员"));
        }
    }


    /**
     * 通过用户名密码 获取用户信息 并组装权限信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public SecurityAuthority verification(UsernamePasswordVerificationParam param) throws Exception {
        log.info("开始 通过用户名密码 获取用户信息 并组装权限信息:{}", JSON.toJSONString(param));
        // 获取到等用户
        SysUser loginUser = getUserByUsernameAndPassword(param.getUsername(), param.getPassword());
        if (loginUser == null) {
            throw CommonException.create(ServerResponse.createByError("用户名或密码错误"));
        }
        if (!loginUser.getActiveFlag()) {
            throw CommonException.create(ServerResponse.createByError("用户不可用请联系管理员"));
        }
        try {
            // 获取用户角色列表
            List<SysRole> userRoleList = getUserRoleList(loginUser.getId());
            // 获取用户资源列表
            List<SysRes> resAllList = getResListByUser(loginUser.getId());

            SecurityAuthority securityAuthority = new SecurityAuthority();
            securityAuthority.setSecurityUser(new SecurityUser(String.valueOf(loginUser.getId()), loginUser.getUsername()));
            if (!CollectionUtils.isEmpty(resAllList)) {
                List<SecurityRes> securityResList = resAllList.stream().map(ele -> new SecurityRes(ele.getType(), ele.getName(), ele.getCode(), ele.getUrl(), "")).collect(Collectors.toList());
                securityAuthority.setSecurityResList(securityResList);
            }
            if (!CollectionUtils.isEmpty(userRoleList)) {
                List<SecurityRole> securityRoleList = userRoleList.stream().map(ele -> new SecurityRole(ele.getId(), ele.getNo(), ele.getName())).collect(Collectors.toList());
                securityAuthority.setSecurityRoleList(securityRoleList);
            }
            SysUser updateParam = new SysUser();
            updateParam.setId(loginUser.getId());
            updateParam.setLastLogin(new Date());
            sysUserDao.updateById(updateParam);
            log.info("完成 通过用户名密码 获取用户信息 并组装权限信息:{}", JSON.toJSONString(param));
            return securityAuthority;
        } catch (Exception e) {
            throw CommonException.create(e, ServerResponse.createByError("通过用户名密码获取用户信息失败,请联系管理员"));
        }

    }


    // //////////////// 用户管理

    /**
     * 创建用户
     *
     * @param param
     * @param sysUser
     * @throws Exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createUser(SysUserCreateParam param, SysUser sysUser) throws Exception {
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
            String salt = RandomStringUtils.random(4);
            sysUserNew.setSalt(salt);
            sysUserNew.setCreateBy(sysUser.getId());
            sysUserNew.setCreateTime(new Date());
            sysUserNew.setPassword(Md5Util.getMD5Str("123456", salt));
            sysUserNew.setActiveFlag(true);
            sysUserNew.setDelFlag(false);
            sysUserNew.setCreateBy(sysUser.getId());
            sysUserNew.setCreateTime(new Date());
            sysUserDao.save(sysUserNew);

            // 保存用户部门信息
            if (param.getDeptId() != null) {
                SysUserDeptRel sysUserDeptRel = new SysUserDeptRel();
                sysUserDeptRel.setId(idWorker.nextId());
                sysUserDeptRel.setUserId(sysUserNew.getId());
                sysUserDeptRel.setDeptId(param.getDeptId());
                sysUserDeptRelDao.save(sysUserDeptRel);
            }

            // 保存用户岗位信息
            if (CollectionUtils.isNotEmpty(param.getPositionIdList())) {
                List<SysUserPositionRel> sysUserPositionRelList = param.getPositionIdList().stream().map(ele -> new SysUserPositionRel(idWorker.nextId(), sysUserNew.getId(), ele)).collect(Collectors.toList());
                sysUserPositionRelDao.saveBatch(sysUserPositionRelList);
            }

            // 保存用户角色信息
            if (CollectionUtils.isNotEmpty(param.getRoleIdList())) {
                List<SysUserRoleRel> sysUserRoleRelList = param.getRoleIdList().stream().map(ele -> new SysUserRoleRel(idWorker.nextId(), sysUserNew.getId(), ele)).collect(Collectors.toList());
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
     * @param sysUser
     * @throws Exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(SysUserUpdateParam param, SysUser sysUser) throws Exception {
        log.info("开始 修改用户");
        try {
            // 更新用户信息
            SysUser updateUser = new SysUser();
            BeanUtils.copyProperties(param, updateUser);
            updateUser.setId(param.getUserId());
            updateUser.setUpdateBy(sysUser.getId());
            updateUser.setUpdateTime(new Date());
            sysUserDao.updateById(updateUser);

            // 保存用户岗位信息
            if (CollectionUtils.isNotEmpty(param.getPositionIdList())) {

                QueryWrapper<SysUserPositionRel> delSysUserPositionRel = new QueryWrapper<>();
                delSysUserPositionRel.lambda().eq(SysUserPositionRel::getUserId, param.getUserId());
                sysUserPositionRelDao.remove(delSysUserPositionRel);

                List<SysUserPositionRel> sysUserPositionRelList = param.getPositionIdList().stream().map(ele -> new SysUserPositionRel(idWorker.nextId(), param.getUserId(), ele)).collect(Collectors.toList());
                sysUserPositionRelDao.saveBatch(sysUserPositionRelList);
            }

            // 保存用户角色信息
            if (CollectionUtils.isNotEmpty(param.getRoleIdList())) {
                QueryWrapper<SysUserRoleRel> delWrapper = new QueryWrapper<>();
                delWrapper.lambda().eq(SysUserRoleRel::getUserId, param.getUserId());
                sysUserRoleRelDao.remove(delWrapper);

                List<SysUserRoleRel> sysUserRoleRelList = param.getRoleIdList().stream().map(ele -> new SysUserRoleRel(idWorker.nextId(), param.getUserId(), ele)).collect(Collectors.toList());
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
     * @param sysUser
     * @return
     * @throws Exception
     */
    @Override
    public PageInfo<SysUserVo> queryUser(SysUserQueryParam param, SysUser sysUser) throws Exception {
        log.info("开始 查询用户");
        try {
            QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
            LambdaQueryWrapper<SysUser> lambda = queryWrapper.lambda();
            lambda.orderByDesc(SysUser::getCreateTime);
            lambda.eq(SysUser::getDelFlag, Boolean.FALSE);
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
    public void delUser(Long userId, SysUser sysUser) throws Exception {
        log.info("开始 删除用户");
        SysUser checkUser = sysUserDao.getById(userId);
        if (checkUser == null) {
            throw CommonException.create(ServerResponse.createByError("用户信息不存在"));
        }
        try {
            // 删除用户
            checkUser.setDelFlag(true);
            checkUser.setUpdateBy(sysUser.getId());
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
    public void resetPassword(Long userId, SysUser sysUser) throws Exception {
        log.info("开始 重置用户密码");
        SysUser checkUser = sysUserDao.getById(userId);
        if (checkUser == null) {
            throw CommonException.create(ServerResponse.createByError("用户信息不存在"));
        }
        try {
            // 重置用户名密码
            SysUser updateUser = new SysUser();
            updateUser.setId(checkUser.getId());
            updateUser.setPassword(Md5Util.getMD5Str("123456", checkUser.getSalt()));
            updateUser.setUpdateBy(sysUser.getId());
            updateUser.setUpdateTime(new Date());
            sysUserDao.updateById(updateUser);
            log.info("完成 重置用户密码");
        } catch (Exception e) {
            throw CommonException.create(e, ServerResponse.createByError("重置用户密码失败,请联系管理员"));
        }
    }
}
