package com.cloud.base.member.property.service;

import com.cloud.base.core.modules.lh_security.core.entity.SecurityAuthority;
import com.cloud.base.member.property.param.*;
import com.cloud.base.member.property.vo.PropCouponDetailVo;
import com.cloud.base.member.property.vo.PropCouponInfoVo;
import com.cloud.base.member.property.vo.PropCouponTemplateVo;
import com.github.pagehelper.PageInfo;
import org.springframework.transaction.annotation.Transactional;

/**
 * 优惠券模板服务
 *
 * @author lh0811
 * @date 2021/5/31
 */
public interface PropCouponService {
    /**
     * 创建优惠券模板
     */
    void couponTemplateCreate(PropCouponTemplateCreateParam param, SecurityAuthority authority) throws Exception;

    /**
     * 修改优惠券模板
     */
    void couponTemplateUpdate(PropCouponTemplateUpdateParam param, SecurityAuthority authority) throws Exception;

    /**
     * 删除优惠券模板
     */
    void couponTemplateDelete(Long templateId, SecurityAuthority authority) throws Exception;

    /**
     * 查询优惠券模板
     */
    PageInfo<PropCouponTemplateVo> couponTemplateQuery(PropCouponTemplateQueryParam param, SecurityAuthority securityAuthority) throws Exception;

    /**
     * 创建优惠券信息
     */
    void couponInfoCreate(PropCouponInfoCreateParam param, SecurityAuthority securityAuthority) throws Exception;

    /**
     * 根据模块id查询优惠券信息列表
     */
    PageInfo<PropCouponInfoVo> couponInfoQuery(PropCouponInfoQueryParam param, SecurityAuthority securityAuthority) throws Exception;

    /**
     * 优惠券消费
     */
    void couponInfoConsume(Long couponInfoId, SecurityAuthority securityAuthority) throws Exception;

    /**
     * 优惠券消费
     */
    void couponInfoInvalid(Long couponInfoId, SecurityAuthority securityAuthority) throws Exception;

    /**
     * 获取优惠券详情
     */
    PropCouponDetailVo couponInfoDetailInfo(Long couponInfoId, SecurityAuthority securityAuthority) throws Exception;
}
