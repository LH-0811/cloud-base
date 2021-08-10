package com.cloud.base.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.cloud.base.core.common.exception.CommonException;
import com.cloud.base.core.common.response.ServerResponse;
import com.cloud.base.core.common.util.IdWorker;
import com.cloud.base.core.common.util.thread_log.ThreadLog;
import com.cloud.base.user.param.SysDeptCreateParam;
import com.cloud.base.user.repository.dao.SysDeptDao;
import com.cloud.base.user.repository.entity.SysDept;
import com.cloud.base.user.repository.entity.SysUser;
import com.cloud.base.user.service.SysDeptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

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


    @Override
    /**
     * 创建部门 信息
     */
    @Transactional(rollbackFor = Exception.class)
    public void createSysDept(SysDeptCreateParam param, SysUser sysUser) throws Exception {
        ThreadLog.info("开始 创建部门信息: param=" + JSON.toJSONString(param));

        // 检查父级部门
        SysDept queryParam = new SysDept();
        queryParam.setParentId(param.getParentId());
        SysDept parentDept = sysDeptDao.selectOne(queryParam);
        if (parentDept == null) {
            ThreadLog.info("退出 父级部门不存在");
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
            sysDeptDao.insertSelective(sysDept);
            ThreadLog.info("完成 创建部门信息");
        } catch (Exception e) {
            throw CommonException.create(e, ServerResponse.createByError("创建部门信息失败,请联系管理员！"));
        }

    }



    /**
     * 删除部门信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteSysDept(Long deptId, SysUser sysUser) throws Exception {
        ThreadLog.info("开始 删除部门信息: deptId=" + deptId);
        // 检查部门是否存在
        SysDept sysDept = sysDeptDao.selectByPrimaryKey(deptId);
        if (sysDept == null) {
            ThreadLog.info("退出 删除部门信息 部门信息不存在");
            throw CommonException.create(ServerResponse.createByError("部门信息不存在"));
        }
        try {
            sysDeptDao.deleteByPrimaryKey(deptId);
            ThreadLog.info("完成 删除部门信息");
        } catch (Exception e) {
            throw CommonException.create(e, ServerResponse.createByError("删除部门信息失败,请联系管理员！"));
        }
    }


}
