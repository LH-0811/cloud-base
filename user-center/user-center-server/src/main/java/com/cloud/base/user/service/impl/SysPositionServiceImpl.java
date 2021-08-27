package com.cloud.base.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.cloud.base.core.common.exception.CommonException;
import com.cloud.base.core.common.response.ServerResponse;
import com.cloud.base.core.common.util.IdWorker;
import com.cloud.base.user.param.SysPositionCreateParam;
import com.cloud.base.user.param.SysPositionQueryParam;
import com.cloud.base.user.repository.dao.SysPositionDao;
import com.cloud.base.user.repository.entity.SysPosition;
import com.cloud.base.user.repository.entity.SysUser;
import com.cloud.base.user.service.SysPositionService;
import com.cloud.base.user.vo.SysPositionVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
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
 * @author lh0811
 * @date 2021/8/17
 */
@Slf4j
@Service("sysPositionService")
public class SysPositionServiceImpl implements SysPositionService {

    @Autowired
    private SysPositionDao sysPositionDao;

    @Autowired
    private IdWorker idWorker;


    /**
     * 创建岗位信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createPosition(SysPositionCreateParam param, SysUser sysUser) throws Exception {
        log.info("开始 创建岗位信息: param=" + JSON.toJSONString(param));
        // 创建部门信息
        try {
            SysPosition sysPosition = new SysPosition();
            // 属性copy
            BeanUtils.copyProperties(param, sysPosition);
            // 设置基本信息
            sysPosition.setId(idWorker.nextId());
            sysPosition.setCreateBy(sysUser.getId());
            sysPosition.setCreateTime(new Date());
            sysPositionDao.insertSelective(sysPosition);
            log.info("完成 创建岗位信息");
        } catch (Exception e) {
            throw CommonException.create(e, ServerResponse.createByError("创建岗位信息失败,请联系管理员！"));
        }
    }


    /**
     * 删除岗位信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePosition(Long positionId, SysUser sysUser) throws Exception {
        log.info("开始 删除岗位信息: positionId=" + positionId);
        // 创建部门信息
        try {
            sysPositionDao.deleteByPrimaryKey(positionId);
            log.info("完成 删除岗位信息");
        } catch (Exception e) {
            throw CommonException.create(e, ServerResponse.createByError("删除岗位信息失败,请联系管理员！"));
        }
    }


    /**
     * 查询岗位信息
     */
    @Override
    public PageInfo<SysPosition> queryPosition(SysPositionQueryParam param, SysUser sysUser) throws Exception {
        log.info("开始 查询岗位信息: param=" + JSON.toJSONString(param));
        // 查询岗位信息
        try {
            Example example = new Example(SysPosition.class);
            example.setOrderByClause(" create_time desc ");
            Example.Criteria criteria = example.createCriteria();
            if (StringUtils.isNotBlank(param.getNo())) {
                criteria.andEqualTo("no", param.getNo());
            }
            if (StringUtils.isNotBlank(param.getName())) {
                criteria.andLike("name", "%" + param.getName() + "%");
            }
            if (param.getCreateTimeLow() != null) {
                criteria.andGreaterThanOrEqualTo("createTime", param.getCreateTimeLow());
            }
            if (param.getCreateTimeUp() != null) {
                criteria.andLessThanOrEqualTo("createTime", param.getCreateTimeUp());
            }
            PageHelper.startPage(param.getPageNum(), param.getPageSize());
            List<SysPosition> sysPositions = sysPositionDao.selectByExample(example);
            PageInfo pageInfo = new PageInfo(sysPositions);
            PageHelper.clearPage();
            log.info("完成 查询岗位信息");
            return pageInfo;
        } catch (Exception e) {
            throw CommonException.create(e, ServerResponse.createByError("查询岗位信息失败,请联系管理员！"));
        }
    }


    /**
     * 获取全部岗位列表
     */
    @Override
    public List<SysPositionVo> queryAllPosition(SysUser sysUser) throws Exception {
        log.info("开始 查询岗位列表");
        try {
            List<SysPosition> sysPositions = sysPositionDao.selectAll();
            List<SysPositionVo> voList = sysPositions.stream().map(ele -> {
                SysPositionVo sysPositionVo = new SysPositionVo();
                BeanUtils.copyProperties(ele, sysPositionVo);
                return sysPositionVo;
            }).collect(Collectors.toList());
            log.info("完成 查询岗位列表");
            return voList;
        } catch (Exception e) {
            throw CommonException.create(e, ServerResponse.createByError("查询岗位列表失败,请联系管理员！"));
        }
    }
}
