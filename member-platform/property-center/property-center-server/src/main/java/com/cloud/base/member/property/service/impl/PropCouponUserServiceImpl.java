package com.cloud.base.member.property.service.impl;

import com.cloud.base.core.common.exception.CommonException;
import com.cloud.base.core.common.response.ServerResponse;
import com.cloud.base.core.modules.lh_security.core.entity.SecurityAuthority;
import com.cloud.base.member.property.feign.MchtApiClient;
import com.cloud.base.member.property.repository.dao.PropCouponInfoDao;
import com.cloud.base.member.property.repository.dao.PropCouponTemplateDao;
import com.cloud.base.member.property.repository.entity.PropCouponInfo;
import com.cloud.base.member.property.repository.entity.PropCouponTemplate;
import com.cloud.base.member.property.service.PropCouponUserService;
import com.cloud.base.member.property.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 优惠券资产  为用户提供的服务
 *
 * @author lh0811
 * @date 2021/6/4
 */
@Slf4j
@Service("propCouponUserService")
public class PropCouponUserServiceImpl implements PropCouponUserService {

    @Autowired
    private PropCouponTemplateDao propCouponTemplateDao;

    @Autowired
    private PropCouponInfoDao propCouponInfoDao;


    @Autowired
    private MchtApiClient mchtApiClient;


    /**
     * 获取当前用户优惠券列表
     */
    @Override
    public PropCouponOfUserVo getCouponInfoOfUser(SecurityAuthority securityAuthority) throws Exception {
        log.info("开始 获取当前用户优惠券列表");
        try {
            PropCouponOfUserVo propCouponOfUserVo = new PropCouponOfUserVo();
            Long userId = Long.valueOf(securityAuthority.getSecurityUser().getId());
            List<PropCouponDetailInfo> voList = propCouponInfoDao.getPropCouponOfUserByUserId(userId);
            if (CollectionUtils.isEmpty(voList)) {
                return propCouponOfUserVo;
            }
            Map<Integer, List<PropCouponDetailInfo>> groupByStatus = voList.stream().collect(Collectors.groupingBy(PropCouponDetailInfo::getStatus));

            // 按照状态分组
            PropCouponOfUserVo.PropCouponGroupByStatus propCouponGroupByStatus = new PropCouponOfUserVo.PropCouponGroupByStatus();
            for (Map.Entry<Integer, List<PropCouponDetailInfo>> entry : groupByStatus.entrySet()) {
                Integer status = entry.getKey();
                List<PropCouponDetailInfo> detailVoList = entry.getValue();
                if (PropCouponInfo.Status.INIT.getCode().equals(status)) {
                    propCouponGroupByStatus.setInitList(voList);
                } else if (PropCouponInfo.Status.CONSUMED.getCode().equals(status)) {
                    propCouponGroupByStatus.setConsumedList(voList);
                } else if (PropCouponInfo.Status.OVERDUE.getCode().equals(status)) {
                    propCouponGroupByStatus.setOverdueList(voList);
                } else if (PropCouponInfo.Status.INVALID.getCode().equals(status)) {
                    propCouponGroupByStatus.setInvalidList(voList);
                }
            }
            propCouponOfUserVo.setGroupByStatus(propCouponGroupByStatus);

            // 按照商户分组
            Map<String, List<PropCouponDetailInfo>> groupByMcht = voList.stream().collect(Collectors.groupingBy(ele -> ele.getMchtBaseInfoId() + ":" + ele.getMchtName() + ":" + ele.getMchtAddress()));
            for (Map.Entry<String, List<PropCouponDetailInfo>> entry : groupByMcht.entrySet()) {
                String[] split = entry.getKey().split(":");
                List<PropCouponDetailInfo> propCouponDetailInfoList = entry.getValue();
                String mchtId = split[0];
                String mchtName = split[1];
                String mchtAddress = split[2];
                PropCouponOfUserVo.PropCouponGroupByMchtBaseInfo groupByMchtBaseInfo  = new PropCouponOfUserVo.PropCouponGroupByMchtBaseInfo();
                groupByMchtBaseInfo.setMchtId(Long.valueOf(mchtId));
                groupByMchtBaseInfo.setMchtName(mchtName);
                groupByMchtBaseInfo.setMchtAddress(mchtAddress);
                groupByMchtBaseInfo.setCouponList(propCouponDetailInfoList);
                propCouponOfUserVo.getGroupByMcht().add(groupByMchtBaseInfo);
            }
            log.info("完成 获取当前用户优惠券列表");
            return propCouponOfUserVo;
        } catch (Exception e) {
            throw CommonException.create(e, ServerResponse.createByError("获取当前用户优惠券列表失败,请联系管理员"));
        }
    }


}
