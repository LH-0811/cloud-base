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
     * ????????????????????????
     */
    @Override
    public List<SysRoleVo> getUserRoleList(Long userId) throws Exception {
        log.info("??????  ????????????????????????");
        try {
            QueryWrapper<SysUserRoleRel> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(SysUserRoleRel::getUserId, userId);
            List<SysUserRoleRel> userRoleList = sysUserRoleRelDao.list(queryWrapper);
            List<SysRole> sysRoles = sysRoleDao.listByIds(userRoleList.stream().map(ele -> ele.getRoleId()).collect(Collectors.toList()));
            log.info("??????  ????????????????????????");
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
            throw CommonException.create(e, ServerResponse.createByError("??????????????????????????????,??????????????????"));
        }
    }

    /**
     * ??????????????????
     */
    @Override
    public void updateUserPassword(SysUserUpdatePasswordParam param, SecurityAuthority securityAuthority) throws Exception {
        log.info("?????? ??????????????????");
        SysUser currentUser = sysUserDao.getById(Long.valueOf(securityAuthority.getSecurityUser().getId()));
        if (currentUser == null) {
            throw CommonException.create(ServerResponse.createByError("?????????????????????"));
        }
        if (!param.getRePwd().equals(param.getNewPwd())) {
            throw CommonException.create(ServerResponse.createByError("???????????????????????????"));
        }

        if (!currentUser.getPassword().equals(Md5Util.getMD5Str(param.getOldPwd(), currentUser.getSalt()))) {
            throw CommonException.create(ServerResponse.createByError("??????????????????"));
        }
        try {
            SysUser updateUser = new SysUser();
            updateUser.setId(currentUser.getId());
            updateUser.setPassword(Md5Util.getMD5Str(param.getNewPwd(), currentUser.getSalt()));
            sysUserDao.updateById(updateUser);
            log.info("?????? ??????????????????");
        } catch (Exception e) {
            throw CommonException.create(e, ServerResponse.createByError("????????????????????????,??????????????????"));
        }
    }

    /**
     * ?????????????????????
     */
    @Override
    public List<MenuVo> getMenuTreeByUser(Long userId) throws Exception {
        log.info("?????? ?????????????????????");
        try {
            // ??????????????????
            QueryWrapper<SysUserRoleRel> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda()
                    .eq(SysUserRoleRel::getUserId, userId);
            List<SysUserRoleRel> roleList = sysUserRoleRelDao.list(queryWrapper);
            if (CollectionUtils.isEmpty(roleList)) {
                return Lists.newArrayList();
            }
            // ??????id??????
            List<Long> roleIds = roleList.stream().map(ele -> ele.getRoleId()).collect(Collectors.toList());
            QueryWrapper<SysRoleResRel> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.lambda().in(SysRoleResRel::getRoleId, roleIds);
            List<SysRoleResRel> sysRoleReRels = sysRoleResRelDao.list(queryWrapper1);
            if (CollectionUtils.isEmpty(sysRoleReRels)) {
                return Lists.newArrayList();
            }
            // ??????id??????
            List<Long> resIds = sysRoleReRels.stream().map(ele -> ele.getResId()).collect(Collectors.toList());

            // ?????????????????????
            List<SysRes> sysResList = sysResDao.listByIds(resIds).stream().filter(ele -> ele.getType().equals(UCConstant.Type.GROUP.getCode()) || ele.getType().equals(UCConstant.Type.MENU.getCode())).collect(Collectors.toList());
            // ??????orderNo ????????????
            sysResList = sysResList.stream().sorted(Comparator.comparing(SysRes::getOrderNo)).collect(Collectors.toList());

            List<SysResVo> sysResVoList = JSONArray.parseArray(JSON.toJSONString(sysResList), SysResVo.class);
            for (SysResVo sysRes : sysResVoList) {
                sysRes.setParent(JSON.parseObject(JSON.toJSONString(sysResDao.getById(sysRes.getParentId())), SysResVo.class));
                sysRes.setTitle(sysRes.getName() + "[" + UCConstant.Type.getDescByCode(sysRes.getType()) + "]");
                sysRes.setKey(String.valueOf(sysRes.getId()));
                sysRes.setPkey(String.valueOf(sysRes.getParentId()));
            }

            // ?????????menuVo
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

            // ?????????tree??????
            JSONArray jsonArray = CommonMethod.listToTree(menuVoList, "0", "parentId", "id", "children");
            log.info("?????? ?????????????????????");
            return jsonArray.toJavaList(MenuVo.class);
        } catch (Exception e) {
            throw CommonException.create(e, ServerResponse.createByError("?????????????????????"));
        }
    }

    /**
     * ????????????????????????
     */
    @Override
    public List<SysResVo> getResListByUser(Long userId) throws Exception {
        log.info("??????  ????????????????????????");
        try {
            // ??????????????????
            QueryWrapper<SysUserRoleRel> sysUserRoleRelQuery = new QueryWrapper<>();
            sysUserRoleRelQuery.lambda()
                    .eq(SysUserRoleRel::getUserId, userId);
            List<SysUserRoleRel> roleList = sysUserRoleRelDao.list(sysUserRoleRelQuery);
            if (CollectionUtils.isEmpty(roleList)) {
                return Lists.newArrayList();
            }
            // ??????id??????
            List<Long> roleIds = roleList.stream().map(ele -> ele.getRoleId()).collect(Collectors.toList());

            QueryWrapper<SysRoleResRel> roleResRelQueryWrapper = new QueryWrapper<>();
            roleResRelQueryWrapper.lambda().in(SysRoleResRel::getRoleId, roleIds);
            List<SysRoleResRel> sysRoleReRels = sysRoleResRelDao.list(roleResRelQueryWrapper);
            if (CollectionUtils.isEmpty(sysRoleReRels)) {
                return Lists.newArrayList();
            }
            // ??????id??????
            List<Long> resIds = sysRoleReRels.stream().map(ele -> ele.getResId()).collect(Collectors.toList());

            // ?????????????????????
            List<SysRes> sysRes = sysResDao.listByIds(resIds);

            log.info("?????? ????????????????????????");
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
            throw CommonException.create(e, ServerResponse.createByError("?????????????????????"));
        }
    }

    /**
     * ????????????id ????????????????????????
     */
    @Override
    public SysUserVo getUserByUserId(Long userId) throws Exception {
        log.info("?????? ????????????id????????????????????????");
        try {
            SysUser sysUser = sysUserDao.getById(userId);
            log.info("?????? ????????????id????????????????????????");
            if (sysUser != null) {
                SysUserVo sysUserVo = new SysUserVo();
                BeanUtils.copyProperties(sysUser,sysUserVo);
                return sysUserVo;
            }
            return null;
        } catch (Exception e) {
            throw CommonException.create(e, ServerResponse.createByError("????????????id??????????????????????????????,??????????????????"));
        }
    }

    /**
     * ??????????????? ????????????????????????
     *
     * @param username
     * @param password
     * @return
     * @throws Exception
     */
    @Override
    public SysUserVo getUserByUsernameAndPassword(String username, String password) throws Exception {
        log.info("?????? ??????????????? ????????????????????????");

        SysUser currentUser = null;
        try {
            QueryWrapper<SysUser> sysUserQueryWrapper = new QueryWrapper<>();
//            sysUserQueryWrapper.eq("delFlag",Boolean.FALSE);
//            sysUserQueryWrapper.eq("username",username);
            LambdaQueryWrapper<SysUser> eq = sysUserQueryWrapper.lambda().eq(SysUser::getDelFlag, Boolean.FALSE).eq(SysUser::getUsername, username);
            SysUser sysUser = sysUserMapper.selectById(1L);
            currentUser = sysUserDao.getOne(eq);
        } catch (Exception e) {
            throw CommonException.create(e, ServerResponse.createByError("?????????????????????"));
        }

        if (currentUser == null) {
            throw CommonException.create(ServerResponse.createByError("?????????????????????"));
        }

        // ??????????????????
        String md5Str = Md5Util.getMD5Str(password, currentUser.getSalt());

        if (!currentUser.getPassword().equals(md5Str)) {
            throw CommonException.create(ServerResponse.createByError("????????????"));
        }
        log.info("?????? ??????????????? ????????????????????????");
        if (currentUser != null) {
            SysUserVo sysUserVo = new SysUserVo();
            BeanUtils.copyProperties(currentUser,sysUserVo);
            return sysUserVo;
        }
        return null;
    }

    /**
     * ????????????????????????
     */
    @Override
    public PageInfo<DeptUserDto> selectDeptUser(SysDeptUserQueryParam param, SecurityAuthority securityAuthority) throws Exception {
        log.info("?????? ???????????????????????????param=" + JSON.toJSONString(param));
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

            log.info("?????? ?????? ????????????????????????");
            return pageInfo;
        } catch (Exception e) {
            throw CommonException.create(e, ServerResponse.createByError("??????????????????????????????,??????????????????"));
        }
    }


    /**
     * ????????????????????? ?????????????????? ?????????????????????
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public SecurityAuthority verification(UsernamePasswordVerificationParam param) throws Exception {
        log.info("?????? ????????????????????? ?????????????????? ?????????????????????:{}", JSON.toJSONString(param));
        // ??????????????????
        SysUserVo loginUser = getUserByUsernameAndPassword(param.getUsername(), param.getPassword());
        if (loginUser == null) {
            throw CommonException.create(ServerResponse.createByError("????????????????????????"));
        }
        if (!loginUser.getActiveFlag()) {
            throw CommonException.create(ServerResponse.createByError("?????????????????????????????????"));
        }
        try {
            // ????????????????????????
            List<SysRoleVo> userRoleList = getUserRoleList(loginUser.getId());
            // ????????????????????????
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
            log.info("?????? ????????????????????? ?????????????????? ?????????????????????:{}", JSON.toJSONString(param));
            return securityAuthority;
        } catch (Exception e) {
            throw CommonException.create(e, ServerResponse.createByError("?????????????????????????????????????????????,??????????????????"));
        }

    }

}
