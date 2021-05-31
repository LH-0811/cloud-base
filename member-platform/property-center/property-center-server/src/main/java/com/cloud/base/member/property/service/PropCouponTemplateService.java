package com.cloud.base.member.property.service;

import com.cloud.base.core.modules.lh_security.core.entity.SecurityAuthority;
import com.cloud.base.member.property.param.PropCouponTemplateCreateParam;
import com.cloud.base.member.property.param.PropCouponTemplateUpdateParam;
import org.springframework.transaction.annotation.Transactional;

/**
 * 优惠券模板服务
 *
 * @author lh0811
 * @date 2021/5/31
 */
public interface PropCouponTemplateService {
    /**
     * 创建优惠券模板
     */
    void couponTemplateCreate(PropCouponTemplateCreateParam param, SecurityAuthority authority) throws Exception;

    /**
     * 修改优惠券模板
     */
    @Transactional(rollbackFor = Exception.class)
    void couponTemplateUpdate(PropCouponTemplateUpdateParam param, SecurityAuthority authority) throws Exception;
}
