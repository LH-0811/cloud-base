package com.cloud.base.member.merchant.service;

import com.cloud.base.core.modules.lh_security.core.entity.SecurityAuthority;
import com.cloud.base.member.merchant.repository.entity.MchtBaseInfo;
import com.cloud.base.memeber.merchant.param.MchtBaseInfoCreateParam;
import com.cloud.base.memeber.merchant.param.MchtBaseInfoQueryParam;
import com.cloud.base.memeber.merchant.param.MchtBaseInfoUpdateParam;
import com.cloud.base.memeber.merchant.vo.MchtBaseInfoVo;
import com.github.pagehelper.PageInfo;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 商户基本信息 服务接口
 *
 * @author lh0811
 * @date 2021/5/26
 */
public interface MchtBaseInfoService {

    /**
     * 创建商户基本信息
     */
    void mchtBaseInfoCreate(MchtBaseInfoCreateParam param, SecurityAuthority securityAuthority) throws Exception;

    /**
     * 查询商户基本信息
     */
    PageInfo<MchtBaseInfoVo> queryMchtBaseInfo(MchtBaseInfoQueryParam param) throws Exception;

    /**
     * 更新商户基本信息
     *
     * @param param
     * @param securityAuthority
     * @throws Exception
     */
    void updateMchtBaseInfo(MchtBaseInfoUpdateParam param, SecurityAuthority securityAuthority) throws Exception;

    /**
     * 根据用户id 查询用户关联的商户基本信息
     */
    List<MchtBaseInfo> getMchtBaseInfoByUserId(Long userId, SecurityAuthority securityAuthority) throws Exception;
}
