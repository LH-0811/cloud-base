package com.cloud.base.member.user.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cloud.base.core.common.entity.CommonMethod;
import com.cloud.base.core.common.response.ServerResponse;
import com.cloud.base.core.common.exception.CommonException;
import com.cloud.base.core.common.util.Md5Util;
import com.cloud.base.member.user.param.SysUserUpdatePasswordParam;
import com.cloud.base.member.user.repository.dao.*;
import com.cloud.base.member.user.repository.entity.*;
import com.cloud.base.member.user.vo.MenuVo;
import com.cloud.base.member.user.service.SysUserService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

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

    @Autowired
    private SysUserDao sysUserDao;

    @Autowired
    private SysRoleResDao sysRoleResDao;

    @Autowired
    private SysUserRoleDao sysUserRoleDao;

    @Autowired
    private SysRoleDao sysRoleDao;

    @Autowired
    private SysResDao sysResDao;


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
            SysUserRole selectParam = new SysUserRole();
            selectParam.setUserId(userId);
            List<SysUserRole> userRoleList = sysUserRoleDao.select(selectParam);
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
     *
     */
    @Override
    public List<SysRes> getResTreeByUser(Long userId) throws Exception {
        log.info("进入 获取用户资源树");
        try {
            // 获取用户角色
            SysUserRole roleSelect = new SysUserRole();
            roleSelect.setUserId(userId);
            List<SysUserRole> roleList = sysUserRoleDao.select(roleSelect);
            if (CollectionUtils.isEmpty(roleList)) {
                return Lists.newArrayList();
            }
            // 角色id列表
            List<Long> roleIds = roleList.stream().map(ele -> ele.getRoleId()).collect(Collectors.toList());
            Example example = new Example(SysRoleRes.class);
            example.createCriteria().andIn("roleId", roleIds);
            List<SysRoleRes> sysRoleRes = sysRoleResDao.selectByExample(example);
            if (CollectionUtils.isEmpty(sysRoleRes)) {
                return Lists.newArrayList();
            }
            // 资源id列表
            List<Long> resIds = sysRoleRes.stream().map(ele -> ele.getResId()).collect(Collectors.toList());

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
     *
     */
    @Override
    public List<MenuVo> getMenuTreeByUser(Long userId) throws Exception {
        log.info("进入 获取用户菜单树");
        try {
            // 获取用户角色
            SysUserRole roleSelect = new SysUserRole();
            roleSelect.setUserId(userId);
            List<SysUserRole> roleList = sysUserRoleDao.select(roleSelect);
            if (CollectionUtils.isEmpty(roleList)) {
                return Lists.newArrayList();
            }
            // 角色id列表
            List<Long> roleIds = roleList.stream().map(ele -> ele.getRoleId()).collect(Collectors.toList());
            List<Long> resIds = null;
            if (roleIds.contains(1L)){
                resIds = sysResDao.selectAll().stream().map(ele->ele.getId()).collect(Collectors.toList());
            }else {
                Example example = new Example(SysRoleRes.class);
                example.createCriteria().andIn("roleId", roleIds);
                List<SysRoleRes> sysRoleRes = sysRoleResDao.selectByExample(example);
                if (CollectionUtils.isEmpty(sysRoleRes)) {
                    return Lists.newArrayList();
                }
                // 资源id列表
                resIds = sysRoleRes.stream().map(ele -> ele.getResId()).collect(Collectors.toList());
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
            SysUserRole roleSelect = new SysUserRole();
            roleSelect.setUserId(userId);
            List<SysUserRole> roleList = sysUserRoleDao.select(roleSelect);
            if (CollectionUtils.isEmpty(roleList)) {
                return Lists.newArrayList();
            }
            // 角色id列表
            List<Long> roleIds = roleList.stream().map(ele -> ele.getRoleId()).collect(Collectors.toList());
            Example example = new Example(SysRoleRes.class);
            example.createCriteria().andIn("roleId", roleIds);
            List<SysRoleRes> sysRoleRes = sysRoleResDao.selectByExample(example);
            if (CollectionUtils.isEmpty(sysRoleRes)) {
                return Lists.newArrayList();
            }
            // 资源id列表
            List<Long> resIds = sysRoleRes.stream().map(ele -> ele.getResId()).collect(Collectors.toList());

            // 所有的资源列表
            List<SysRes> sysRes = sysResDao.selectByIdList(resIds);

            log.info("完成 获取用户资源列表");
            return sysRes;
        } catch (Exception e) {
            throw CommonException.create(e, ServerResponse.createByError("获取资源树失败"));
        }
    }

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

        if (currentUser == null){
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
}
