package com.cloud.base.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cloud.base.core.common.entity.CommonMethod;
import com.cloud.base.core.common.response.ServerResponse;
import com.cloud.base.core.common.exception.CommonException;
import com.cloud.base.core.common.util.IdWorker;
import com.cloud.base.core.common.util.Md5Util;
import com.cloud.base.core.common.util.ThreadLog;
import com.cloud.base.core.modules.lh_security.core.entity.SecurityAuthority;
import com.cloud.base.core.modules.lh_security.core.entity.SecurityRes;
import com.cloud.base.core.modules.lh_security.core.entity.SecurityRole;
import com.cloud.base.core.modules.lh_security.core.entity.SecurityUser;
import com.cloud.base.user.dto.DeptUserDto;
import com.cloud.base.member.user.param.*;
import com.cloud.base.member.user.repository.dao.*;
import com.cloud.base.user.repository.dao.*;
import com.cloud.base.user.repository.dao.custom.DeptUserCustomDao;
import com.cloud.base.member.user.repository.entity.*;
import com.cloud.base.user.vo.MenuVo;
import com.cloud.base.user.service.SysUserService;
import com.cloud.base.user.param.*;
import com.cloud.base.user.repository.entity.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

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

    @Autowired
    private DeptUserCustomDao deptUserCustomDao;

    @Autowired
    private SysRegionDao sysRegionDao;

    @Autowired
    private IdWorker idWorker;



    /**
     * 获取部门角色信息
     *
     * @param param
     * @return
     * @throws Exception
     */
    @Override
    public PageInfo<DeptUserDto> selectDeptUser(SysDeptUserQueryParam param, SysUser sysUser) throws Exception {
        ThreadLog.info("开始 获取部门角色信息：param=" + JSON.toJSONString(param));
        try {
            PageHelper.startPage(param.getPageNum(), param.getPageSize());
            List<DeptUserDto> deptUserDtos = deptUserCustomDao.selectDeptUser(param);
            PageInfo<DeptUserDto> pageInfo = new PageInfo(deptUserDtos);
            PageHelper.clearPage();
            ThreadLog.info("完成 开始 获取部门角色信息");
            // 输出日志
            ThreadLog.output();
            return pageInfo;
        } catch (Exception e) {
            throw CommonException.create(e,ServerResponse.createByError("获取部门角色信息失败,请联系管理员"));
        }
    }

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
            if (roleIds.contains(1L)) {
                resIds = sysResDao.selectAll().stream().map(ele -> ele.getId()).collect(Collectors.toList());
            } else {
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


    /**
     * 注册用户
     *
     * @throws Exception
     */
    @Override
    @Transactional
    public void registerUser(SysUserRegisterParam param) throws Exception {
        log.info("开始 用户注册:{}", JSON.toJSONString(param));
        if (!param.getPassword().equals(param.getRepassword())) {
            throw CommonException.create(ServerResponse.createByError("两次输入密码不一致"));
        }
        try {
            SysUser sysUser = new SysUser();
            sysUser.setId(idWorker.nextId());
            // 属性对拷
            BeanUtils.copyProperties(param, sysUser);
            sysUser.setSalt(RandomStringUtils.random(4));
            sysUser.setPassword(Md5Util.getMD5Str(param.getPassword(), sysUser.getSalt()));
            sysUser.setActiveFlag(true);
            sysUserDao.insertSelective(sysUser);
            log.info("完成 用户注册");
        } catch (Exception e) {
            throw CommonException.create(e, ServerResponse.createByError("用户注册失败,请联系管理员"));
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
            SysUser updateUser = new SysUser();
            BeanUtils.copyProperties(param, updateUser);
            sysUserDao.updateByPrimaryKeySelective(updateUser);
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
    public PageInfo<SysUser> queryUser(SysUserQueryParam param, SysUser sysUser) throws Exception {
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
                criteria.andLike("username","%"+param.getUsername()+"%");
            }

            if (StringUtils.isNotBlank(param.getNickName())){
                criteria.andLike("nickName","%"+param.getNickName()+"%");
            }
            if (StringUtils.isNotBlank(param.getPhone())){
                criteria.andLike("phone","%"+param.getPhone()+"%");
            }
            if (param.getActiveFlag()!=null){
                criteria.andEqualTo("activeFlag",param.getActiveFlag());
            }
            if (param.getLastLoginLow()!=null){
                criteria.andGreaterThanOrEqualTo("lastLogin",param.getLastLoginLow());
            }
            if (param.getLastLoginUp()!=null){
                criteria.andLessThanOrEqualTo("lastLogin",param.getLastLoginUp());
            }

            if (param.getCreateTimeLow()!=null){
                criteria.andGreaterThanOrEqualTo("createTime",param.getCreateTimeLow());
            }
            if (param.getCreateTimeUp()!=null){
                criteria.andLessThanOrEqualTo("createTime",param.getCreateTimeUp());
            }

            PageHelper.startPage(param.getPageNum(), param.getPageSize());
            List<SysUser> sysUsers = sysUserDao.selectByExample(example);
            PageInfo pageInfo = new PageInfo(sysUsers);
            PageHelper.clearPage();
            log.info("完成 查询用户");
            return pageInfo;
        } catch (Exception e) {
            throw CommonException.create(e, ServerResponse.createByError("查询用户失败,请联系管理员"));
        }
    }

    /**
     * 设置用户角色列表
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setUserRoleList(SysUserRoleSetParam param, SysUser sysUser) throws Exception {
        log.info("开始 设置用户角色列表");
        SysUser currentUser = sysUserDao.selectByPrimaryKey(param.getSysUserId());
        if (currentUser == null || currentUser.getDelFlag()) {
            throw CommonException.create(ServerResponse.createByError("用户信息不存在"));
        }

        try {
            // 删除原关联
            SysUserRole delParam = new SysUserRole();
            delParam.setUserId(param.getSysUserId());
            sysUserRoleDao.delete(delParam);
        } catch (Exception e) {
            throw CommonException.create(ServerResponse.createByError("设置用户角色列表失败，请联系管理员"));
        }

        // 获取到要添加的角色列表
        List<SysRole> roles = Lists.newArrayList();
        roles = sysRoleDao.selectByIdList(param.getRoleIds());
        List<SysRole> postMgrRole = roles.stream().filter(ele -> ele.getId().equals(Long.valueOf(2)) || ele.getId().equals(Long.valueOf(4)) || ele.getId().equals(Long.valueOf(5))).collect(Collectors.toList());
        List<SysRole> girdMgrRole = roles.stream().filter(ele -> ele.getId().equals(Long.valueOf(3))).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(param.getRoleIds())) {
            if (girdMgrRole.size() > 0) {
                if (StringUtils.isEmpty(param.getRegionCode())) {
                    throw CommonException.create(ServerResponse.createByError("未上传区域信息"));
                }
                SysRegion region = sysRegionDao.selectByPrimaryKey(param.getRegionCode());
                if (region == null) {
                    throw CommonException.create(ServerResponse.createByError("地区信息不存在"));
                }
            }
        }


        // 增加新的关联关系
        if (!CollectionUtils.isEmpty(roles)) {
            try {
                List<SysUserRole> sysUserRoleList = roles.stream().map(ele -> {
                    SysUserRole sysUserRole = new SysUserRole();
                    sysUserRole.setId(idWorker.nextId());
                    sysUserRole.setUserId(param.getSysUserId());
                    sysUserRole.setRoleId(ele.getId());
                    return sysUserRole;
                }).collect(Collectors.toList());
                sysUserRoleDao.insertList(sysUserRoleList);
                log.info("完成 设置用户角色列表");
            } catch (Exception e) {
                throw CommonException.create(e, ServerResponse.createByError("设置用户角色信息失败,请联系管理员"));
            }
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

// //////////////// 资源管理

    /**
     * 创建资源
     *
     * @param param   参数
     * @param sysUser 系统用户
     * @throws Exception 异常
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createRes(SysResCreateParam param, SysUser sysUser) throws Exception {
        log.info("进入 创建资源:{}", JSON.toJSONString(param));
        SysRes queryByName = new SysRes();
        queryByName.setName(param.getName());
        if (sysResDao.selectCount(queryByName) > 0) {
            throw CommonException.create(ServerResponse.createByError("权限名称已经存在"));
        }

        try {
            SysRes sysRes = new SysRes();
            sysRes.setId(idWorker.nextId());
            BeanUtils.copyProperties(param, sysRes);
            sysRes.setCreateBy(sysUser.getId());
            sysRes.setCreateTime(new Date());

            SysRes parent = sysResDao.selectByPrimaryKey(sysRes.getParentId());
            if (parent != null) {
                sysRes.setRouter(StringUtils.join(Lists.newArrayList(parent.getRouter(), String.valueOf(parent.getId())), ","));
            } else {
                sysRes.setRouter("0");
            }
            sysResDao.insertSelective(sysRes);
            log.info("完成 创建资源:{}", JSON.toJSONString(param));
        } catch (Exception e) {
            throw CommonException.create(e, ServerResponse.createByError("创建权限失败"));
        }

    }


    /**
     * 删除资源信息
     *
     * @param resId
     * @param sysUser
     * @throws Exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRes(Long resId, SysUser sysUser) throws Exception {
        log.info("进入 删除资源信息:{}", resId);
        SysRes sysRes = sysResDao.selectByPrimaryKey(resId);
        if (sysRes == null) {
            throw CommonException.create(ServerResponse.createByError("权限信息不存在"));
        }
        SysRoleRes selectByResId = new SysRoleRes();
        selectByResId.setResId(resId);
        if (sysRoleResDao.selectCount(selectByResId) > 0) {
            throw CommonException.create(ServerResponse.createByError("当前权限已关联角色"));
        }
        try {
            sysResDao.deleteByPrimaryKey(resId);
            log.info("完成 删除资源信息:{}", resId);
        } catch (Exception e) {
            throw CommonException.create(ServerResponse.createByError("删除权限失败"));
        }
    }


    /**
     * 获取全部资源树
     *
     * @throws Exception 异常
     */
    @Override
    public List<SysRes> getAllResTree() throws Exception {
        log.info("进入 获取全部资源树");
        try {
            // 所有的资源列表
            List<SysRes> sysResList = sysResDao.selectAll();
            for (SysRes sysRes : sysResList) {
                sysRes.setParent(sysResDao.selectByPrimaryKey(sysRes.getParentId()));
                sysRes.setTitle(sysRes.getName() + "[" + SysRes.Type.getDescByCode(sysRes.getType()) + "]");
                sysRes.setKey(String.valueOf(sysRes.getId()));
                sysRes.setPkey(String.valueOf(sysRes.getParentId()));
            }
            // 组装未tree数据
            JSONArray jsonArray = CommonMethod.listToTree(sysResList, "0", "parentId", "id", "children");
            log.info("完成 获取全部资源树");
            return jsonArray.toJavaList(SysRes.class);
        } catch (Exception e) {
            throw CommonException.create(e, ServerResponse.createByError("获取资源树失败"));
        }
    }


// //////////////// 角色管理

    /**
     * 创建系统角色信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createRole(SysRoleCreateParam param, SysUser sysUser) throws Exception {
        log.info("进入 SysAdminServiceImpl.createRole:{}", JSON.toJSONString(param));
        SysRole queryByName = new SysRole();
        queryByName.setName(param.getName());
        if (sysRoleDao.selectCount(queryByName) > 0) {
            throw CommonException.create(ServerResponse.createByError("角色名称已经存在"));
        }

        try {
            SysRole sysRole = new SysRole();
            BeanUtils.copyProperties(param, sysRole);
            sysRole.setId(idWorker.nextId());
            sysRole.setCreateTime(new Date());
            sysRole.setCreateBy(sysUser.getId());
            sysRoleDao.insertSelective(sysRole);
            log.info("完成创建");
        } catch (Exception e) {
            log.info("完成 SysAdminServiceImpl.createRole:{}", JSON.toJSONString(param));
            throw CommonException.create(e, ServerResponse.createByError("创建用户失败"));
        }
    }


    /**
     * 修改系统角色信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRole(SysRoleUpdateParam param, SysUser sysUser) throws Exception {
        log.info("进入 SysAdminServiceImpl.updateRole:{}", JSON.toJSONString(param));
        SysRole sysRole = sysRoleDao.selectByPrimaryKey(param.getId());
        if (sysRole == null) {
            log.info("角色信息不存在");
            throw CommonException.create(ServerResponse.createByError("角色信息不存在"));
        }

        try {
            BeanUtils.copyProperties(param, sysRole);
            sysRole.setUpdateBy(sysUser.getId());
            sysRole.setUpdateTime(new Date());
            sysRoleDao.updateByPrimaryKeySelective(sysRole);
            log.info("完成 SysAdminServiceImpl.updateRole:{}", JSON.toJSONString(param));
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
    public void deleteRole(Long roleId, SysUser sysUser) throws Exception {
        log.info("进入 SysAdminServiceImpl.deleteRole:{}", roleId);
        SysRole sysRole = sysRoleDao.selectByPrimaryKey(roleId);
        if (sysRole == null) {
            log.info("角色信息不存在");
            throw CommonException.create(ServerResponse.createByError("角色信息不存在"));
        }


        SysUserRole queryUserByRoleId = new SysUserRole();
        queryUserByRoleId.setRoleId(roleId);
        if (sysUserRoleDao.selectCount(queryUserByRoleId) > 0) {
            log.info("角色有关联用户不可删除");
            throw CommonException.create(ServerResponse.createByError("角色有关联用户不可删除"));
        }
        try {
            sysRoleDao.deleteByPrimaryKey(roleId);
            SysRoleRes deleteByRoleId = new SysRoleRes();
            deleteByRoleId.setRoleId(roleId);
            sysRoleResDao.delete(deleteByRoleId);
            log.info("完成 SysAdminServiceImpl.deleteRole:{}", roleId);
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
    public PageInfo<SysRole> queryRole(SysRoleQueryParam param, SysUser sysUser) throws Exception {
        log.info("进入 SysAdminServiceImpl.queryRole:{}", JSON.toJSONString(param));
        try {
            Example example = new Example(SysRole.class);
            example.setOrderByClause(" create_time desc,update_time desc");
            Example.Criteria criteria = example.createCriteria();

            if (StringUtils.isNotEmpty(param.getName())) {
                criteria.andLike("name", "%" + param.getName() + "%");
            }
            if (StringUtils.isNotEmpty(param.getNo())) {
                criteria.andEqualTo("no", param.getNo());
            }
            if (param.getStatus() != null) {
                criteria.andEqualTo("activeFlag", param.getStatus());
            }
            PageHelper.startPage(param.getPageNum(), param.getPageSize());
            List<SysRole> sysRoles = sysRoleDao.selectByExample(example);
            PageInfo<SysRole> pageInfo = new PageInfo<>(sysRoles);
            PageHelper.clearPage();
            log.info("完成 SysAdminServiceImpl.queryRole:{}", JSON.toJSONString(param));
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
    public List<SysRole> getRoleList() throws Exception {
        log.info("开始 获取角色列表");
        try {
            List<SysRole> roles = sysRoleDao.selectAll();
            log.info("完成 获取角色列表");
            return roles;
        } catch (Exception e) {
            throw CommonException.create(e, ServerResponse.createByError("获取角色列表失败,请联系管理员"));
        }


    }

    /**
     * 保存角色权限
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRoleRes(SysRoleResSaveParam param, SysUser sysUser) throws Exception {
        log.info("进入 SysAdminServiceImpl.saveRoleRes:{}", param);
        // 检查角色是否存在
        SysRole sysRole = sysRoleDao.selectByPrimaryKey(param.getRoleId());
        if (sysRole == null) {
            throw CommonException.create(ServerResponse.createByError("角色不存在"));
        }
        try {
            // 删除之前的 权限列表
            SysRoleRes deleteByRoleId = new SysRoleRes();
            deleteByRoleId.setRoleId(param.getRoleId());
            sysRoleResDao.delete(deleteByRoleId);
        } catch (Exception e) {
            log.info("保存角色权限失败");
            throw CommonException.create(e, ServerResponse.createByError("保存角色权限失败"));
        }
        // 获取到这次要保存的权限
        param.getResIds().add(0L);
        Example example = new Example(SysRes.class);
        example.createCriteria().andIn("id", param.getResIds());
        List<SysRes> sysPermissions = sysResDao.selectByExample(example);
        if (!org.apache.commons.collections4.CollectionUtils.isEmpty(sysPermissions)) {
            // 获取到有效的id 组装成对应的bean
            List<SysRoleRes> sysRoleResList = sysPermissions.stream().map(ele -> new SysRoleRes(idWorker.nextId(), param.getRoleId(), ele.getId())).collect(Collectors.toList());
            try {
                // 保存新的权限列表
                sysRoleResDao.insertList(sysRoleResList);
            } catch (Exception e) {
                log.info("保存角色权限失败");
                throw CommonException.create(e, ServerResponse.createByError("保存角色权限失败"));
            }
        }

    }

    /**
     * 查询角色资源列表
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<SysRes> getSysResListByRoleId(Long roleId, SysUser sysUser) throws Exception {
        log.info("进入 查询角色资源列表:{}", roleId);
        // 检查角色是否存在
        SysRole sysRole = sysRoleDao.selectByPrimaryKey(roleId);
        if (sysRole == null) {
            throw CommonException.create(ServerResponse.createByError("角色不存在"));
        }
        try {
            SysRoleRes selectParam = new SysRoleRes();
            selectParam.setRoleId(roleId);
            List<SysRoleRes> roleResList = sysRoleResDao.select(selectParam);
            if (org.apache.commons.collections4.CollectionUtils.isEmpty(roleResList)) {
                return Lists.newArrayList();
            }
            List<SysRes> sysRes = sysResDao.selectByIdList(roleResList.stream().map(ele -> ele.getResId()).collect(Collectors.toList()));
            log.info("完成 查询角色资源列表");
            return sysRes;
        } catch (Exception e) {
            throw CommonException.create(e, ServerResponse.createByError("获取角色资源列表失败,请联系管理员"));
        }
    }

}
