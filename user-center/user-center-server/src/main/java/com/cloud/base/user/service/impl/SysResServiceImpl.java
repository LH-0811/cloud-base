package com.cloud.base.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.cloud.base.core.common.entity.CommonMethod;
import com.cloud.base.core.common.exception.CommonException;
import com.cloud.base.core.common.response.ServerResponse;
import com.cloud.base.core.common.util.IdWorker;
import com.cloud.base.core.common.util.thread_log.ThreadLog;
import com.cloud.base.user.param.SysResCreateParam;
import com.cloud.base.user.repository.dao.*;
import com.cloud.base.user.repository.entity.SysRes;
import com.cloud.base.user.repository.entity.SysRoleResRel;
import com.cloud.base.user.repository.entity.SysUser;
import com.cloud.base.user.service.SysResService;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author lh0811
 * @date 2021/8/17
 */
@Service("sysResService")
public class SysResServiceImpl implements SysResService {

    @Autowired
    private SysRoleResRelDao sysRoleResRelDao;

    @Autowired
    private SysResDao sysResDao;

    @Autowired
    private IdWorker idWorker;


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
        ThreadLog.info("进入 创建资源:" + JSON.toJSONString(param));
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
            ThreadLog.info("完成 创建资源:" + JSON.toJSONString(param));
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
        ThreadLog.info("进入 删除资源信息:" + resId);
        SysRes sysRes = sysResDao.selectByPrimaryKey(resId);
        if (sysRes == null) {
            throw CommonException.create(ServerResponse.createByError("权限信息不存在"));
        }
        SysRoleResRel selectByResId = new SysRoleResRel();
        selectByResId.setResId(resId);
        if (sysRoleResRelDao.selectCount(selectByResId) > 0) {
            throw CommonException.create(ServerResponse.createByError("当前权限已关联角色"));
        }
        try {
            sysResDao.deleteByPrimaryKey(resId);
            ThreadLog.info("完成 删除资源信息:" + resId);
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
        ThreadLog.info("进入 获取全部资源树");
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
            ThreadLog.info("完成 获取全部资源树");
            return jsonArray.toJavaList(SysRes.class);
        } catch (Exception e) {
            throw CommonException.create(e, ServerResponse.createByError("获取资源树失败"));
        }
    }

}
