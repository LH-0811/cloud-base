package com.cloud.base.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cloud.base.common.core.entity.CommonMethod;
import com.cloud.base.common.core.exception.CommonException;
import com.cloud.base.common.core.response.ServerResponse;
import com.cloud.base.common.core.util.Md5Util;
import com.cloud.base.common.xugou.core.model.entity.SecurityAuthority;
import com.cloud.base.common.xugou.core.model.entity.SecurityRes;
import com.cloud.base.common.xugou.core.model.entity.SecurityRole;
import com.cloud.base.common.xugou.core.model.entity.SecurityUser;
import com.cloud.base.user.constant.UCConstant;
import com.cloud.base.user.dto.DeptUserDto;
import com.cloud.base.user.param.SysDeptUserQueryParam;
import com.cloud.base.user.param.SysUserUpdatePasswordParam;
import com.cloud.base.user.param.UsernamePasswordVerificationParam;
import com.cloud.base.user.repository.dao.*;
import com.cloud.base.user.repository.dao.mapper.DeptUserCustomDao;
import com.cloud.base.user.repository.dao.mapper.SysUserMapper;
import com.cloud.base.user.repository.entity.*;
import com.cloud.base.user.service.CurrentUserService;
import com.cloud.base.user.vo.MenuVo;
import com.cloud.base.user.vo.SysResVo;
import com.cloud.base.user.vo.SysRoleVo;
import com.cloud.base.user.vo.SysUserVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lh0811
 * @date 2021/12/29
 */
@Slf4j
@Service
public class CurrentUserServiceImpl implements CurrentUserService {

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
    private SysUserDao sysUserDao;

    @Resource
    private DeptUserCustomDao deptUserCustomDao;

    @Resource
    private SysUserMapper sysUserMapper;


    /**
     * 获取用户角色列表
     */
    @Override
    public List<SysRoleVo> getUserRoleList(Long userId) throws Exception {
        log.info("开始  获取用户角色列表");
        try {
            QueryWrapper<SysUserRoleRel> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(SysUserRoleRel::getUserId, userId);
            List<SysUserRoleRel> userRoleList = sysUserRoleRelDao.list(queryWrapper);
            List<SysRole> sysRoles = sysRoleDao.listByIds(userRoleList.stream().map(ele -> ele.getRoleId()).collect(Collectors.toList()));
            log.info("完成  获取用户角色列表");
            if (CollectionUtils.isEmpty(sysRoles)) {
                return Lists.newArrayList();
            } else {
                List<SysRoleVo> voList = sysRoles.stream().map(ele -> {
                    SysRoleVo sysRoleVo = new SysRoleVo();
                    BeanUtils.copyProperties(ele, sysRoleVo);
                    return sysRoleVo;
                }).collect(Collectors.toList());
                return voList;
            }
        } catch (Exception e) {
            throw CommonException.create(e, ServerResponse.createByError("获取用户角色列表失败,请联系管理员"));
        }
    }

    /**
     * 用户修改密码
     */
    @Override
    public void updateUserPassword(SysUserUpdatePasswordParam param, SecurityAuthority securityAuthority) throws Exception {
        log.info("开始 用户修改密码");
        SysUser currentUser = sysUserDao.getById(Long.valueOf(securityAuthority.getSecurityUser().getId()));
        if (currentUser == null) {
            throw CommonException.create(ServerResponse.createByError("当前用户不存在"));
        }
        if (!param.getRePwd().equals(param.getNewPwd())) {
            throw CommonException.create(ServerResponse.createByError("两次输入密码不一致"));
        }

        if (!currentUser.getPassword().equals(Md5Util.getMD5Str(param.getOldPwd(), currentUser.getSalt()))) {
            throw CommonException.create(ServerResponse.createByError("旧密码不正确"));
        }
        try {
            SysUser updateUser = new SysUser();
            updateUser.setId(currentUser.getId());
            updateUser.setPassword(Md5Util.getMD5Str(param.getNewPwd(), currentUser.getSalt()));
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
            QueryWrapper<SysUserRoleRel> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda()
                    .eq(SysUserRoleRel::getUserId, userId);
            List<SysUserRoleRel> roleList = sysUserRoleRelDao.list(queryWrapper);
            if (CollectionUtils.isEmpty(roleList)) {
                return Lists.newArrayList();
            }
            // 角色id列表
            List<Long> roleIds = roleList.stream().map(ele -> ele.getRoleId()).collect(Collectors.toList());
            QueryWrapper<SysRoleResRel> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.lambda().in(SysRoleResRel::getRoleId, roleIds);
            List<SysRoleResRel> sysRoleReRels = sysRoleResRelDao.list(queryWrapper1);
            if (CollectionUtils.isEmpty(sysRoleReRels)) {
                return Lists.newArrayList();
            }
            // 资源id列表
            List<Long> resIds = sysRoleReRels.stream().map(ele -> ele.getResId()).collect(Collectors.toList());

            // 所有的资源列表
            List<SysRes> sysResList = sysResDao.listByIds(resIds).stream().filter(ele -> ele.getType().equals(UCConstant.Type.GROUP.getCode()) || ele.getType().equals(UCConstant.Type.MENU.getCode())).collect(Collectors.toList());
            // 按照orderNo 升序排序
            sysResList = sysResList.stream().sorted(Comparator.comparing(SysRes::getOrderNo)).collect(Collectors.toList());

            List<SysResVo> sysResVoList = JSONArray.parseArray(JSON.toJSONString(sysResList), SysResVo.class);
            for (SysResVo sysRes : sysResVoList) {
                sysRes.setParent(JSON.parseObject(JSON.toJSONString(sysResDao.getById(sysRes.getParentId())), SysResVo.class));
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
    public List<SysResVo> getResListByUser(Long userId) throws Exception {
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
            if (CollectionUtils.isEmpty(sysRes)) {
                return Lists.newArrayList();
            } else {
                List<SysResVo> voList = sysRes.stream().map(ele -> {
                    SysResVo sysResVo = new SysResVo();
                    BeanUtils.copyProperties(ele, sysResVo);
                    return sysResVo;
                }).collect(Collectors.toList());
                return voList;
            }
        } catch (Exception e) {
            throw CommonException.create(e, ServerResponse.createByError("获取资源树失败"));
        }
    }

    /**
     * 根据用户id 获取用户基本信息
     */
    @Override
    public SysUserVo getUserByUserId(Long userId) throws Exception {
        log.info("进入 根据用户id获取用户信息接口");
        try {
            SysUser sysUser = sysUserDao.getById(userId);
            log.info("完成 根据用户id获取用户信息接口");
            if (sysUser != null) {
                SysUserVo sysUserVo = new SysUserVo();
                BeanUtils.copyProperties(sysUser,sysUserVo);
                return sysUserVo;
            }
            return null;
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
    public SysUserVo getUserByUsernameAndPassword(String username, String password) throws Exception {
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
        if (currentUser != null) {
            SysUserVo sysUserVo = new SysUserVo();
            BeanUtils.copyProperties(currentUser,sysUserVo);
            return sysUserVo;
        }
        return null;
    }

    /**
     * 获取部门用户信息
     */
    @Override
    public PageInfo<DeptUserDto> selectDeptUser(SysDeptUserQueryParam param, SecurityAuthority securityAuthority) throws Exception {
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
        SysUserVo loginUser = getUserByUsernameAndPassword(param.getUsername(), param.getPassword());
        if (loginUser == null) {
            throw CommonException.create(ServerResponse.createByError("用户名或密码错误"));
        }
        if (!loginUser.getActiveFlag()) {
            throw CommonException.create(ServerResponse.createByError("用户不可用请联系管理员"));
        }
        try {
            // 获取用户角色列表
            List<SysRoleVo> userRoleList = getUserRoleList(loginUser.getId());
            // 获取用户资源列表
            List<SysResVo> resAllList = getResListByUser(loginUser.getId());

            SecurityAuthority securityAuthority = new SecurityAuthority();
            securityAuthority.setSecurityUser(new SecurityUser(String.valueOf(loginUser.getId()), loginUser.getTenantNo(), loginUser.getUsername()));
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

}
