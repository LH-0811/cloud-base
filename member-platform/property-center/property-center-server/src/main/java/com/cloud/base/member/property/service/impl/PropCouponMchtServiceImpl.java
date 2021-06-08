package com.cloud.base.member.property.service.impl;

import com.alibaba.fastjson.JSON;
import com.cloud.base.core.common.exception.CommonException;
import com.cloud.base.core.common.response.ServerResponse;
import com.cloud.base.core.common.util.IdWorker;
import com.cloud.base.core.modules.lh_security.core.entity.SecurityAuthority;
import com.cloud.base.member.common.method.UserRoleCheck;
import com.cloud.base.member.property.feign.MchtApiClient;
import com.cloud.base.member.property.param.*;
import com.cloud.base.member.property.repository.dao.PropCouponInfoDao;
import com.cloud.base.member.property.repository.dao.PropCouponTemplateDao;
import com.cloud.base.member.property.repository.entity.PropCouponInfo;
import com.cloud.base.member.property.repository.entity.PropCouponTemplate;
import com.cloud.base.member.property.service.PropCouponMchtService;
import com.cloud.base.member.property.vo.PropCouponDetailVo;
import com.cloud.base.member.property.vo.PropCouponInfoVo;
import com.cloud.base.member.property.vo.PropCouponTemplateVo;
import com.cloud.base.memeber.merchant.vo.MchtInfoVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
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
 * @date 2021/5/31
 */
@Slf4j
@Service("propCouponBaseService")
public class PropCouponMchtServiceImpl implements PropCouponMchtService {

    @Autowired
    private PropCouponTemplateDao propCouponTemplateDao;

    @Autowired
    private MchtApiClient mchtApiClient;

    @Autowired
    private PropCouponInfoDao propCouponInfoDao;

    @Autowired
    private IdWorker idWorker;


    /**
     * 创建优惠券模板
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void couponTemplateCreate(PropCouponTemplateCreateParam param, SecurityAuthority authority) throws Exception {
        log.info("开始 创建优惠券模板:param:{},currentUserId:{}", JSON.toJSONString(param), authority.getSecurityUser().getId());
        // 防止越权
        defenseSkipOverAuthority(param.getMchtBaseInfoId(), authority);
        // 检查参数
        checkPropCouponTemplateCreateParam(param);
        try {
            // 创建数据
            PropCouponTemplate template = new PropCouponTemplate();
            // 属性对拷
            BeanUtils.copyProperties(param, template);

            template.setId(idWorker.nextId());
            template.setCreateBy(Long.valueOf(authority.getSecurityUser().getId()));
            template.setCreateTime(new Date());
            propCouponTemplateDao.insertSelective(template);
            log.info("完成 创建优惠券模板");
        } catch (Exception e) {
            throw CommonException.create(e, ServerResponse.createByError("创建优惠券模板失败,请联系管理员"));
        }
    }

    /**
     * 修改优惠券模板
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void couponTemplateUpdate(PropCouponTemplateUpdateParam param, SecurityAuthority authority) throws Exception {
        log.info("开始 修改优惠券模板:param:{},currentUserId:{}", JSON.toJSONString(param), authority.getSecurityUser().getId());

        // 检测数据是否存在
        PropCouponTemplate existTemplate = propCouponTemplateDao.selectByPrimaryKey(param.getId());
        if (existTemplate == null) {
            throw CommonException.create(ServerResponse.createByError("优惠券模板信息不存在"));
        }

        // 防止越权
        defenseSkipOverAuthority(existTemplate.getMchtBaseInfoId(), authority);
        // 检查参数
        checkPropCouponTemplateUpdateParam(param);
        try {
            // 创建数据
            PropCouponTemplate templateUpdate = new PropCouponTemplate();
            // 属性对拷
            BeanUtils.copyProperties(param, templateUpdate);

            templateUpdate.setId(idWorker.nextId());
            templateUpdate.setUpdateBy(Long.valueOf(authority.getSecurityUser().getId()));
            templateUpdate.setUpdateTime(new Date());
            propCouponTemplateDao.updateByPrimaryKeySelective(templateUpdate);
            log.info("完成 修改优惠券模板");
        } catch (Exception e) {
            throw CommonException.create(e, ServerResponse.createByError("修改优惠券模板失败,请联系管理员"));
        }
    }

    /**
     * 删除优惠券模板
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void couponTemplateDelete(Long templateId, SecurityAuthority authority) throws Exception {
        log.info("开始 删除优惠券模板:templateId:{},currentUserId:{}", JSON.toJSONString(templateId), authority.getSecurityUser().getId());

        // 检测数据是否存在
        PropCouponTemplate existTemplate = propCouponTemplateDao.selectByPrimaryKey(templateId);
        if (existTemplate == null) {
            throw CommonException.create(ServerResponse.createByError("优惠券模板信息不存在"));
        }
        // 防止越权
        defenseSkipOverAuthority(existTemplate.getMchtBaseInfoId(), authority);
        try {
            propCouponTemplateDao.deleteByPrimaryKey(templateId);
            log.info("完成 删除优惠券模板");
        } catch (Exception e) {
            throw CommonException.create(e, ServerResponse.createByError("删除优惠券模板失败,请联系管理员"));
        }

    }

    /**
     * 查询优惠券模板
     */
    @Override
    public PageInfo<PropCouponTemplateVo> couponTemplateQuery(PropCouponTemplateQueryParam param, SecurityAuthority securityAuthority) throws Exception {
        log.info("开始 查询优惠券模板:param-{}", JSON.toJSONString(param));
        try {
            Example example = new Example(PropCouponTemplate.class);
            example.setOrderByClause(" create_time desc ");
            Example.Criteria criteria = example.createCriteria();
            if (param.getMchtBaseInfoId() == null) {
                if (!UserRoleCheck.isSysAdmin(securityAuthority)) {
                    ServerResponse<List<MchtInfoVo>> response = mchtApiClient.getMchtBaseInfoByUserId(Long.valueOf(securityAuthority.getSecurityUser().getId()));
                    if (!response.isSuccess()) throw CommonException.create(response);
                    if (CollectionUtils.isEmpty(response.getData())) {
                        throw CommonException.create(ServerResponse.createByError("当前用户无关联的商户信息"));
                    }
                    criteria.andIn("mchtBaseInfoId", response.getData().stream().map(ele -> ele.getId()).collect(Collectors.toList()));
                }
            }
            if (param.getCouponType() != null) {
                criteria.andEqualTo("couponType", param.getCouponType());
            }
            if (param.getFullScoreLow() != null) {
                criteria.andGreaterThanOrEqualTo("fullScore", param.getFullScoreLow());
            }
            if (param.getFullScoreUp() != null) {
                criteria.andLessThanOrEqualTo("fullScore", param.getFullScoreUp());
            }
            if (param.getFullNumberLow() != null) {
                criteria.andGreaterThanOrEqualTo("fullNumber", param.getFullNumberLow());
            }
            if (param.getFullNumberUp() != null) {
                criteria.andLessThanOrEqualTo("fullNumber", param.getFullNumberUp());
            }
            if (param.getReduceDiscountLow() != null) {
                criteria.andGreaterThanOrEqualTo("reduceDiscount", param.getReduceDiscountLow());
            }
            if (param.getReduceDiscountUp() != null) {
                criteria.andLessThanOrEqualTo("reduceDiscount", param.getReduceDiscountUp());
            }
            if (param.getEnableFlag() != null) {
                criteria.andEqualTo("enableFlag", param.getEnableFlag());
            }
            if (param.getCreateTimeLow() != null) {
                criteria.andGreaterThanOrEqualTo("createTime", param.getCreateTimeLow());
            }
            if (param.getCreateTimeUp() != null) {
                criteria.andLessThanOrEqualTo("createTime", param.getCreateTimeUp());
            }
            PageHelper.startPage(param.getPageNum(), param.getPageSize());
            List<PropCouponTemplate> propCouponTemplates = propCouponTemplateDao.selectByExample(example);
            PageInfo pageInfo = new PageInfo(propCouponTemplates);
            PageHelper.clearPage();
            List<PropCouponTemplateVo> templateVoList = propCouponTemplates.stream().map(ele -> {
                PropCouponTemplateVo templateVo = new PropCouponTemplateVo();
                BeanUtils.copyProperties(ele, templateVo);
                return templateVo;
            }).collect(Collectors.toList());
            pageInfo.setList(templateVoList);
            log.info("完成 查询优惠券模板");
            return pageInfo;
        } catch (Exception e) {
            throw CommonException.create(e, ServerResponse.createByError("查询优惠券模板失败,请联系管理员"));
        }
    }

    /**
     * 创建优惠券信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void couponInfoCreate(PropCouponInfoCreateParam param, SecurityAuthority securityAuthority) throws Exception {
        log.info("开始 创建优惠券信息:param-{}", JSON.toJSONString(param));
        // 获取到优惠券模板信息
        PropCouponTemplate template = propCouponTemplateDao.selectByPrimaryKey(param.getTemplateId());
        if (template == null) {
            throw CommonException.create(ServerResponse.createByError("模板信息不存在"));
        }
        // 越权检查
        if (!UserRoleCheck.isSysAdmin(securityAuthority)) {
            ServerResponse<List<MchtInfoVo>> response = mchtApiClient.getMchtBaseInfoByUserId(Long.valueOf(securityAuthority.getSecurityUser().getId()));
            if (!response.isSuccess()) {
                throw CommonException.create(response);
            }
            List<Long> mchtIdList = response.getData().stream().map(ele -> ele.getId()).collect(Collectors.toList());
            if (!mchtIdList.contains(template.getMchtBaseInfoId())) {
                throw CommonException.create(ServerResponse.createByError("不能使用不属于自己的商户的优惠券模板"));
            }
        }

        try {
            PropCouponInfo propCouponInfo = new PropCouponInfo();
            propCouponInfo.setId(idWorker.nextId());
            propCouponInfo.setUserId(param.getUserId());
            propCouponInfo.setTemplateId(param.getTemplateId());
            propCouponInfo.setMchtBaseInfoId(template.getMchtBaseInfoId());
            propCouponInfo.setExpiryTime(DateUtils.addMinutes(new Date(), template.getEffectiveMinute()));
            propCouponInfo.setStatus(PropCouponInfo.Status.INIT.getCode());
            propCouponInfo.setDelFlag(false);
            propCouponInfo.setCreateBy(Long.valueOf(securityAuthority.getSecurityUser().getId()));
            propCouponInfo.setCreateTime(new Date());
            propCouponInfoDao.insertSelective(propCouponInfo);
            log.info("完成 创建优惠券信息");
        } catch (Exception e) {
            throw CommonException.create(e, ServerResponse.createByError("创建优惠券信息失败,请联系管理员"));
        }

    }

    /**
     * 根据模块id查询优惠券信息列表
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public PageInfo<PropCouponInfoVo> couponInfoQuery(PropCouponInfoQueryParam param, SecurityAuthority securityAuthority) throws Exception {
        log.info("开始 查询优惠券信息列表:param-{}", JSON.toJSONString(param));
        try {
            Example example = new Example(PropCouponInfo.class);
            example.setOrderByClause(" create_time desc ");
            Example.Criteria criteria = example.createCriteria();
            if (param.getCouponType() != null) {
                criteria.andEqualTo("couponType", param.getCouponType());
            }
            if (param.getTemplateId() != null) {
                criteria.andEqualTo("templateId", param.getTemplateId());
            }
            if (param.getMchtBaseInfoId() != null) {
                criteria.andEqualTo("mchtBaseInfoId", param.getMchtBaseInfoId());
            }
            if (param.getExpiryTimeLow() != null) {
                criteria.andGreaterThanOrEqualTo("expiryTime", param.getExpiryTimeLow());
            }
            if (param.getExpiryTimeUp() != null) {
                criteria.andLessThanOrEqualTo("expiryTime", param.getExpiryTimeUp());
            }
            if (param.getStatus() != null) {
                criteria.andEqualTo("status", param.getStatus());
            }
            if (param.getCreateTimeLow() != null) {
                criteria.andEqualTo("createTime", param.getCreateTimeLow());
            }
            if (param.getCreateTimeUp() != null) {
                criteria.andEqualTo("createTime", param.getCreateTimeUp());
            }
            PageHelper.startPage(param.getPageNum(), param.getPageSize());
            List<PropCouponInfo> propCouponInfos = propCouponInfoDao.selectByExample(example);
            PageInfo pageInfo = new PageInfo(propCouponInfos);
            PageHelper.clearPage();
            List<PropCouponInfoVo> infoVoList = propCouponInfos.stream().map(ele -> {
                PropCouponInfoVo infoVo = new PropCouponInfoVo();
                BeanUtils.copyProperties(ele, infoVo);
                return infoVo;
            }).collect(Collectors.toList());
            pageInfo.setList(infoVoList);
            log.info("完成 查询优惠券信息列表");
            return pageInfo;
        } catch (Exception e) {
            throw CommonException.create(e, ServerResponse.createByError("查询优惠券信息列表失败,请联系管理员"));
        }


    }


    /**
     * 优惠券消费
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void couponInfoConsume(Long couponInfoId, SecurityAuthority securityAuthority) throws Exception {
        log.info("开始 优惠券消费 couponInfoId-{}", couponInfoId);
        PropCouponInfo propCouponInfo = propCouponInfoDao.selectByPrimaryKey(couponInfoId);
        if (propCouponInfo == null) {
            throw CommonException.create(ServerResponse.createByError("优惠券信息不存在"));
        }
        try {
            PropCouponInfo updateCouponInfo = new PropCouponInfo();
            updateCouponInfo.setId(propCouponInfo.getId());
            updateCouponInfo.setStatus(PropCouponInfo.Status.CONSUMED.getCode());
            updateCouponInfo.setUpdateBy(Long.valueOf(securityAuthority.getSecurityUser().getId()));
            updateCouponInfo.setUpdateTime(new Date());
            propCouponInfoDao.updateByPrimaryKeySelective(updateCouponInfo);
            log.info("完成 优惠券消费");
        } catch (Exception e) {
            throw CommonException.create(e, ServerResponse.createByError("优惠券消费失败,请联系管理员"));
        }
    }


    /**
     * 优惠券消费
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void couponInfoInvalid(Long couponInfoId, SecurityAuthority securityAuthority) throws Exception {
        log.info("开始 优惠券失效 couponInfoId-{}", couponInfoId);
        PropCouponInfo propCouponInfo = propCouponInfoDao.selectByPrimaryKey(couponInfoId);
        if (propCouponInfo == null) {
            throw CommonException.create(ServerResponse.createByError("优惠券信息不存在"));
        }
        try {
            PropCouponInfo updateCouponInfo = new PropCouponInfo();
            updateCouponInfo.setId(propCouponInfo.getId());
            updateCouponInfo.setStatus(PropCouponInfo.Status.CONSUMED.getCode());
            updateCouponInfo.setUpdateBy(Long.valueOf(securityAuthority.getSecurityUser().getId()));
            updateCouponInfo.setUpdateTime(new Date());
            propCouponInfoDao.updateByPrimaryKeySelective(updateCouponInfo);
            log.info("完成 优惠券失效");
        } catch (Exception e) {
            throw CommonException.create(e, ServerResponse.createByError("优惠券失效失败,请联系管理员"));
        }
    }


    /**
     * 获取优惠券详情
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public PropCouponDetailVo couponInfoDetailInfo(Long couponInfoId, SecurityAuthority securityAuthority) throws Exception {
        log.info("开始 获取优惠券详情 couponInfoId-{}", couponInfoId);
        PropCouponInfo info = propCouponInfoDao.selectByPrimaryKey(couponInfoId);
        if (info == null) {
            throw CommonException.create(ServerResponse.createByError("优惠券信息不存在"));
        }
        PropCouponTemplate template = propCouponTemplateDao.selectByPrimaryKey(info.getTemplateId());
        if (template == null) {
            throw CommonException.create(ServerResponse.createByError("优惠券对应的模板数据不存在"));
        }
        try {
            PropCouponDetailVo detailVo = new PropCouponDetailVo();
            PropCouponInfoVo infoVo = new PropCouponInfoVo();
            BeanUtils.copyProperties(info, infoVo);
            PropCouponTemplateVo templateVo = new PropCouponTemplateVo();
            BeanUtils.copyProperties(template, templateVo);
            detailVo.setCouponInfoVo(infoVo);
            detailVo.setPropCouponTemplateVo(templateVo);
            log.info("完成 获取优惠券详情");
            return detailVo;
        } catch (Exception e) {
            throw CommonException.create(e, ServerResponse.createByError("获取优惠券详情失败,请联系管理员"));
        }
    }


    ////私有方法//////////////////////////////////////////////////////////////////

    private void defenseSkipOverAuthority(Long mchtBaseInfoId, SecurityAuthority authority) throws Exception {
        if (!UserRoleCheck.isSysAdmin(authority) && !UserRoleCheck.isMchtAdmin(authority)) {
            throw CommonException.create(ServerResponse.createByError("当前用户没有权限进行该操作"));
        }
        if (UserRoleCheck.isMchtAdmin(authority)) {
            ServerResponse<List<MchtInfoVo>> response = mchtApiClient.getMchtBaseInfoByUserId(Long.valueOf(authority.getSecurityUser().getId()));
            if (!response.isSuccess()) throw CommonException.create(response);
            List<MchtInfoVo> mchtInfoVoList = response.getData();
            if (CollectionUtils.isEmpty(mchtInfoVoList)) {
                throw CommonException.create(ServerResponse.createByError("该用户无关联的商户基本信息"));
            }
            List<Long> mchtBaseIdList = mchtInfoVoList.stream().map(ele -> ele.getId()).collect(Collectors.toList());
            if (!mchtBaseIdList.contains(mchtBaseInfoId))
                throw CommonException.create(ServerResponse.createByError("非法操作，当前商户不属于操作用户"));
        }
    }

    /**
     * 检查创建参数
     */
    private void checkPropCouponTemplateCreateParam(PropCouponTemplateCreateParam param) throws Exception {
        // 如果是线下优惠券 必须填写优化全内容描述该券的能力
        if (PropCouponTemplateCreateParam.CouponType.OFFLINE.getCode().equals(param.getCouponType())) {
            if (StringUtils.isBlank(param.getOfflineFunction())) {
                throw CommonException.create(ServerResponse.createByError("线下优惠券必须填写优惠内容"));
            }
        } else if (PropCouponTemplateCreateParam.CouponType.FULL_REDUCE.getCode().equals(param.getCouponType())) {
            if (param.getFullScore() == null) {
                throw CommonException.create(ServerResponse.createByError("满减券必须填写满足条件金额"));
            }
            if (param.getReduceScore() == null) {
                throw CommonException.create(ServerResponse.createByError("满减券必须填写优惠减免金额"));
            }
        } else if (PropCouponTemplateCreateParam.CouponType.DISCOUNT.getCode().equals(param.getCouponType())) {
            if (param.getDiscountProductId() == null) {
                throw CommonException.create(ServerResponse.createByError("折扣优惠券必须指定商品"));
            }
            if (param.getFullNumber() == null) {
                throw CommonException.create(ServerResponse.createByError("折扣优惠券必须填写满几件"));
            }
            if (param.getReduceDiscount() == null) {
                throw CommonException.create(ServerResponse.createByError("折扣优惠券必须填写折扣比例"));
            }
        }
    }

    /**
     * 检查修改参数
     */
    private void checkPropCouponTemplateUpdateParam(PropCouponTemplateUpdateParam param) throws Exception {
        // 如果是线下优惠券 必须填写优化全内容描述该券的能力
        if (PropCouponTemplateCreateParam.CouponType.OFFLINE.getCode().equals(param.getCouponType())) {
            if (StringUtils.isBlank(param.getOfflineFunction())) {
                throw CommonException.create(ServerResponse.createByError("线下优惠券必须填写优惠内容"));
            }
        } else if (PropCouponTemplateCreateParam.CouponType.FULL_REDUCE.getCode().equals(param.getCouponType())) {
            if (param.getFullScore() == null) {
                throw CommonException.create(ServerResponse.createByError("满减券必须填写满足条件金额"));
            }
            if (param.getReduceScore() == null) {
                throw CommonException.create(ServerResponse.createByError("满减券必须填写优惠减免金额"));
            }
        } else if (PropCouponTemplateCreateParam.CouponType.DISCOUNT.getCode().equals(param.getCouponType())) {
            if (param.getDiscountProductId() == null) {
                throw CommonException.create(ServerResponse.createByError("折扣优惠券必须指定商品"));
            }
            if (param.getFullNumber() == null) {
                throw CommonException.create(ServerResponse.createByError("折扣优惠券必须填写满几件"));
            }
            if (param.getReduceDiscount() == null) {
                throw CommonException.create(ServerResponse.createByError("折扣优惠券必须填写折扣比例"));
            }
        }
    }

}
