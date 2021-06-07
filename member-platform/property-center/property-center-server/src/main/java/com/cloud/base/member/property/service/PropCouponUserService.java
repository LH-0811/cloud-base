package com.cloud.base.member.property.service;

import com.cloud.base.core.modules.lh_security.core.entity.SecurityAuthority;
import com.cloud.base.member.property.vo.PropCouponOfUserVo;

/**
 * 优惠券资产 为用户提供服务
 *
 * @author lh0811
 * @date 2021/5/31
 */
public interface PropCouponUserService {

    /**
     * 获取当前用户优惠券列表
     */
    PropCouponOfUserVo getCouponInfoOfUser(SecurityAuthority securityAuthority) throws Exception;



}
