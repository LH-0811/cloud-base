package com.cloud.base.member.merchant.service.impl;

import com.alibaba.fastjson.JSON;
import com.cloud.base.core.common.exception.CommonException;
import com.cloud.base.core.common.response.ServerResponse;
import com.cloud.base.core.common.util.IdWorker;
import com.cloud.base.core.modules.lh_security.core.entity.SecurityAuthority;
import com.cloud.base.member.common.method.UserRoleCheck;
import com.cloud.base.member.merchant.repository.dao.MchtBaseInfoDao;
import com.cloud.base.member.merchant.repository.dao.MchtGiftSettingsDao;
import com.cloud.base.member.merchant.repository.entity.MchtBaseInfo;
import com.cloud.base.member.merchant.repository.entity.MchtGiftSettings;
import com.cloud.base.member.merchant.service.MchtInfoService;
import com.cloud.base.memeber.merchant.param.MchtBaseInfoQueryParam;
import com.cloud.base.memeber.merchant.param.MchtBaseInfoCreateParam;
import com.cloud.base.memeber.merchant.param.MchtBaseInfoUpdateParam;
import com.cloud.base.memeber.merchant.param.MchtGiftSettingsSaveParam;
import com.cloud.base.memeber.merchant.vo.MchtBaseInfoVo;
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
 * 商户基本信息 服务实现
 *
 * @author lh0811
 * @date 2021/5/26
 */
@Slf4j
@Service("merchantBaseInfoService")
public class MchtInfoServiceImpl implements MchtInfoService {

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private MchtGiftSettingsDao mchtGiftSettingsDao;

    @Autowired
    private MchtBaseInfoDao mchtBaseInfoDao;

    /**
     * 创建商户基本信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void mchtBaseInfoCreate(MchtBaseInfoCreateParam param, SecurityAuthority securityAuthority) throws Exception {
        log.info("开始 创建商户基本信息:{}", JSON.toJSONString(param));
        // 检查商户名是否已经存在
        checkMerchantNameExist(param.getMchtName());

        // 创建基础信息
        MchtBaseInfo mchtBaseInfo = new MchtBaseInfo();
        try {
            // 属性对拷
            BeanUtils.copyProperties(param, mchtBaseInfo);
            // 设置
            mchtBaseInfo.setId(idWorker.nextId());
            mchtBaseInfo.setEnableFlag(false);
            mchtBaseInfo.setDelFlag(false);
            mchtBaseInfo.setCreateTime(new Date());
            mchtBaseInfo.setCreateBy(Long.valueOf(securityAuthority.getSecurityUser().getId()));
            mchtBaseInfoDao.insertSelective(mchtBaseInfo);
        } catch (Exception e) {
            throw CommonException.create(e, ServerResponse.createByError("创建商户基本信息失败,请联系管理员"));
        }

        // 初始化福利设置
        initMchtGiftSettings(mchtBaseInfo.getId());
        log.info("完成 创建商户基本信息");
    }

    /**
     * 查询商户基本信息
     */
    @Override
    public PageInfo<MchtBaseInfoVo> queryMchtBaseInfo(MchtBaseInfoQueryParam param) throws Exception {
        log.info("开始查询商户基本信息:{}", JSON.toJSONString(param));
        try {

            Example example = new Example(MchtBaseInfo.class);
            example.setOrderByClause(" create_time desc ");
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("delFalg",false);
            if (param.getMchtUserId() != null) {
                criteria.andEqualTo("mchtUserId", param.getMchtUserId());
            }
            if (StringUtils.isNotBlank(param.getMchtName())) {
                criteria.andLike("mchtName", "%" + param.getMchtName() + "%");
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
            List<MchtBaseInfo> mchtBaseInfoList = mchtBaseInfoDao.selectByExample(example);
            PageInfo pageInfo = new PageInfo(mchtBaseInfoList);
            PageHelper.clearPage();

            // 转vo返回
            List<MchtBaseInfoVo> mchtBaseInfoVos = mchtBaseInfoList.stream().map(ele -> {
                MchtBaseInfoVo mchtBaseInfoVo = new MchtBaseInfoVo();
                BeanUtils.copyProperties(ele, mchtBaseInfoVo);
                return mchtBaseInfoVo;
            }).collect(Collectors.toList());
            pageInfo.setList(mchtBaseInfoVos);
            log.info("完成 查询商户基本信息");
            return pageInfo;
        } catch (Exception e) {
            throw CommonException.create(e, ServerResponse.createByError("查询商户基本信息失败,请联系管理员"));
        }
    }

    /**
     * 更新商户基本信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMchtBaseInfo(MchtBaseInfoUpdateParam param, SecurityAuthority securityAuthority) throws Exception {
        log.info("开始 更新商户基本信息:{}", JSON.toJSONString(param));
        if (mchtBaseInfoDao.selectByPrimaryKey(param.getId()) == null) {
            throw CommonException.create(ServerResponse.createByError("商户基本信息不存在"));
        }

        // 防止横向越权
        if (!UserRoleCheck.isSysAdmin(securityAuthority)) {
            List<MchtBaseInfo> mchtBaseInfoList = null;
            try {
                MchtBaseInfo selectParam = new MchtBaseInfo();
                selectParam.setMchtUserId(Long.valueOf(securityAuthority.getSecurityUser().getId()));
                selectParam.setDelFlag(false);
                mchtBaseInfoList = mchtBaseInfoDao.select(selectParam);
            } catch (Exception e) {
                throw CommonException.create(ServerResponse.createByError("根据用户id 查询用户关联的商户基本信息失败,请联系管理员"));
            }
            List<Long> mchtBaseInfoIdList = mchtBaseInfoList.stream().map(ele -> ele.getId()).collect(Collectors.toList());
            if (!mchtBaseInfoIdList.contains(param.getId())) {
                throw CommonException.create(ServerResponse.createByError("非法操作，不能修改不属于自己的商户信息"));
            }
        }

        try {
            MchtBaseInfo mchtBaseInfoUpdate = new MchtBaseInfo();
            BeanUtils.copyProperties(param, mchtBaseInfoUpdate);
            mchtBaseInfoDao.updateByPrimaryKeySelective(mchtBaseInfoUpdate);
            log.info("完成 更新商户基本信息");
        } catch (Exception e) {
            throw CommonException.create(e, ServerResponse.createByError("更新商户基本信息失败，请联系管理员"));
        }
    }

    /**
     * 根据用户id 查询用户关联的商户基本信息
     */
    @Override
    public List<MchtBaseInfoVo> getMchtBaseInfoByUserId(Long userId, SecurityAuthority securityAuthority) throws Exception {
        log.info("开始 根据用户id 查询用户关联的商户基本信息:{}", userId);
        List<MchtBaseInfo> mchtBaseInfoList = null;
        try {
            MchtBaseInfo selectParam = new MchtBaseInfo();
            selectParam.setMchtUserId(userId);
            selectParam.setEnableFlag(true);
            selectParam.setDelFlag(false);
            mchtBaseInfoList = mchtBaseInfoDao.select(selectParam);
        } catch (Exception e) {
            throw CommonException.create(ServerResponse.createByError("根据用户id 查询用户关联的商户基本信息失败,请联系管理员"));
        }

        if (CollectionUtils.isEmpty(mchtBaseInfoList)) {
            throw CommonException.create(ServerResponse.createByError("当前用户没有可用的关联商户信息"));
        }
        // 转vo
        List<MchtBaseInfoVo> mchtBaseInfoVoList = mchtBaseInfoList.stream().map(ele -> {
            MchtBaseInfoVo mchtBaseInfoVo = new MchtBaseInfoVo();
            BeanUtils.copyProperties(ele, mchtBaseInfoVo);
            return mchtBaseInfoVo;
        }).collect(Collectors.toList());

        log.info("完成 根据用户id 查询用户关联的商户基本信息:{}", userId);
        return mchtBaseInfoVoList;
    }

    /**
     * 删除商户信息
     */
    @Override
    public void deletaMchtBaseInfo(Long mchtBaseId, SecurityAuthority securityAuthority) throws Exception {
        log.info("开始 删除商户基本信息:{}", mchtBaseId);
        // 防止横向越权
        if (!UserRoleCheck.isSysAdmin(securityAuthority)) {
            List<MchtBaseInfo> mchtBaseInfoList = null;
            try {
                MchtBaseInfo selectParam = new MchtBaseInfo();
                selectParam.setMchtUserId(Long.valueOf(securityAuthority.getSecurityUser().getId()));
                selectParam.setDelFlag(false);
                mchtBaseInfoList = mchtBaseInfoDao.select(selectParam);
            } catch (Exception e) {
                throw CommonException.create(ServerResponse.createByError("根据用户id 查询用户关联的商户基本信息失败,请联系管理员"));
            }
            List<Long> mchtBaseInfoIdList = mchtBaseInfoList.stream().map(ele -> ele.getId()).collect(Collectors.toList());
            if (!mchtBaseInfoIdList.contains(mchtBaseId)) {
                throw CommonException.create(ServerResponse.createByError("非法操作，不能删除不属于自己的商户信息"));
            }
        }

        try {
            MchtBaseInfo delParam = new MchtBaseInfo();
            delParam.setId(mchtBaseId);
            delParam.setDelFlag(true);
            mchtBaseInfoDao.updateByPrimaryKeySelective(delParam);
            log.info("完成 删除商户基本信息");
        } catch (Exception e) {
            throw CommonException.create(e,ServerResponse.createByError("删除商户基本信息失败,请联系管理员"));
        }
    }

    /**
     * 保存商户的福利配置
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveMchtGiftSettings(MchtGiftSettingsSaveParam param, SecurityAuthority securityAuthority) throws Exception {
        log.info("开始 保存商户的福利配置：{}", JSON.toJSONString(param));
        MchtGiftSettings mchtGiftSettings = mchtGiftSettingsDao.selectByPrimaryKey(param.getId());
        if (mchtGiftSettings == null)
            throw CommonException.create(ServerResponse.createByError("配置信息不存在"));

        // 检查参数
        checkMchtGiftSettingsSaveParam(param);

        // 防止横向越权
        if (!UserRoleCheck.isSysAdmin(securityAuthority)) {
            MchtBaseInfo selectParam = new MchtBaseInfo();
            selectParam.setDelFlag(false);
            selectParam.setMchtUserId(Long.valueOf(securityAuthority.getSecurityUser().getId()));
            List<MchtBaseInfo> mchtBaseInfoList = mchtBaseInfoDao.select(selectParam);
            if (CollectionUtils.isEmpty(mchtBaseInfoList))
                throw CommonException.create(ServerResponse.createByError("当前用户下没有商户信息"));
            List<Long> mchtIdList = mchtBaseInfoList.stream().map(ele -> ele.getId()).collect(Collectors.toList());
            if (!mchtIdList.contains(param.getId()))
                throw CommonException.create(ServerResponse.createByError("非法访问，不能操作不属于自己的福利设置"));
        }

        try {
            // 更新参数
            MchtGiftSettings settingsUpdate = new MchtGiftSettings();

            // 更新属性对拷
            BeanUtils.copyProperties(param, settingsUpdate);

            // 设置id
            settingsUpdate.setId(param.getId());

            // 更新数据
            mchtGiftSettingsDao.updateByPrimaryKeySelective(settingsUpdate);

            log.info("完成 保存商户的福利配置");
        } catch (Exception e) {
            throw CommonException.create(e, ServerResponse.createByError("保存商户的福利配置失败，请联系管理员"));
        }


    }

// 私有方法 ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 检查商户名是否已经存在
     */
    public void checkMerchantNameExist(String mchtName) throws Exception {
        if (StringUtils.isBlank(mchtName)) {
            throw CommonException.create(ServerResponse.createByError("未上传商户名"));
        }
        MchtBaseInfo checkParam = new MchtBaseInfo();
        checkParam.setMchtName(mchtName);
        checkParam.setDelFlag(false);
        if (mchtBaseInfoDao.selectCount(checkParam) > 0) {
            throw CommonException.create(ServerResponse.createByError("商户名:" + mchtName + "已经存在"));
        }
    }

    /**
     * 给商户基本信息添加默认配置
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void initMchtGiftSettings(Long mchtBaseInfoId) throws Exception {
        log.info("开始 给商户基本信息添加默认配置：{}", mchtBaseInfoId);
        MchtBaseInfo mchtBaseInfo = mchtBaseInfoDao.selectByPrimaryKey(mchtBaseInfoId);
        if (mchtBaseInfo == null)
            throw CommonException.create(ServerResponse.createByError("商户基本信息不存在"));
        try {
            MchtGiftSettings mchtGiftSettings = new MchtGiftSettings();
            mchtGiftSettings.setId(idWorker.nextId());
            mchtGiftSettings.setMchtId(mchtBaseInfoId);
            mchtGiftSettings.setConsumeScoreGift("1-1");
            mchtGiftSettings.setBirthdayGiftFlag(false);
            mchtGiftSettings.setBirthdayGiftType(MchtGiftSettings.GiftType.NULL.getCode());
            mchtGiftSettings.setBirthdayGiftScore(0);
            mchtGiftSettings.setBirthdayGiftCouponsTemplateId(null);
            mchtGiftSettings.setMonthGiftFlag(false);
            mchtGiftSettings.setMonthGiftType(MchtGiftSettings.GiftType.NULL.getCode());
            mchtGiftSettings.setMonthGiftScore(0);
            mchtGiftSettings.setMonthGiftCouponsTemplateId(null);
            mchtGiftSettings.setMonthGiftDay(null);
            mchtGiftSettings.setYearGiftFlag(false);
            mchtGiftSettings.setYearGiftType(MchtGiftSettings.GiftType.NULL.getCode());
            mchtGiftSettings.setYearGiftScore(0);
            mchtGiftSettings.setYearGiftCouponsTemplateId(null);
            mchtGiftSettings.setYearGiftMonthDay(null);
            mchtGiftSettings.setCreateTime(mchtBaseInfo.getCreateTime());
            mchtGiftSettings.setCreateBy(mchtBaseInfo.getCreateBy());
        } catch (Exception e) {
            throw CommonException.create(e, ServerResponse.createByError("给商户基本信息添加默认配置失败,请联系管理员"));
        }
    }

    // 检查福利配置是否合法
    private void checkMchtGiftSettingsSaveParam(MchtGiftSettingsSaveParam param) throws Exception {
        // 检测 消费积分奖励
        if (StringUtils.isNotBlank(param.getConsumeScoreGift())) {
            if (param.getConsumeScoreGift().indexOf("-") > -1) {
                throw CommonException.create(ServerResponse.createByError("消费积分奖励配合格式不正确: 消费金额-积分奖励 比如: 1:1"));
            }
        }

        // 检测生日福利配置
        if (param.getBirthdayGiftFlag() == true) {
            if (param.getBirthdayGiftType().equals(MchtGiftSettings.GiftType.NULL.getCode())) {
                throw CommonException.create(ServerResponse.createByError("未设置生日福利类型"));
            }
            if (param.getBirthdayGiftType().equals(MchtGiftSettings.GiftType.COUPON.getCode())) {
                if (param.getBirthdayGiftCouponsTemplateId() == null) {
                    throw CommonException.create(ServerResponse.createByError("生日福利类型为优惠券,但未设置优惠券模板id"));
                }
            }
            if (param.getBirthdayGiftType().equals(MchtGiftSettings.GiftType.SCORE.getCode())) {
                if (param.getBirthdayGiftScore() == null) {
                    throw CommonException.create(ServerResponse.createByError("生日福利类型为积分奖励,但是未设置奖励积分数"));
                }
            }
        }

        // 检查月度福利配置
        if (param.getMonthGiftFlag() == true) {
            if (param.getMonthGiftType().equals(MchtGiftSettings.GiftType.NULL.getCode())) {
                throw CommonException.create(ServerResponse.createByError("未设置月度福利类型"));
            }
            if (param.getMonthGiftType().equals(MchtGiftSettings.GiftType.COUPON.getCode())) {
                if (param.getMonthGiftCouponsTemplateId() == null) {
                    throw CommonException.create(ServerResponse.createByError("月度福利类型为优惠券,但未设置优惠券模板id"));
                }
            }
            if (param.getMonthGiftType().equals(MchtGiftSettings.GiftType.SCORE.getCode())) {
                if (param.getBirthdayGiftScore() == null) {
                    throw CommonException.create(ServerResponse.createByError("月度福利类型为积分奖励,但是未设置奖励积分数"));
                }
            }
        }

        // 检查年度福利配置
        if (param.getYearGiftFlag() == true) {
            if (StringUtils.isBlank(param.getYearGiftMonthDay())) {
                throw CommonException.create(ServerResponse.createByError("未设置年度福利类型"));
            } else {
                try {
                    Date date = DateUtils.parseDate(param.getYearGiftMonthDay(), "MM-dd");
                } catch (Exception e) {
                    throw CommonException.create(e, ServerResponse.createByError("年度福利的发放时间格式不合法: MM-dd"));
                }
            }
            if (param.getYearGiftType().equals(MchtGiftSettings.GiftType.NULL.getCode())) {
                throw CommonException.create(ServerResponse.createByError("未设置年度福利类型"));
            }
            if (param.getYearGiftType().equals(MchtGiftSettings.GiftType.COUPON.getCode())) {
                if (param.getYearGiftCouponsTemplateId() == null) {
                    throw CommonException.create(ServerResponse.createByError("年度福利类型为优惠券,但未设置优惠券模板id"));
                }
            }
            if (param.getYearGiftType().equals(MchtGiftSettings.GiftType.SCORE.getCode())) {
                if (param.getYearGiftScore() == null) {
                    throw CommonException.create(ServerResponse.createByError("年度福利类型为积分奖励,但是未设置奖励积分数"));
                }
            }
        }

    }
}
