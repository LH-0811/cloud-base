package com.cloud.base.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.cloud.base.core.common.entity.CommonMethod;
import com.cloud.base.core.common.exception.CommonException;
import com.cloud.base.core.common.response.ServerResponse;
import com.cloud.base.core.common.util.IdWorker;
import com.cloud.base.user.param.SysDeptCreateParam;
import com.cloud.base.user.repository.dao.SysDeptDao;
import com.cloud.base.user.repository.dao.SysUserDeptRelDao;
import com.cloud.base.user.repository.entity.SysDept;
import com.cloud.base.user.repository.entity.SysUser;
import com.cloud.base.user.repository.entity.SysUserDeptRel;
import com.cloud.base.user.service.SysDeptService;
import com.cloud.base.user.vo.SysDeptVo;
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

/**
 * 系统部门服务接口 实现
 *
 * @author lh0811
 * @date 2021/8/10
 */
@Slf4j
@Service("sysDeptService")
public class SysDeptServiceImpl implements SysDeptService {

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private SysDeptDao sysDeptDao;

    @Autowired
    private SysUserDeptRelDao sysUserDeptRelDao;


    /**
     * 创建部门 信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createSysDept(SysDeptCreateParam param, SysUser sysUser) throws Exception {
        log.info("开始 创建部门信息: param=" + JSON.toJSONString(param));
        // 检查父级部门
        SysDept parentDept = sysDeptDao.selectByPrimaryKey(param.getParentId());
        if (parentDept == null && !param.getParentId().equals(0L)) {
            log.info("退出 父级部门不存在");
            throw CommonException.create(ServerResponse.createByError("父级部门不存在！"));
        }
        // 创建部门信息
        try {
            SysDept sysDept = new SysDept();
            // 属性copy
            BeanUtils.copyProperties(param, sysDept);
            // 设置基本信息
            sysDept.setId(idWorker.nextId());
            sysDept.setCreateBy(sysUser.getId());
            sysDept.setCreateTime(new Date());


            SysDept parent = sysDeptDao.selectByPrimaryKey(sysDept.getParentId());
            if (parent != null) {
                sysDept.setRouter(StringUtils.join(Lists.newArrayList(parent.getRouter(), String.valueOf(sysDept.getId())), ","));
                parent.setIsLeaf(Boolean.FALSE);
                sysDeptDao.updateByPrimaryKeySelective(parent);
            } else {
                sysDept.setRouter("0,"+sysDept.getId());
            }

            sysDeptDao.insertSelective(sysDept);
            log.info("完成 创建部门信息");
        } catch (Exception e) {
            throw CommonException.create(e, ServerResponse.createByError("创建部门信息失败,请联系管理员！"));
        }

    }


    /**
     * 获取部门树
     */
    @Override
    public List<SysDeptVo> queryDeptTree(String deptName, SysUser sysUser) throws Exception {
        log.info("开始 获取部门树");
        try {
            Example example = new Example(SysDept.class);
            Example.Criteria criteria = example.createCriteria();
            if (StringUtils.isNotBlank(deptName)) {
                criteria.andLike("name", "%" + deptName + "%");
            }
            // 所有的资源列表
            List<SysDept> sysDeptList = sysDeptDao.selectByExample(example);

            List<SysDeptVo> sysDeptVos = JSONArray.parseArray(JSON.toJSONString(sysDeptList), SysDeptVo.class);
            for (SysDeptVo sysDept : sysDeptVos) {
                sysDept.setTitle(sysDept.getName());
                sysDept.setKey(String.valueOf(sysDept.getId()));
                sysDept.setPkey(String.valueOf(sysDept.getParentId()));
            }
            // 组装未tree数据
            JSONArray jsonArray = CommonMethod.listToTree(sysDeptVos, "0", "parentId", "id", "children");
            log.info("完成 获取部门树");
            return jsonArray.toJavaList(SysDeptVo.class);
        } catch (Exception e) {
            throw CommonException.create(e, ServerResponse.createByError("获取资源树失败"));
        }
    }


    /**
     * 删除部门信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteSysDept(Long deptId, SysUser sysUser) throws Exception {
        log.info("开始 删除部门信息: deptId=" + deptId);

        // 检查部门是否存在
        SysDept sysDept = sysDeptDao.selectByPrimaryKey(deptId);
        if (sysDept == null) {
            log.info("退出 删除部门信息 部门信息不存在");
            throw CommonException.create(ServerResponse.createByError("部门信息不存在"));
        }

        // 检查子部门
        SysDept queryChild = new SysDept();
        queryChild.setParentId(deptId);
        List<SysDept> childrenList = sysDeptDao.select(queryChild);
        if (CollectionUtils.isNotEmpty(childrenList)) {
            throw CommonException.create(ServerResponse.createByError("部门下有所属子部门不能删除"));
        }

        // 检测部门用户
        SysUserDeptRel queryRel = new SysUserDeptRel();
        queryRel.setDeptId(deptId);
        List<SysUserDeptRel> userDeptRelationList = sysUserDeptRelDao.select(queryRel);
        if (CollectionUtils.isNotEmpty(userDeptRelationList)) {
            throw CommonException.create(ServerResponse.createByError("部门下有用户不能删除"));
        }

        try {
            sysDeptDao.deleteByPrimaryKey(deptId);

            // 更新父节点的是否叶子节点状态
            SysDept pChildrenParam = new SysDept();
            pChildrenParam.setParentId(sysDept.getParentId());
            if (sysDeptDao.selectCount(pChildrenParam) == 0){
                SysDept pUpdateParam = new SysDept();
                pUpdateParam.setId(sysDept.getParentId());
                pUpdateParam.setIsLeaf(Boolean.TRUE);
                sysDeptDao.updateByPrimaryKeySelective(pUpdateParam);
            }
            log.info("完成 删除部门信息");
        } catch (Exception e) {
            throw CommonException.create(e, ServerResponse.createByError("删除部门信息失败,请联系管理员！"));
        }
    }




    /**
     * 获取部门级联选项列表
     */
    @Override
    public List<SysDeptVo> queryDeptCascader(String deptName, SysUser sysUser) throws Exception {
        log.info("开始 获取部门级联列表");
        try {
            Example example = new Example(SysDept.class);
            Example.Criteria criteria = example.createCriteria();
            if (StringUtils.isNotBlank(deptName)) {
                criteria.andLike("name", "%" + deptName + "%");
            }
            // 所有的资源列表
            List<SysDept> sysDeptList = sysDeptDao.selectByExample(example);
            List<SysDeptVo> sysDeptVos = JSONArray.parseArray(JSON.toJSONString(sysDeptList), SysDeptVo.class);
            for (SysDeptVo sysDept : sysDeptVos) {
                sysDept.setLabel(sysDept.getName());
                sysDept.setValue(String.valueOf(sysDept.getId()));
            }
            // 组装未tree数据
            JSONArray jsonArray = CommonMethod.listToTree(sysDeptVos, "0", "parentId", "id", "children");
            log.info("完成 获取部门级联列表");
            return jsonArray.toJavaList(SysDeptVo.class);
        } catch (Exception e) {
            throw CommonException.create(e, ServerResponse.createByError("获取资源级联列表失败"));
        }
    }


}
