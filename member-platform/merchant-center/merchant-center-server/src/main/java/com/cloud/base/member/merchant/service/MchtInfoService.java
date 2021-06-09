package com.cloud.base.member.merchant.service;

import com.cloud.base.core.modules.lh_security.core.entity.SecurityAuthority;
import com.cloud.base.member.merchant.param.MchtInfoCreateParam;
import com.cloud.base.member.merchant.param.MchtInfoQueryParam;
import com.cloud.base.member.merchant.param.MchtInfoUpdateParam;
import com.cloud.base.member.merchant.param.MchtGiftSettingsSaveParam;
import com.cloud.base.member.merchant.vo.MchtInfoVo;
import com.cloud.base.member.merchant.vo.MchtVipUserVo;
import com.github.pagehelper.PageInfo;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 商户基本信息 服务接口
 *
 * @author lh0811
 * @date 2021/5/26
 */
public interface MchtInfoService {

    /**
     * 创建商户基本信息
     */
    void mchtBaseInfoCreate(MchtInfoCreateParam param, SecurityAuthority securityAuthority) throws Exception;

    /**
     * 查询商户基本信息
     */
    PageInfo<MchtInfoVo> queryMchtBaseInfo(MchtInfoQueryParam param) throws Exception;

    /**
     * 更新商户基本信息
     *
     * @param param
     * @param securityAuthority
     * @throws Exception
     */
    void updateMchtBaseInfo(MchtInfoUpdateParam param, SecurityAuthority securityAuthority) throws Exception;

    /**
     * 根据用户id 查询用户关联的商户基本信息
     */
    List<MchtInfoVo> getMchtBaseInfoByUserId(Long userId, SecurityAuthority securityAuthority) throws Exception;

    /**
     * 删除商户信息
     */
    void deletaMchtBaseInfo(Long mchtBaseId, SecurityAuthority securityAuthority) throws Exception;

    /**
     * 保存商户的福利配置
     */
    void saveMchtGiftSettings(MchtGiftSettingsSaveParam param, SecurityAuthority securityAuthority) throws Exception;

    /**
     * 获取商户基本信息
     */
    MchtInfoVo getMchtBaseInfoVoById(Long mchtBaseInfoId) throws Exception;

    /**
     * 获取当前用户关联商户列表
     */
    List<MchtInfoVo> getMchtInfoOfCurrentUser(SecurityAuthority securityAuthority) throws Exception;

    /**
     * 用户加入到商户vip
     */
    void joinToMchtVip(Long mchtId, SecurityAuthority securityAuthority) throws Exception;

    /**
     * 获取商户的vip用户列表
     */
    List<MchtVipUserVo> getVipUserOfMcht(Long mchtId) throws Exception;
}
