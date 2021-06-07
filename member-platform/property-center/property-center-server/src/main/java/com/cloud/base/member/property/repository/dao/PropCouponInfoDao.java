package com.cloud.base.member.property.repository.dao;

import com.cloud.base.member.property.repository.entity.PropCouponInfo;
import com.cloud.base.member.property.vo.PropCouponDetailInfo;
import com.cloud.base.member.property.vo.PropCouponOfUserVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * 优惠券信息表(PropCouponInfo)表数据库访问层
 *
 * @author lh0811
 * @since 2021-05-31 16:28:45
 */
public interface PropCouponInfoDao extends Mapper<PropCouponInfo> {

    @Select("<script>" +
            "SELECT " +
            " pct.id AS templateId, " +
            " pct.mcht_base_info_id AS mchtBaseInfoId, " +
            " pct.mcht_name AS mchtName, " +
            " pct.mcht_address AS mchtAddress, " +
            " pci.id AS couponInfoId, " +
            " pci.user_id AS userId, " +
            " pct.coupon_type AS couponType, " +
            " pct.`describe` AS `describe`, " +
            " pct.effective_minute AS effectiveMinute, " +
            " pct.full_score AS fullScore, " +
            " pct.reduce_score AS reduceScore, " +
            " pct.full_number AS fullNumber, " +
            " pct.reduce_discount AS reduceDiscount, " +
            " pct.offline_function AS offlineFunction, " +
            " pct.enable_flag AS templateEnableFlag, " +
            " pci.expiry_time AS expiryTime, " +
            " pci.`status` AS `status`, " +
            " pci.invalid_time AS invalidTime, " +
            " pci.invalid_by AS invalidBy, " +
            " pci.invalid_reason AS invalidReason  " +
            "FROM " +
            " ( SELECT * FROM prop_coupon_info WHERE user_id = #{userId} ) pci " +
            " LEFT JOIN prop_coupon_template pct ON pci.template_id = pct.id  " +
            "WHERE " +
            " pci.del_flag = FALSE" +
            "</script>")
    List<PropCouponDetailInfo> getPropCouponOfUserByUserId(@Param(value = "userId") Long userId);



}
