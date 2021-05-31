package com.cloud.base.member.property.service.impl;

import com.alibaba.fastjson.JSON;
import com.cloud.base.core.common.exception.CommonException;
import com.cloud.base.core.common.response.ServerResponse;
import com.cloud.base.core.common.util.IdWorker;
import com.cloud.base.core.modules.lh_security.core.entity.SecurityAuthority;
import com.cloud.base.member.common.method.UserRoleCheck;
import com.cloud.base.member.property.feign.MchtBaseInfoApiClient;
import com.cloud.base.member.property.param.PropCouponTemplateCreateParam;
import com.cloud.base.member.property.param.PropCouponTemplateUpdateParam;
import com.cloud.base.member.property.repository.dao.PropCouponTemplateDao;
import com.cloud.base.member.property.repository.entity.PropCouponTemplate;
import com.cloud.base.member.property.service.PropCouponTemplateService;
import com.cloud.base.memeber.merchant.vo.MchtBaseInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
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
 * @date 2021/5/31
 */
@Slf4j
@Service("propCouponTemplateService")
public class PropCouponTemplateServiceImpl implements PropCouponTemplateService {

    @Autowired
    private PropCouponTemplateDao propCouponTemplateDao;

    @Autowired
    private MchtBaseInfoApiClient mchtBaseInfoApiClient;

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


    ////私有方法//////////////////////////////////////////////////////////////////

    private void defenseSkipOverAuthority(Long mchtBaseInfoId, SecurityAuthority authority) throws Exception {
        if (!UserRoleCheck.isSysAdmin(authority) && !UserRoleCheck.isMchtAdmin(authority)) {
            throw CommonException.create(ServerResponse.createByError("当前用户没有权限进行该操作"));
        }
        if (UserRoleCheck.isMchtAdmin(authority)) {
            ServerResponse<List<MchtBaseInfoVo>> response = mchtBaseInfoApiClient.getMchtBaseInfoByUserId(Long.valueOf(authority.getSecurityUser().getId()));
            if (!response.isSuccess()) throw CommonException.create(response);
            List<MchtBaseInfoVo> mchtBaseInfoVoList = response.getData();
            if (CollectionUtils.isEmpty(mchtBaseInfoVoList)) {
                throw CommonException.create(ServerResponse.createByError("该用户无关联的商户基本信息"));
            }
            List<Long> mchtBaseIdList = mchtBaseInfoVoList.stream().map(ele -> ele.getId()).collect(Collectors.toList());
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
