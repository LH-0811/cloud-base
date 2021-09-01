package com.cloud.base.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.cloud.base.core.common.entity.CommonMethod;
import com.cloud.base.core.common.exception.CommonException;
import com.cloud.base.core.common.response.ServerResponse;
import com.cloud.base.core.common.util.IdWorker;
import com.cloud.base.user.param.SysResCreateParam;
import com.cloud.base.user.repository.dao.SysResDao;
import com.cloud.base.user.repository.dao.SysRoleResRelDao;
import com.cloud.base.user.repository.entity.SysRes;
import com.cloud.base.user.repository.entity.SysRoleResRel;
import com.cloud.base.user.repository.entity.SysUser;
import com.cloud.base.user.service.SysResService;
import com.cloud.base.user.vo.SysResVo;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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
        log.info("进入 创建资源:" + JSON.toJSONString(param));
        SysRes queryByName = new SysRes();
        queryByName.setName(param.getName());
        if (sysResDao.selectCount(queryByName) > 0) {
            throw CommonException.create(ServerResponse.createByError("权限名称已经存在"));
        }

        try {
            SysRes sysRes = new SysRes();
            BeanUtils.copyProperties(param, sysRes);

            sysRes.setId(idWorker.nextId());
            sysRes.setCreateBy(sysUser.getId());
            sysRes.setCreateTime(new Date());

            SysRes parent = sysResDao.selectByPrimaryKey(sysRes.getParentId());
            if (parent != null) {
                sysRes.setRouter(StringUtils.join(Lists.newArrayList(parent.getRouter(), String.valueOf(sysRes.getId())), ","));
                parent.setIsLeaf(Boolean.FALSE);
                sysResDao.updateByPrimaryKeySelective(parent);
            } else {
                sysRes.setRouter("0,"+sysRes.getId());
            }
            sysResDao.insertSelective(sysRes);
            log.info("完成 创建资源:" + JSON.toJSONString(param));
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
        log.info("进入 删除资源信息:" + resId);
        SysRes sysRes = sysResDao.selectByPrimaryKey(resId);
        if (sysRes == null) {
            throw CommonException.create(ServerResponse.createByError("权限信息不存在"));
        }

        SysRes queryChildParam = new SysRes();
        queryChildParam.setParentId(resId);
        if (sysResDao.selectCount(queryChildParam) > 0) {
            throw CommonException.create(ServerResponse.createByError("当前资源有子资源，不能删除"));
        }

        try {
            // 删除资源与角色之间的关系
            SysRoleResRel selectByResId = new SysRoleResRel();
            selectByResId.setResId(resId);
            sysRoleResRelDao.delete(selectByResId);

            // 删除资源信息
            sysResDao.deleteByPrimaryKey(resId);

            // 更新父节点的是否叶子节点状态
            SysRes pChildrenParam = new SysRes();
            pChildrenParam.setParentId(sysRes.getParentId());
            if (sysResDao.selectCount(pChildrenParam) == 0){
                SysRes pUpdateParam = new SysRes();
                pUpdateParam.setId(sysRes.getParentId());
                pUpdateParam.setIsLeaf(Boolean.TRUE);
                sysResDao.updateByPrimaryKeySelective(pUpdateParam);
            }


            log.info("完成 删除资源信息:" + resId);
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
    public List<SysResVo> getAllResTree() throws Exception {
        log.info("进入 获取全部资源树");
        try {
            // 所有的资源列表
            List<SysRes> sysResList = sysResDao.selectAll();
            List<SysResVo> sysResVoList = JSONArray.parseArray(JSON.toJSONString(sysResList),SysResVo.class);
            for (SysResVo sysRes : sysResVoList) {
                sysRes.setTitle(sysRes.getName() + "[" + SysRes.Type.getDescByCode(sysRes.getType()) + "]");
                sysRes.setKey(String.valueOf(sysRes.getId()));
                sysRes.setPkey(String.valueOf(sysRes.getParentId()));
            }
            // 组装未tree数据
            JSONArray jsonArray = CommonMethod.listToTree(sysResVoList, "0", "parentId", "id", "children");
            log.info("完成 获取全部资源树");
            return jsonArray.toJavaList(SysResVo.class);
        } catch (Exception e) {
            throw CommonException.create(e, ServerResponse.createByError("获取资源树失败"));
        }
    }




}
