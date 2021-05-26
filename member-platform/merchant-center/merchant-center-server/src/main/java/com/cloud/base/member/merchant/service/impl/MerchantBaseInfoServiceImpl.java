package com.cloud.base.member.merchant.service.impl;

import com.cloud.base.member.merchant.repository.dao.MchtBaseInfoDao;
import com.cloud.base.member.merchant.service.MerchantBaseInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 商户基本信息 服务实现
 *
 * @author lh0811
 * @date 2021/5/26
 */
@Service("merchantBaseInfoService")
public class MerchantBaseInfoServiceImpl implements MerchantBaseInfoService {

    @Autowired
    private MchtBaseInfoDao mchtBaseInfoDao;




}
