package com.cloud.base.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.cloud.base.core.common.exception.CommonException;
import com.cloud.base.core.common.response.ServerResponse;
import com.cloud.base.core.common.util.IdWorker;
import com.cloud.base.core.common.util.thread_log.ThreadLog;
import com.cloud.base.user.param.SysRoleCreateParam;
import com.cloud.base.user.param.SysRoleQueryParam;
import com.cloud.base.user.param.SysRoleResSaveParam;
import com.cloud.base.user.param.SysRoleUpdateParam;
import com.cloud.base.user.repository.dao.SysResDao;
import com.cloud.base.user.repository.dao.SysRoleDao;
import com.cloud.base.user.repository.dao.SysRoleResDao;
import com.cloud.base.user.repository.dao.SysUserRoleRelDao;
import com.cloud.base.user.repository.entity.*;
import com.cloud.base.user.service.SysRoleService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
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
@Service("sysRoleService")
public class SysRoleServiceImpl implements SysRoleService {


    @Autowired
    private SysRoleDao sysRoleDao;

    @Autowired
    private SysRoleResDao sysRoleResDao;

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
        ThreadLog.info("进入 SysAdminServiceImpl.createRole:" + JSON.toJSONString(param));
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
            ThreadLog.info("完成创建");
        } catch (Exception e) {
            ThreadLog.info("完成 SysAdminServiceImpl.createRole:" + JSON.toJSONString(param));
            throw CommonException.create(e, ServerResponse.createByError("创建用户失败"));
        }
    }


    /**
     * 修改系统角色信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRole(SysRoleUpdateParam param, SysUser sysUser) throws Exception {
        ThreadLog.info("进入 SysAdminServiceImpl.updateRole:" + JSON.toJSONString(param));
        SysRole sysRole = sysRoleDao.selectByPrimaryKey(param.getId());
        if (sysRole == null) {
            ThreadLog.info("角色信息不存在");
            throw CommonException.create(ServerResponse.createByError("角色信息不存在"));
        }

        try {
            BeanUtils.copyProperties(param, sysRole);
            sysRole.setUpdateBy(sysUser.getId());
            sysRole.setUpdateTime(new Date());
            sysRoleDao.updateByPrimaryKeySelective(sysRole);
            ThreadLog.info("完成 SysAdminServiceImpl.updateRole:" + JSON.toJSONString(param));
        } catch (Exception e) {
            ThreadLog.info("修改角色信息错误");
            throw CommonException.create(e, ServerResponse.createByError("修改角色信息错误"));
        }
    }


    /**
     * 删除系统角色信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRole(Long roleId, SysUser sysUser) throws Exception {
        ThreadLog.info("进入 SysAdminServiceImpl.deleteRole:" + roleId);
        SysRole sysRole = sysRoleDao.selectByPrimaryKey(roleId);
        if (sysRole == null) {
            ThreadLog.info("角色信息不存在");
            throw CommonException.create(ServerResponse.createByError("角色信息不存在"));
        }


        SysUserRoleRel queryUserByRoleId = new SysUserRoleRel();
        queryUserByRoleId.setRoleId(roleId);
        if (sysUserRoleRelDao.selectCount(queryUserByRoleId) > 0) {
            ThreadLog.info("角色有关联用户不可删除");
            throw CommonException.create(ServerResponse.createByError("角色有关联用户不可删除"));
        }
        try {
            sysRoleDao.deleteByPrimaryKey(roleId);
            SysRoleRes deleteByRoleId = new SysRoleRes();
            deleteByRoleId.setRoleId(roleId);
            sysRoleResDao.delete(deleteByRoleId);
            ThreadLog.info("完成 SysAdminServiceImpl.deleteRole:" + roleId);
        } catch (Exception e) {
            ThreadLog.info("删除角色失败");
            throw CommonException.create(e, ServerResponse.createByError("删除角色失败"));
        }
    }

    /**
     * 查询系统角色信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public PageInfo<SysRole> queryRole(SysRoleQueryParam param, SysUser sysUser) throws Exception {
        ThreadLog.info("进入 SysAdminServiceImpl.queryRole:" + JSON.toJSONString(param));
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
            ThreadLog.info("完成 SysAdminServiceImpl.queryRole:" + JSON.toJSONString(param));
            return pageInfo;
        } catch (Exception e) {
            ThreadLog.info("查询角色失败");
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
        ThreadLog.info("开始 获取角色列表");
        ;
        try {
            List<SysRole> roles = sysRoleDao.selectAll();
            ThreadLog.info("完成 获取角色列表");
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
        ThreadLog.info("进入 SysAdminServiceImpl.saveRoleRes:" + param);
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
            ThreadLog.info("保存角色权限失败");
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
                ThreadLog.info("保存角色权限失败");
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
        ThreadLog.info("进入 查询角色资源列表:" + roleId);
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
            ThreadLog.info("完成 查询角色资源列表");
            return sysRes;
        } catch (Exception e) {
            throw CommonException.create(e, ServerResponse.createByError("获取角色资源列表失败,请联系管理员"));
        }
    }

}
