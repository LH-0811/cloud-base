package com.cloud.base.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cloud.base.core.common.entity.CommonMethod;
import com.cloud.base.core.common.exception.CommonException;
import com.cloud.base.core.common.response.ServerResponse;
import com.cloud.base.core.common.util.IdWorker;
import com.cloud.base.core.common.util.Md5Util;
import com.cloud.base.core.modules.lh_security.core.entity.SecurityAuthority;
import com.cloud.base.core.modules.lh_security.core.entity.SecurityRes;
import com.cloud.base.core.modules.lh_security.core.entity.SecurityRole;
import com.cloud.base.core.modules.lh_security.core.entity.SecurityUser;
import com.cloud.base.user.param.*;
import com.cloud.base.user.repository.dao.*;
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
import tk.mybatis.mapper.entity.Example;

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
    private SysUserDao sysUserDao;

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
    private SysPositionDao sysPositionDao;

    @Resource
    private IdWorker idWorker;


    /**
     * 获取用户角色列表
     */
    @Override
    public List<SysRole> getUserRoleList(Long userId) throws Exception {
        log.info("开始  获取用户角色列表");
        SysUser sysUser = sysUserDao.selectByPrimaryKey(userId);
        if (sysUser == null) {
            throw CommonException.create(ServerResponse.createByError("角色列表不存在"));
        }
        try {
            SysUserRoleRel selectParam = new SysUserRoleRel();
            selectParam.setUserId(userId);
            List<SysUserRoleRel> userRoleList = sysUserRoleRelDao.select(selectParam);
            List<SysRole> sysRoles = sysRoleDao.selectByIdList(userRoleList.stream().map(ele -> ele.getRoleId()).collect(Collectors.toList()));
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

        SysUser currentUser = sysUserDao.selectByPrimaryKey(userId);
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
            sysUserDao.updateByPrimaryKeySelective(updateUser);
            log.info("完成 用户修改密码");
        } catch (Exception e) {
            throw CommonException.create(e, ServerResponse.createByError("用户修改密码失败,请联系管理员"));
        }
    }

    /**
     * 获取用户资源树
     */
    @Override
    public List<SysRes> getResTreeByUser(Long userId) throws Exception {
        log.info("进入 获取用户资源树");
        try {
            // 获取用户角色
            SysUserRoleRel roleSelect = new SysUserRoleRel();
            roleSelect.setUserId(userId);
            List<SysUserRoleRel> roleList = sysUserRoleRelDao.select(roleSelect);
            if (CollectionUtils.isEmpty(roleList)) {
                return Lists.newArrayList();
            }
            // 角色id列表
            List<Long> roleIds = roleList.stream().map(ele -> ele.getRoleId()).collect(Collectors.toList());
            Example example = new Example(SysRoleResRel.class);
            example.createCriteria().andIn("roleId", roleIds);
            List<SysRoleResRel> sysRoleReRels = sysRoleResRelDao.selectByExample(example);
            if (CollectionUtils.isEmpty(sysRoleReRels)) {
                return Lists.newArrayList();
            }
            // 资源id列表
            List<Long> resIds = sysRoleReRels.stream().map(ele -> ele.getResId()).collect(Collectors.toList());

            // 所有的资源列表
            List<SysRes> sysResList = sysResDao.selectByIdList(resIds);
            for (SysRes sysRes : sysResList) {
                sysRes.setParent(sysResDao.selectByPrimaryKey(sysRes.getParentId()));
                sysRes.setTitle(sysRes.getName() + "[" + SysRes.Type.getDescByCode(sysRes.getType()) + "]");
                sysRes.setKey(String.valueOf(sysRes.getId()));
                sysRes.setPkey(String.valueOf(sysRes.getParentId()));
            }
            // 组装未tree数据
            JSONArray jsonArray = CommonMethod.listToTree(sysResList, "0", "parentId", "id", "children");
            log.info("完成 获取用户资源树");
            return jsonArray.toJavaList(SysRes.class);
        } catch (Exception e) {
            throw CommonException.create(e, ServerResponse.createByError("获取资源树失败"));
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
            List<SysUserRoleRel> roleList = sysUserRoleRelDao.select(roleSelect);
            if (CollectionUtils.isEmpty(roleList)) {
                return Lists.newArrayList();
            }
            // 角色id列表
            List<Long> roleIds = roleList.stream().map(ele -> ele.getRoleId()).collect(Collectors.toList());
            List<Long> resIds = null;
            if (roleIds.contains(1L)) {
                resIds = sysResDao.selectAll().stream().map(ele -> ele.getId()).collect(Collectors.toList());
            } else {
                Example example = new Example(SysRoleResRel.class);
                example.createCriteria().andIn("roleId", roleIds);
                List<SysRoleResRel> sysRoleReRels = sysRoleResRelDao.selectByExample(example);
                if (CollectionUtils.isEmpty(sysRoleReRels)) {
                    return Lists.newArrayList();
                }
                // 资源id列表
                resIds = sysRoleReRels.stream().map(ele -> ele.getResId()).collect(Collectors.toList());
            }

            // 所有的资源列表
            List<SysRes> sysResList = sysResDao.selectByIdList(resIds).stream().filter(ele -> ele.getType().equals(1) || ele.getType().equals(2)).collect(Collectors.toList());
            for (SysRes sysRes : sysResList) {
                sysRes.setParent(sysResDao.selectByPrimaryKey(sysRes.getParentId()));
                sysRes.setTitle(sysRes.getName() + "[" + SysRes.Type.getDescByCode(sysRes.getType()) + "]");
                sysRes.setKey(String.valueOf(sysRes.getId()));
                sysRes.setPkey(String.valueOf(sysRes.getParentId()));
            }

            // 资源转menuVo
            List<MenuVo> menuVoList = sysResList.stream().map(ele -> {
                MenuVo menuVo = new MenuVo();
                menuVo.setId(ele.getId());
                menuVo.setParentId(ele.getParentId());
//                menuVo.setIcon(ele.getIcon());
                JSONObject icon = new JSONObject();
                icon.put("type", "icon");
                icon.put("value", ele.getIcon());
                menuVo.setIcon(icon);
                menuVo.setGroup(ele.getType().equals(SysRes.Type.MENU.getCode()));
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
            SysUserRoleRel roleSelect = new SysUserRoleRel();
            roleSelect.setUserId(userId);
            List<SysUserRoleRel> roleList = sysUserRoleRelDao.select(roleSelect);
            if (CollectionUtils.isEmpty(roleList)) {
                return Lists.newArrayList();
            }
            // 角色id列表
            List<Long> roleIds = roleList.stream().map(ele -> ele.getRoleId()).collect(Collectors.toList());
            Example example = new Example(SysRoleResRel.class);
            example.createCriteria().andIn("roleId", roleIds);
            List<SysRoleResRel> sysRoleReRels = sysRoleResRelDao.selectByExample(example);
            if (CollectionUtils.isEmpty(sysRoleReRels)) {
                return Lists.newArrayList();
            }
            // 资源id列表
            List<Long> resIds = sysRoleReRels.stream().map(ele -> ele.getResId()).collect(Collectors.toList());

            // 所有的资源列表
            List<SysRes> sysRes = sysResDao.selectByIdList(resIds);

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
            SysUser sysUser = sysUserDao.selectByPrimaryKey(userId);
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
            SysUser selectByUsername = new SysUser();
            selectByUsername.setDelFlag(false);
            selectByUsername.setUsername(username);
            currentUser = sysUserDao.selectOne(selectByUsername);
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
     * 通过用户名密码 获取用户信息 并组装权限信息
     */
    @Override
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
        // 获取用户角色列表
        List<SysRole> userRoleList = getUserRoleList(loginUser.getId());
        // 获取用户资源列表
        List<SysRes> resAllList = getResListByUser(loginUser.getId());

        SecurityAuthority securityAuthority = new SecurityAuthority();
        securityAuthority.setSecurityUser(new SecurityUser(String.valueOf(loginUser.getId()), loginUser.getUsername()));
//      securityAuthority.setSecurityResList(Lists.newArrayList(SecurityRes.allUrlRes(),SecurityRes.allCodeRes(),SecurityRes.allStaticResPath()));
        if (!CollectionUtils.isEmpty(resAllList)) {
            List<SecurityRes> securityResList = resAllList.stream().map(ele -> new SecurityRes(ele.getType(), ele.getName(), ele.getCode(), ele.getUrl(), "")).collect(Collectors.toList());
            securityAuthority.setSecurityResList(securityResList);
        }
        if (!CollectionUtils.isEmpty(userRoleList)) {
            List<SecurityRole> securityRoleList = userRoleList.stream().map(ele -> new SecurityRole(ele.getId(), ele.getNo(), ele.getName())).collect(Collectors.toList());
            securityAuthority.setSecurityRoleList(securityRoleList);
        }
        log.info("完成 通过用户名密码 获取用户信息 并组装权限信息:{}", JSON.toJSONString(param));
        return securityAuthority;
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
        SysUser nameCheckParam = new SysUser();
        nameCheckParam.setUsername(param.getUsername());
        nameCheckParam.setDelFlag(false);
        if (sysUserDao.selectCount(nameCheckParam) > 0) {
            throw CommonException.create(ServerResponse.createByError("用户名已经存在"));
        }

        SysUser phoneCheckParam = new SysUser();
        phoneCheckParam.setPhone(param.getPhone());
        phoneCheckParam.setDelFlag(false);
        if (sysUserDao.selectCount(phoneCheckParam) > 0) {
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
            sysUserDao.insertSelective(sysUserNew);

            // 保存用户岗位信息
            if (CollectionUtils.isNotEmpty(param.getPositionIdList())) {
                List<SysUserPositionRel> sysUserPositionRelList = param.getPositionIdList().stream().map(ele -> new SysUserPositionRel(idWorker.nextId(), sysUserNew.getId(), ele)).collect(Collectors.toList());
                sysUserPositionRelDao.insertList(sysUserPositionRelList);
            }

            // 保存用户角色信息
            if (CollectionUtils.isNotEmpty(param.getRoleIdList())) {
                List<SysUserRoleRel> sysUserRoleRelList = param.getRoleIdList().stream().map(ele -> new SysUserRoleRel(idWorker.nextId(), sysUserNew.getId(), ele)).collect(Collectors.toList());
                sysUserRoleRelDao.insertList(sysUserRoleRelList);
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
            updateUser.setUpdateBy(sysUser.getId());
            updateUser.setUpdateTime(new Date());
            sysUserDao.updateByPrimaryKeySelective(updateUser);

            // 保存用户岗位信息
            if (CollectionUtils.isNotEmpty(param.getPositionIdList())) {

                SysUserPositionRel delParam = new SysUserPositionRel();
                delParam.setUserId(param.getId());
                sysUserPositionRelDao.delete(delParam);

                List<SysUserPositionRel> sysUserPositionRelList = param.getPositionIdList().stream().map(ele -> new SysUserPositionRel(idWorker.nextId(), param.getId(), ele)).collect(Collectors.toList());
                sysUserPositionRelDao.insertList(sysUserPositionRelList);
            }

            // 保存用户角色信息
            if (CollectionUtils.isNotEmpty(param.getRoleIdList())) {

                SysUserRoleRel delParam = new SysUserRoleRel();
                delParam.setUserId(param.getId());
                sysUserRoleRelDao.delete(delParam);

                List<SysUserRoleRel> sysUserRoleRelList = param.getRoleIdList().stream().map(ele -> new SysUserRoleRel(idWorker.nextId(), param.getId(), ele)).collect(Collectors.toList());
                sysUserRoleRelDao.insertList(sysUserRoleRelList);
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
            Example example = new Example(SysUser.class);
            example.setOrderByClause(" create_time desc ");
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("delFlag", false);
            if (param.getUserType() != null) {
                criteria.andEqualTo("userType", param.getUserType());
            }
            if (StringUtils.isNotBlank(param.getUsername())) {
                criteria.andLike("username", "%" + param.getUsername() + "%");
            }
            if (StringUtils.isNotBlank(param.getNickName())) {
                criteria.andLike("nickName", "%" + param.getNickName() + "%");
            }
            if (StringUtils.isNotBlank(param.getPhone())) {
                criteria.andLike("phone", "%" + param.getPhone() + "%");
            }
            if (param.getActiveFlag() != null) {
                criteria.andEqualTo("activeFlag", param.getActiveFlag());
            }
            if (param.getLastLoginLow() != null) {
                criteria.andGreaterThanOrEqualTo("lastLogin", param.getLastLoginLow());
            }
            if (param.getLastLoginUp() != null) {
                criteria.andLessThanOrEqualTo("lastLogin", param.getLastLoginUp());
            }
            if (param.getCreateTimeLow() != null) {
                criteria.andGreaterThanOrEqualTo("createTime", param.getCreateTimeLow());
            }
            if (param.getCreateTimeUp() != null) {
                criteria.andLessThanOrEqualTo("createTime", param.getCreateTimeUp());
            }
            PageHelper.startPage(param.getPageNum(), param.getPageSize());
            List<SysUser> sysUsers = sysUserDao.selectByExample(example);
            PageInfo pageInfo = new PageInfo(sysUsers);
            PageHelper.clearPage();

            // 补充vo信息
            List<SysUser> sysUserList = pageInfo.getList();
            List<SysUserVo> sysUserVoStream = sysUserList.stream().map(ele -> {
                SysUserVo sysUserVo = new SysUserVo();
                BeanUtils.copyProperties(ele, sysUserVo);

                // 部门信息
                SysUserDeptRel sysUserDeptRelParam = new SysUserDeptRel();
                sysUserDeptRelParam.setUserId(ele.getId());
                SysUserDeptRel sysUserDeptRel = sysUserDeptRelDao.selectOne(sysUserDeptRelParam);
                if (sysUserDeptRel != null) {
                    SysDept sysDept = sysDeptDao.selectByPrimaryKey(sysUserDeptRel.getDeptId());
                    SysDeptVo sysDeptVo = new SysDeptVo();
                    BeanUtils.copyProperties(sysDept, sysDeptVo);
                    sysUserVo.setDeptInfo(sysDeptVo);
                }

                // 岗位信息
                SysUserPositionRel sysUserPositionRelParam = new SysUserPositionRel();
                sysUserPositionRelParam.setUserId(ele.getId());
                List<SysUserPositionRel> sysUserPositionRelList = sysUserPositionRelDao.select(sysUserPositionRelParam);
                if (CollectionUtils.isNotEmpty(sysUserPositionRelList)) {
                    List<Long> positionIdList = sysUserPositionRelList.stream().map(userPositionRel -> userPositionRel.getPositionId()).collect(Collectors.toList());
                    List<SysPosition> sysPositions = sysPositionDao.selectByIdList(positionIdList);
                    if (CollectionUtils.isNotEmpty(sysPositions)) {
                        List<SysPositionVo> sysPositionVoList = sysPositions.stream().map(sysPosition -> {
                            SysPositionVo sysPositionVo = new SysPositionVo();
                            BeanUtils.copyProperties(sysPosition, sysPositionVo);
                            return sysPositionVo;
                        }).collect(Collectors.toList());
                        sysUserVo.setPositionList(sysPositionVoList);
                    }
                }

                // 角色信息
                SysUserRoleRel sysUserRoleRelParam = new SysUserRoleRel();
                sysUserRoleRelParam.setUserId(ele.getId());
                List<SysUserRoleRel> sysUserRoleRelList = sysUserRoleRelDao.select(sysUserRoleRelParam);
                if (CollectionUtils.isNotEmpty(sysUserRoleRelList)) {
                    List<Long> sysRoleIdList = sysUserRoleRelList.stream().map(sysUserRoleRel -> sysUserRoleRel.getRoleId()).collect(Collectors.toList());
                    List<SysRole> sysRoles = sysRoleDao.selectByIdList(sysRoleIdList);
                    if (CollectionUtils.isNotEmpty(sysRoles)) {
                        List<SysRoleVo> sysRoleVoList = sysRoles.stream().map(sysRole -> {
                            SysRoleVo sysRoleVo = new SysRoleVo();
                            BeanUtils.copyProperties(sysRole, sysRoleVo);
                            return sysRoleVo;
                        }).collect(Collectors.toList());
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
        SysUser checkUser = sysUserDao.selectByPrimaryKey(userId);
        if (checkUser == null) {
            throw CommonException.create(ServerResponse.createByError("用户信息不存在"));
        }
        try {
            // 删除用户
            checkUser.setDelFlag(true);
            checkUser.setUpdateBy(sysUser.getId());
            checkUser.setUpdateTime(new Date());
            sysUserDao.updateByPrimaryKeySelective(checkUser);
            log.info("完成 删除用户");
        } catch (Exception e) {
            throw CommonException.create(e, ServerResponse.createByError("删除用户失败,请联系管理员"));
        }
    }


}
