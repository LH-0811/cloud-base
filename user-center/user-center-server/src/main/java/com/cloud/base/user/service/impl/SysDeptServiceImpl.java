package com.cloud.base.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.cloud.base.core.common.entity.CommonMethod;
import com.cloud.base.core.common.exception.CommonException;
import com.cloud.base.core.common.response.ServerResponse;
import com.cloud.base.core.common.util.IdWorker;
import com.cloud.base.user.dto.DeptUserDto;
import com.cloud.base.user.param.SysDeptCreateParam;
import com.cloud.base.user.param.SysDeptUserQueryParam;
import com.cloud.base.user.repository.dao.SysDeptDao;
import com.cloud.base.user.repository.dao.SysUserDeptRelDao;
import com.cloud.base.user.repository.dao.custom.DeptUserCustomDao;
import com.cloud.base.user.repository.entity.SysDept;
import com.cloud.base.user.repository.entity.SysUser;
import com.cloud.base.user.repository.entity.SysUserDeptRel;
import com.cloud.base.user.service.SysDeptService;
import com.cloud.base.user.vo.SysDeptVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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
    private DeptUserCustomDao deptUserCustomDao;

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
        SysDept queryParam = new SysDept();
        queryParam.setParentId(param.getParentId());
        SysDept parentDept = sysDeptDao.selectOne(queryParam);
        if (parentDept == null) {
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
    public List<SysDept> queryDeptTree(String deptName, SysUser sysUser) throws Exception {
        log.info("开始 获取部门树");
        try {
            Example example = new Example(SysDept.class);
            Example.Criteria criteria = example.createCriteria();
            if (StringUtils.isNotBlank(deptName)) {
                criteria.andLike("name", "%" + deptName + "%");
            }
            // 所有的资源列表
            List<SysDept> sysDeptList = sysDeptDao.selectByExample(example);
            for (SysDept sysDept : sysDeptList) {
                sysDept.setParent(sysDeptDao.selectByPrimaryKey(sysDept.getParentId()));
                sysDept.setTitle(sysDept.getName());
                sysDept.setKey(String.valueOf(sysDept.getId()));
                sysDept.setPkey(String.valueOf(sysDept.getParentId()));
                sysDept.setIsLeaf(checkIsLeaf(sysDept.getId()));
            }
            // 组装未tree数据
            JSONArray jsonArray = CommonMethod.listToTree(sysDeptList, "0", "parentId", "id", "children");
            log.info("完成 获取部门树");
            return jsonArray.toJavaList(SysDept.class);
        } catch (Exception e) {
            throw CommonException.create(e, ServerResponse.createByError("获取资源树失败"));
        }
    }

    private Boolean checkIsLeaf(Long deptId) {
        SysDept queryParam = new SysDept();
        queryParam.setParentId(deptId);
        return sysDeptDao.selectCount(queryParam) == 0;
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
            log.info("完成 删除部门信息");
        } catch (Exception e) {
            throw CommonException.create(e, ServerResponse.createByError("删除部门信息失败,请联系管理员！"));
        }
    }


    /**
     * 获取部门用户信息
     */
    @Override
    public PageInfo<DeptUserDto> selectDeptUser(SysDeptUserQueryParam param, SysUser sysUser) throws Exception {
        log.info("开始 获取部门角色信息：param=" + JSON.toJSONString(param));
        try {
            PageHelper.startPage(param.getPageNum(), param.getPageSize());
            List<DeptUserDto> deptUserDtos = deptUserCustomDao.selectDeptUser(param);
            PageInfo<DeptUserDto> pageInfo = new PageInfo(deptUserDtos);
            PageHelper.clearPage();
            log.info("完成 开始 获取部门角色信息");
            return pageInfo;
        } catch (Exception e) {
            throw CommonException.create(e, ServerResponse.createByError("获取部门角色信息失败,请联系管理员"));
        }
    }



    /**
     * 获取部门级联选项列表
     */
    @Override
    public List<SysDept> queryDeptCascader(String deptName, SysUser sysUser) throws Exception {
        log.info("开始 获取部门级联列表");
        try {
            Example example = new Example(SysDept.class);
            Example.Criteria criteria = example.createCriteria();
            if (StringUtils.isNotBlank(deptName)) {
                criteria.andLike("name", "%" + deptName + "%");
            }
            // 所有的资源列表
            List<SysDept> sysDeptList = sysDeptDao.selectByExample(example);
            for (SysDept sysDept : sysDeptList) {
                sysDept.setParent(sysDeptDao.selectByPrimaryKey(sysDept.getParentId()));
                sysDept.setLabel(sysDept.getName());
                sysDept.setValue(String.valueOf(sysDept.getId()));
                sysDept.setIsLeaf(checkIsLeaf(sysDept.getId()));
            }
            // 组装未tree数据
            JSONArray jsonArray = CommonMethod.listToTree(sysDeptList, "0", "parentId", "id", "children");
            log.info("完成 获取部门级联列表");
            return jsonArray.toJavaList(SysDept.class);
        } catch (Exception e) {
            throw CommonException.create(e, ServerResponse.createByError("获取资源级联列表失败"));
        }
    }


}
