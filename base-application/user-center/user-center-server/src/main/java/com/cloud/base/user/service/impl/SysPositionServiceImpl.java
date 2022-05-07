package com.cloud.base.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cloud.base.common.core.exception.CommonException;
import com.cloud.base.common.core.response.ServerResponse;
import com.cloud.base.common.core.util.IdWorker;
import com.cloud.base.common.xugou.core.model.entity.SecurityAuthority;
import com.cloud.base.user.param.SysPositionCreateParam;
import com.cloud.base.user.param.SysPositionQueryParam;
import com.cloud.base.user.repository.dao.SysPositionDao;
import com.cloud.base.user.repository.dao.SysUserPositionRelDao;
import com.cloud.base.user.repository.entity.SysPosition;
import com.cloud.base.user.repository.entity.SysUser;
import com.cloud.base.user.repository.entity.SysUserPositionRel;
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
    private SysUserPositionRelDao sysUserPositionRelDao;

    @Autowired
    private IdWorker idWorker;


    /**
     * 创建岗位信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createPosition(SysPositionCreateParam param, SecurityAuthority securityAuthority) throws Exception {
        log.info("开始 创建岗位信息: param=" + JSON.toJSONString(param));
        // 创建部门信息
        try {
            SysPosition sysPosition = new SysPosition();
            // 属性copy
            BeanUtils.copyProperties(param, sysPosition);
            // 设置基本信息
            sysPosition.setId(idWorker.nextId());
            sysPosition.setTenantNo(securityAuthority.getSecurityUser().getTenantNo());
            sysPosition.setCreateBy(Long.valueOf(securityAuthority.getSecurityUser().getId()));
            sysPosition.setCreateTime(new Date());
            sysPositionDao.save(sysPosition);
            UpdateWrapper<SysPosition> updateWrapper = new UpdateWrapper<>();
            updateWrapper.lambda();
            sysPositionDao.update(null,null);

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
    public void deletePosition(Long positionId, SecurityAuthority securityAuthority) throws Exception {
        log.info("开始 删除岗位信息: positionId=" + positionId);
        SysPosition position = sysPositionDao.getById(positionId);
        if (position == null) {
            throw CommonException.create(ServerResponse.createByError("当前岗位信息不存在"));
        }
        if (!position.getTenantNo().equals(securityAuthority.getSecurityUser().getTenantNo())) {
            throw CommonException.create(ServerResponse.createByError("非法操作"));
        }
        // 创建部门信息
        try {
            // 删除岗位和用户关系
            QueryWrapper<SysUserPositionRel> userPositionRelQuery = new QueryWrapper<>();
            userPositionRelQuery.lambda()
                    .eq(SysUserPositionRel::getPositionId, positionId)
                    .eq(SysUserPositionRel::getTenantNo, securityAuthority.getSecurityUser().getTenantNo());
            sysUserPositionRelDao.remove(userPositionRelQuery);
            // 删除岗位信息
            sysPositionDao.removeById(positionId);
            log.info("完成 删除岗位信息");
        } catch (Exception e) {
            throw CommonException.create(e, ServerResponse.createByError("删除岗位信息失败,请联系管理员！"));
        }
    }


    /**
     * 查询岗位信息
     */
    @Override
    public PageInfo<SysPositionVo> queryPosition(SysPositionQueryParam param, SecurityAuthority securityAuthority) throws Exception {
        log.info("开始 查询岗位信息: param=" + JSON.toJSONString(param));
        // 查询岗位信息
        try {
            QueryWrapper<SysPosition> queryWrapper = new QueryWrapper<>();
            LambdaQueryWrapper<SysPosition> lambda = queryWrapper.lambda();
            lambda.orderByDesc(SysPosition::getCreateTime);
            lambda.eq(SysPosition::getTenantNo, securityAuthority.getSecurityUser().getTenantNo());
            if (StringUtils.isNotBlank(param.getNo())) {
                lambda.eq(SysPosition::getNo, param.getNo());
            }
            if (StringUtils.isNotBlank(param.getName())) {
                lambda.like(SysPosition::getName, "%" + param.getName() + "%");
            }
            if (param.getCreateTimeLow() != null) {
                lambda.ge(SysPosition::getCreateTime, param.getCreateTimeLow());
            }
            if (param.getCreateTimeUp() != null) {
                lambda.le(SysPosition::getCreateTime, param.getCreateTimeUp());
            }
            PageHelper.startPage(param.getPageNum(), param.getPageSize());
            List<SysPosition> sysPositions = sysPositionDao.list(queryWrapper);
            PageInfo pageInfo = new PageInfo(sysPositions);
            PageHelper.clearPage();
            pageInfo.setList(JSONArray.parseArray(JSON.toJSONString(sysPositions), SysPositionVo.class));
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
    public List<SysPositionVo> queryAllPosition(SecurityAuthority securityAuthority) throws Exception {
        log.info("开始 查询岗位列表");
        try {
            QueryWrapper<SysPosition> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(SysPosition::getTenantNo,securityAuthority.getSecurityUser().getTenantNo());
            List<SysPosition> sysPositions = sysPositionDao.list(queryWrapper);
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
