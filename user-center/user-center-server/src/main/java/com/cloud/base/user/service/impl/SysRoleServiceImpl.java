package com.cloud.base.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.cloud.base.core.common.entity.CommonMethod;
import com.cloud.base.core.common.exception.CommonException;
import com.cloud.base.core.common.response.ServerResponse;
import com.cloud.base.core.common.util.IdWorker;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

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


    @Autowired
    private SysRoleDao sysRoleDao;

    @Autowired
    private SysRoleResRelDao sysRoleResRelDao;

    @Autowired
    private SysUserRoleRelDao sysUserRoleRelDao;

    @Autowired
    private SysResDao sysResDao;

    @Autowired
    private IdWorker idWorker;

// //////////////// 角色管理

    /**
     * 创建系统角色信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createRole(SysRoleCreateParam param, SysUser sysUser) throws Exception {
        log.info("进入 SysAdminServiceImpl.createRole:" + JSON.toJSONString(param));
        SysRole queryByName = new SysRole();
        queryByName.setName(param.getName());
        if (sysRoleDao.selectCount(queryByName) > 0) {
            throw CommonException.create(ServerResponse.createByError("角色名称已经存在"));
        }

        try {
            // 创建角色信息
            SysRole sysRole = new SysRole();
            BeanUtils.copyProperties(param, sysRole);
            sysRole.setId(idWorker.nextId());
            sysRole.setCreateTime(new Date());
            sysRole.setCreateBy(sysUser.getId());
            sysRoleDao.insertSelective(sysRole);

            // 添加角色与资源之间的关系
            if (CollectionUtils.isNotEmpty(param.getResIdList())) {
                List<SysRoleResRel> sysRoleResRelList = param.getResIdList().stream().map(resId -> new SysRoleResRel(idWorker.nextId(), sysRole.getId(), resId)).collect(Collectors.toList());
                sysRoleResRelDao.insertList(sysRoleResRelList);
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
    public void updateRole(SysRoleUpdateParam param, SysUser sysUser) throws Exception {
        log.info("进入 SysAdminServiceImpl.updateRole:" + JSON.toJSONString(param));
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

            // 添加角色与资源之间的关系
            if (CollectionUtils.isNotEmpty(param.getResIdList())) {
                SysRoleResRel delParam = new SysRoleResRel();
                delParam.setRoleId(param.getId());
                sysRoleResRelDao.delete(delParam);

                List<SysRoleResRel> sysRoleResRelList = param.getResIdList().stream().map(resId -> new SysRoleResRel(idWorker.nextId(), sysRole.getId(), resId)).collect(Collectors.toList());
                sysRoleResRelDao.insertList(sysRoleResRelList);
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
    public void deleteRole(Long roleId, SysUser sysUser) throws Exception {
        log.info("进入 SysAdminServiceImpl.deleteRole:" + roleId);
        SysRole sysRole = sysRoleDao.selectByPrimaryKey(roleId);
        if (sysRole == null) {
            log.info("角色信息不存在");
            throw CommonException.create(ServerResponse.createByError("角色信息不存在"));
        }


        SysUserRoleRel queryUserByRoleId = new SysUserRoleRel();
        queryUserByRoleId.setRoleId(roleId);
        if (sysUserRoleRelDao.selectCount(queryUserByRoleId) > 0) {
            log.info("角色有关联用户不可删除");
            throw CommonException.create(ServerResponse.createByError("角色有关联用户不可删除"));
        }
        try {
            sysRoleDao.deleteByPrimaryKey(roleId);
            SysRoleResRel deleteByRoleId = new SysRoleResRel();
            deleteByRoleId.setRoleId(roleId);
            sysRoleResRelDao.delete(deleteByRoleId);
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
    public PageInfo<SysRoleVo> queryRole(SysRoleQueryParam param, SysUser sysUser) throws Exception {
        log.info("进入 SysAdminServiceImpl.queryRole:" + JSON.toJSONString(param));
        try {
            Example example = new Example(SysRole.class);
            example.setOrderByClause(" sort_num asc,create_time desc ");
            Example.Criteria criteria = example.createCriteria();

            if (StringUtils.isNotEmpty(param.getName())) {
                criteria.andLike("name", "%" + param.getName() + "%");
            }
            if (StringUtils.isNotEmpty(param.getNo())) {
                criteria.andEqualTo("no", param.getNo());
            }
            if (param.getActiveFlag() != null) {
                criteria.andEqualTo("activeFlag", param.getActiveFlag());
            }
            PageHelper.startPage(param.getPageNum(), param.getPageSize());
            List<SysRole> sysRoles = sysRoleDao.selectByExample(example);
            PageInfo pageInfo = new PageInfo<>(sysRoles);
            PageHelper.clearPage();

            // 获取到全部的资源列表
            // 所有的资源列表
            List<SysRes> sysResAllList = sysResDao.selectAll();


            // 增加vo信息
            List<SysRole> roleList = pageInfo.getList();
            List<SysRoleVo> roleVoList = roleList.stream().map(role -> {
                SysRoleVo sysRoleVo = new SysRoleVo();
                BeanUtils.copyProperties(role, sysRoleVo);
                // 设置角色资源列表
                SysRoleResRel queryParam = new SysRoleResRel();
                queryParam.setRoleId(role.getId());
                List<SysRoleResRel> sysRoleResRels = sysRoleResRelDao.select(queryParam);
                if (CollectionUtils.isNotEmpty(sysRoleResRels)) {
                    List<Long> resIds = sysRoleResRels.stream().map(sysRoleResRel -> sysRoleResRel.getResId()).collect(Collectors.toList());
                    List<SysRes> sysResList = sysResDao.selectByIdList(resIds);
                    List<SysResVo> sysResVoList = sysResList.stream().map(sysRes -> {
                        SysResVo sysResVo = new SysResVo();
                        BeanUtils.copyProperties(sysRes, sysResVo);
                        return sysResVo;
                    }).collect(Collectors.toList());
                    sysRoleVo.setSysResList(sysResVoList);
                }

                // 设置角色资源树
                if (CollectionUtils.isNotEmpty(sysRoleResRels)) {
                    List<Long> resOrRoleIds = sysRoleResRels.stream().map(ele -> ele.getResId()).collect(Collectors.toList());
                    for (SysRes sysRes : sysResAllList) {
                        sysRes.setParent(sysResDao.selectByPrimaryKey(sysRes.getParentId()));
                        sysRes.setTitle(sysRes.getName() + "[" + SysRes.Type.getDescByCode(sysRes.getType()) + "]");
                        sysRes.setKey(String.valueOf(sysRes.getId()));
                        sysRes.setPkey(String.valueOf(sysRes.getParentId()));
                        sysRes.setChecked(resOrRoleIds.contains(sysRes.getId()));
                    }
                    // 组装未tree数据
                    JSONArray jsonArray = CommonMethod.listToTree(sysResAllList, "0", "parentId", "id", "children");
                    sysRoleVo.setSysResTree(JSON.parseArray(JSONArray.toJSONString(jsonArray), SysResVo.class));
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
    public List<SysRole> getRoleList(String roleName) throws Exception {
        log.info("开始 获取角色列表");
        try {
            Example example = new Example(SysRole.class);
            example.setOrderByClause(" sort_num asc,create_time desc ");
            if (StringUtils.isNotBlank(roleName)) {
                example.createCriteria().andEqualTo("name", roleName);
            }
            List<SysRole> roles = sysRoleDao.selectByExample(example);
            log.info("完成 获取角色列表");
            return roles;
        } catch (Exception e) {
            throw CommonException.create(e, ServerResponse.createByError("获取角色列表失败,请联系管理员"));
        }
    }

    /**
     * 查询角色资源列表
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<SysRes> getSysResListByRoleId(Long roleId, SysUser sysUser) throws Exception {
        log.info("进入 查询角色资源列表:" + roleId);
        // 检查角色是否存在
        SysRole sysRole = sysRoleDao.selectByPrimaryKey(roleId);
        if (sysRole == null) {
            throw CommonException.create(ServerResponse.createByError("角色不存在"));
        }
        try {
            SysRoleResRel selectParam = new SysRoleResRel();
            selectParam.setRoleId(roleId);
            List<SysRoleResRel> roleResList = sysRoleResRelDao.select(selectParam);
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
