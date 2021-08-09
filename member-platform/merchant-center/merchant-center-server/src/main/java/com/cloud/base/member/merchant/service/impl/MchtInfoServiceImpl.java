package com.cloud.base.member.merchant.service.impl;

import com.alibaba.fastjson.JSON;
import com.cloud.base.core.common.exception.CommonException;
import com.cloud.base.core.common.response.ServerResponse;
import com.cloud.base.core.common.util.IdWorker;
import com.cloud.base.core.modules.lh_security.core.entity.SecurityAuthority;
import com.cloud.base.member.common.method.UserRoleCheck;
import com.cloud.base.member.merchant.feign.PropScoreAccountApiClient;
import com.cloud.base.member.merchant.feign.UserCenterCommonApiClient;
import com.cloud.base.member.merchant.repository.dao.MchtAddressDao;
import com.cloud.base.member.merchant.repository.dao.MchtInfoDao;
import com.cloud.base.member.merchant.repository.dao.MchtGiftSettingsDao;
import com.cloud.base.member.merchant.repository.dao.MchtVipUserDao;
import com.cloud.base.member.merchant.repository.entity.MchtAddress;
import com.cloud.base.member.merchant.repository.entity.MchtInfo;
import com.cloud.base.member.merchant.repository.entity.MchtGiftSettings;
import com.cloud.base.member.merchant.repository.entity.MchtVipUser;
import com.cloud.base.member.merchant.service.MchtInfoService;
import com.cloud.base.member.merchant.param.MchtInfoQueryParam;
import com.cloud.base.member.merchant.param.MchtInfoCreateParam;
import com.cloud.base.member.merchant.param.MchtInfoUpdateParam;
import com.cloud.base.member.merchant.param.MchtGiftSettingsSaveParam;
import com.cloud.base.member.merchant.vo.MchtInfoVo;
import com.cloud.base.member.merchant.vo.MchtVipUserVo;
import com.cloud.base.member.property.param.PropScoreAccountCreateParam;
import com.cloud.base.user.param.UserOfMchtQueryParam;
import com.cloud.base.user.vo.SysUserVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.assertj.core.util.Lists;
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
    private MchtInfoDao mchtInfoDao;

    @Autowired
    private MchtAddressDao mchtAddressDao;

    @Autowired
    private MchtVipUserDao mchtVipUserDao;

    @Autowired
    private UserCenterCommonApiClient userCenterCommonApiClient;

    @Autowired
    private PropScoreAccountApiClient propScoreAccountApiClient;


    /**
     * 创建商户基本信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void mchtBaseInfoCreate(MchtInfoCreateParam param, SecurityAuthority securityAuthority) throws Exception {
        log.info("开始 创建商户基本信息:{}", JSON.toJSONString(param));
        // 检查商户名是否已经存在
        checkMerchantNameExist(param.getMchtName());

        // 创建基础信息
        MchtInfo mchtInfo = new MchtInfo();
        try {
            // 属性对拷
            BeanUtils.copyProperties(param, mchtInfo);
            // 设置
            mchtInfo.setId(idWorker.nextId());
            mchtInfo.setEnableFlag(false);
            mchtInfo.setDelFlag(false);
            mchtInfo.setCreateTime(new Date());
            mchtInfo.setCreateBy(Long.valueOf(securityAuthority.getSecurityUser().getId()));
            mchtInfoDao.insertSelective(mchtInfo);

            // 获取到商户地址信息
            MchtAddress selectOneParam = new MchtAddress();
            selectOneParam.setMchtId(mchtInfo.getId());
            MchtAddress mchtAddress = mchtAddressDao.selectOne(selectOneParam);
            if (mchtAddress != null) {
                Long mchtAddressId = mchtAddress.getMchtId();
                BeanUtils.copyProperties(param, mchtAddress);
                mchtAddress.setId(mchtAddressId);
                mchtAddress.setUpdateBy(Long.valueOf(securityAuthority.getSecurityUser().getId()));
                mchtAddress.setUpdateTime(new Date());
                mchtAddressDao.updateByPrimaryKeySelective(mchtAddress);
            } else {
                mchtAddress = new MchtAddress();
                BeanUtils.copyProperties(param, mchtAddress);
                mchtAddress.setId(idWorker.nextId());
                mchtAddress.setUpdateBy(Long.valueOf(securityAuthority.getSecurityUser().getId()));
                mchtAddress.setUpdateTime(new Date());
                mchtAddressDao.insertSelective(mchtAddress);
            }
        } catch (Exception e) {
            throw CommonException.create(e, ServerResponse.createByError("创建商户基本信息失败,请联系管理员"));
        }

        // 初始化福利设置
        initMchtGiftSettings(mchtInfo.getId());
        log.info("完成 创建商户基本信息");
    }

    /**
     * 查询商户基本信息
     */
    @Override
    public PageInfo<MchtInfoVo> queryMchtBaseInfo(MchtInfoQueryParam param) throws Exception {
        log.info("开始查询商户基本信息:{}", JSON.toJSONString(param));
        try {
            List<MchtAddress> mchtAddressesList = Lists.newArrayList();
            if (StringUtils.isNotEmpty(param.getProvinceCode()) || StringUtils.isNotEmpty(param.getCityCode()) || StringUtils.isNotEmpty(param.getAreaCode()) || param.getLatitude() != null || param.getLongitude() != null) {
                Example example = new Example(MchtAddress.class);
                Example.Criteria criteria = example.createCriteria();
                if (StringUtils.isNotEmpty(param.getProvinceCode())) {
                    criteria.andEqualTo("provinceCode", param.getProvinceCode());
                }
                if (StringUtils.isNotEmpty(param.getCityCode())) {
                    criteria.andEqualTo("cityCode", param.getCityCode());
                }
                if (StringUtils.isNotEmpty(param.getAreaCode())) {
                    criteria.andEqualTo("areaCode", param.getAreaCode());
                }
                if (param.getLongitude() != null) {
                    criteria.andEqualTo("longitude", param.getLongitude());
                }
                if (param.getLatitude() != null) {
                    criteria.andEqualTo("latitude", param.getLatitude());
                }
                mchtAddressesList = mchtAddressDao.selectByExample(example);
            }
            Example example = new Example(MchtInfo.class);
            example.setOrderByClause(" create_time desc ");
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("delFalg", false);
            if (CollectionUtils.isNotEmpty(mchtAddressesList)) {
                criteria.andIn("id", mchtAddressesList.stream().map(ele -> ele.getMchtId()).collect(Collectors.toList()));
            }
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
            List<MchtInfo> mchtInfoList = mchtInfoDao.selectByExample(example);
            PageInfo pageInfo = new PageInfo(mchtInfoList);
            PageHelper.clearPage();
            // 转vo返回
            List<MchtInfoVo> mchtInfoVos = mchtInfoList.stream().map(ele -> {
                MchtInfoVo mchtInfoVo = new MchtInfoVo();
                BeanUtils.copyProperties(ele, mchtInfoVo);
                return mchtInfoVo;
            }).collect(Collectors.toList());
            pageInfo.setList(mchtInfoVos);
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
    public void updateMchtBaseInfo(MchtInfoUpdateParam param, SecurityAuthority securityAuthority) throws Exception {
        log.info("开始 更新商户基本信息:{}", JSON.toJSONString(param));
        if (mchtInfoDao.selectByPrimaryKey(param.getId()) == null) {
            throw CommonException.create(ServerResponse.createByError("商户基本信息不存在"));
        }

        // 防止横向越权
        if (!UserRoleCheck.isSysAdmin(securityAuthority)) {
            List<MchtInfo> mchtInfoList = null;
            try {
                MchtInfo selectParam = new MchtInfo();
                selectParam.setMchtUserId(Long.valueOf(securityAuthority.getSecurityUser().getId()));
                selectParam.setDelFlag(false);
                mchtInfoList = mchtInfoDao.select(selectParam);
            } catch (Exception e) {
                throw CommonException.create(ServerResponse.createByError("根据用户id 查询用户关联的商户基本信息失败,请联系管理员"));
            }
            List<Long> mchtBaseInfoIdList = mchtInfoList.stream().map(ele -> ele.getId()).collect(Collectors.toList());
            if (!mchtBaseInfoIdList.contains(param.getId())) {
                throw CommonException.create(ServerResponse.createByError("非法操作，不能修改不属于自己的商户信息"));
            }
        }

        try {
            MchtInfo mchtInfoUpdate = new MchtInfo();
            BeanUtils.copyProperties(param, mchtInfoUpdate);
            mchtInfoDao.updateByPrimaryKeySelective(mchtInfoUpdate);


            // 获取到商户地址信息
            MchtAddress selectOneParam = new MchtAddress();
            selectOneParam.setMchtId(mchtInfoUpdate.getId());
            MchtAddress mchtAddress = mchtAddressDao.selectOne(selectOneParam);
            if (mchtAddress != null) {
                Long mchtAddressId = mchtAddress.getMchtId();
                BeanUtils.copyProperties(param, mchtAddress);
                mchtAddress.setId(mchtAddressId);
                mchtAddress.setUpdateBy(Long.valueOf(securityAuthority.getSecurityUser().getId()));
                mchtAddress.setUpdateTime(new Date());
                mchtAddressDao.updateByPrimaryKeySelective(mchtAddress);
            } else {
                mchtAddress = new MchtAddress();
                BeanUtils.copyProperties(param, mchtAddress);
                mchtAddress.setId(idWorker.nextId());
                mchtAddress.setUpdateBy(Long.valueOf(securityAuthority.getSecurityUser().getId()));
                mchtAddress.setUpdateTime(new Date());
                mchtAddressDao.insertSelective(mchtAddress);
            }

            log.info("完成 更新商户基本信息");
        } catch (Exception e) {
            throw CommonException.create(e, ServerResponse.createByError("更新商户基本信息失败，请联系管理员"));
        }
    }

    /**
     * 根据用户id 查询用户关联的商户基本信息
     */
    @Override
    public List<MchtInfoVo> getMchtBaseInfoByUserId(Long userId, SecurityAuthority securityAuthority) throws Exception {
        log.info("开始 根据用户id 查询用户关联的商户基本信息:{}", userId);
        List<MchtInfo> mchtInfoList = null;
        try {
            MchtInfo selectParam = new MchtInfo();
            selectParam.setMchtUserId(userId);
            selectParam.setEnableFlag(true);
            selectParam.setDelFlag(false);
            mchtInfoList = mchtInfoDao.select(selectParam);
        } catch (Exception e) {
            throw CommonException.create(ServerResponse.createByError("根据用户id 查询用户关联的商户基本信息失败,请联系管理员"));
        }

        if (CollectionUtils.isEmpty(mchtInfoList)) {
            throw CommonException.create(ServerResponse.createByError("当前用户没有可用的关联商户信息"));
        }
        // 转vo
        List<MchtInfoVo> mchtInfoVoList = mchtInfoList.stream().map(ele -> {
            MchtInfoVo mchtInfoVo = new MchtInfoVo();
            BeanUtils.copyProperties(ele, mchtInfoVo);
            return mchtInfoVo;
        }).collect(Collectors.toList());

        log.info("完成 根据用户id 查询用户关联的商户基本信息:{}", userId);
        return mchtInfoVoList;
    }

    /**
     * 删除商户信息
     */
    @Override
    public void deletaMchtBaseInfo(Long mchtBaseId, SecurityAuthority securityAuthority) throws Exception {
        log.info("开始 删除商户基本信息:{}", mchtBaseId);
        // 防止横向越权
        if (!UserRoleCheck.isSysAdmin(securityAuthority)) {
            List<MchtInfo> mchtInfoList = null;
            try {
                MchtInfo selectParam = new MchtInfo();
                selectParam.setMchtUserId(Long.valueOf(securityAuthority.getSecurityUser().getId()));
                selectParam.setDelFlag(false);
                mchtInfoList = mchtInfoDao.select(selectParam);
            } catch (Exception e) {
                throw CommonException.create(ServerResponse.createByError("根据用户id 查询用户关联的商户基本信息失败,请联系管理员"));
            }
            List<Long> mchtBaseInfoIdList = mchtInfoList.stream().map(ele -> ele.getId()).collect(Collectors.toList());
            if (!mchtBaseInfoIdList.contains(mchtBaseId)) {
                throw CommonException.create(ServerResponse.createByError("非法操作，不能删除不属于自己的商户信息"));
            }
        }

        try {
            MchtInfo delParam = new MchtInfo();
            delParam.setId(mchtBaseId);
            delParam.setDelFlag(true);
            mchtInfoDao.updateByPrimaryKeySelective(delParam);
            log.info("完成 删除商户基本信息");
        } catch (Exception e) {
            throw CommonException.create(e, ServerResponse.createByError("删除商户基本信息失败,请联系管理员"));
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
            MchtInfo selectParam = new MchtInfo();
            selectParam.setDelFlag(false);
            selectParam.setMchtUserId(Long.valueOf(securityAuthority.getSecurityUser().getId()));
            List<MchtInfo> mchtInfoList = mchtInfoDao.select(selectParam);
            if (CollectionUtils.isEmpty(mchtInfoList))
                throw CommonException.create(ServerResponse.createByError("当前用户下没有商户信息"));
            List<Long> mchtIdList = mchtInfoList.stream().map(ele -> ele.getId()).collect(Collectors.toList());
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

    /**
     * 获取商户基本信息
     */
    @Override
    public MchtInfoVo getMchtBaseInfoVoById(Long mchtBaseInfoId) throws Exception {
        log.info("开始 获取商户基本信息：{}", mchtBaseInfoId);
        MchtInfo mchtInfo = null;
        try {
            mchtInfo = mchtInfoDao.selectByPrimaryKey(mchtBaseInfoId);
        } catch (Exception e) {
            throw CommonException.create(e, ServerResponse.createByError("获取商户基本信息失败，请联系管理员"));
        }
        if (mchtInfo == null)
            throw CommonException.create(ServerResponse.createByError("商户基本信息不存在"));
        MchtInfoVo mchtInfoVo = new MchtInfoVo();
        BeanUtils.copyProperties(mchtInfo, mchtInfoVo);
        log.info("完成 获取商户基本信息");
        return mchtInfoVo;
    }


    /**
     * 获取当前用户关联商户列表
     */
    @Override
    public List<MchtInfoVo> getMchtInfoOfCurrentUser(SecurityAuthority securityAuthority) throws Exception {
        log.info("开始 获取当前用户关联商户列表：{}", securityAuthority.getSecurityUser().getId());
        try {
            Example example = new Example(MchtInfo.class);
            example.setOrderByClause(" create_time desc ");
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("mchtUserId", securityAuthority.getSecurityUser().getId());
            criteria.andEqualTo("delFlag", false);
            List<MchtInfo> mchtInfos = mchtInfoDao.selectByExample(example);
            List<MchtInfoVo> mchtInfoVoList = Lists.newArrayList();
            if (CollectionUtils.isNotEmpty(mchtInfos)) {
                mchtInfoVoList = mchtInfos.stream().map(ele -> {
                    MchtInfoVo mchtInfoVo = new MchtInfoVo();
                    BeanUtils.copyProperties(ele, mchtInfoVo);
                    return mchtInfoVo;
                }).collect(Collectors.toList());
            }
            log.info("完成 获取当前用户关联商户列表");
            return mchtInfoVoList;
        } catch (Exception e) {
            throw CommonException.create(e, ServerResponse.createByError("获取当前用户关联商户列表失败,请联系管理员"));
        }
    }

    /**
     * 用户加入到商户vip
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void joinToMchtVip(Long mchtId, SecurityAuthority securityAuthority) throws Exception {
        log.info("开始 用户加入到商户vip: mchtId={} userId={}", mchtId, securityAuthority.getSecurityUser().getId());

        // 创建用户积分账号
        PropScoreAccountCreateParam propScoreAccountCreateParam = new PropScoreAccountCreateParam();
        propScoreAccountCreateParam.setMchtId(Long.valueOf(securityAuthority.getSecurityUser().getId()));
        propScoreAccountCreateParam.setUserId(mchtId);
        ServerResponse response = propScoreAccountApiClient.createPropScoreAccount(propScoreAccountCreateParam);
        if (!response.isSuccess()) {
            throw CommonException.create(response);
        }

        try {
            MchtVipUser mchtVipUser = new MchtVipUser();
            mchtVipUser.setId(idWorker.nextId());
            mchtVipUser.setUserId(Long.valueOf(securityAuthority.getSecurityUser().getId()));
            mchtVipUser.setMchtId(mchtId);
            mchtVipUserDao.insertSelective(mchtVipUser);
            log.info("完成 用户加入到商户vip");
        } catch (Exception e) {
            throw CommonException.create(e, ServerResponse.createByError("用户加入到商户vip失败，请联系管理员"));
        }


    }


    /**
     * 获取商户的vip用户列表
     */
    @Override
    public List<MchtVipUserVo> getVipUserOfMcht(Long mchtId) throws Exception {
        log.info("开始 获取商户vip用户列表:mchtId={}", mchtId);
        try {
            MchtVipUser selectParam = new MchtVipUser();
            selectParam.setMchtId(mchtId);
            List<MchtVipUser> vipUserList = mchtVipUserDao.select(selectParam);
            List<MchtVipUserVo> voList = vipUserList.stream().map(ele -> {
                MchtVipUserVo mchtVipUserVo = new MchtVipUserVo();
                BeanUtils.copyProperties(ele, mchtVipUserVo);
                return mchtVipUserVo;
            }).collect(Collectors.toList());
            log.info("完成 获取商户vip用户列表");
            return voList;
        } catch (Exception e) {
            throw CommonException.create(e, ServerResponse.createByError("获取商户vip用户列表失败,请联系管理员"));
        }
    }


    /**
     * 查询商户的会员用户列表
     */
    @Override
    public PageInfo<SysUserVo> getVipUserListOfMcht(UserOfMchtQueryParam param, SecurityAuthority securityAuthority) throws Exception {
        log.info("开始 查询商户的会员用户列表:param={}", JSON.toJSONString(param));

        // 获取到用户关联的商户列表
        List<MchtInfoVo> mchtInfoVoList = getMchtBaseInfoByUserId(Long.valueOf(securityAuthority.getSecurityUser().getId()), securityAuthority);
        if (CollectionUtils.isEmpty(mchtInfoVoList)) {
            throw CommonException.create(ServerResponse.createByError("用户没有关联的商户信息"));
        }
        List<Long> mchtIdList = mchtInfoVoList.stream().map(ele -> ele.getId()).collect(Collectors.toList());
        if (!mchtIdList.contains(param.getMchtId())) {
            throw CommonException.create(ServerResponse.createByError("查询商户主体不在当前用户下"));
        }
        ServerResponse<PageInfo<SysUserVo>> response = userCenterCommonApiClient.getUserVoListOfMcht(param);
        if (!response.isSuccess()) {
            throw CommonException.create(response);
        }
        return response.getData();
    }


// 私有方法 ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 检查商户名是否已经存在
     */
    private void checkMerchantNameExist(String mchtName) throws Exception {
        if (StringUtils.isBlank(mchtName)) {
            throw CommonException.create(ServerResponse.createByError("未上传商户名"));
        }
        MchtInfo checkParam = new MchtInfo();
        checkParam.setMchtName(mchtName);
        checkParam.setDelFlag(false);
        if (mchtInfoDao.selectCount(checkParam) > 0) {
            throw CommonException.create(ServerResponse.createByError("商户名:" + mchtName + "已经存在"));
        }
    }

    /**
     * 给商户基本信息添加默认配置
     */
    private void initMchtGiftSettings(Long mchtBaseInfoId) throws Exception {
        log.info("开始 给商户基本信息添加默认配置：{}", mchtBaseInfoId);
        MchtInfo mchtInfo = mchtInfoDao.selectByPrimaryKey(mchtBaseInfoId);
        if (mchtInfo == null)
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
            mchtGiftSettings.setCreateTime(mchtInfo.getCreateTime());
            mchtGiftSettings.setCreateBy(mchtInfo.getCreateBy());
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
