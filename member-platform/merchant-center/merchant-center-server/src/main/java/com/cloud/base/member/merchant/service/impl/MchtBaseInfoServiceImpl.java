package com.cloud.base.member.merchant.service.impl;

import com.alibaba.fastjson.JSON;
import com.cloud.base.core.common.exception.CommonException;
import com.cloud.base.core.common.response.ServerResponse;
import com.cloud.base.core.common.util.IdWorker;
import com.cloud.base.core.modules.lh_security.core.entity.SecurityAuthority;
import com.cloud.base.member.merchant.repository.dao.MchtBaseInfoDao;
import com.cloud.base.member.merchant.repository.entity.MchtBaseInfo;
import com.cloud.base.member.merchant.service.MchtBaseInfoService;
import com.cloud.base.memeber.merchant.param.MchtBaseInfoQueryParam;
import com.cloud.base.memeber.merchant.param.MchtBaseInfoCreateParam;
import com.cloud.base.memeber.merchant.param.MchtBaseInfoUpdateParam;
import com.cloud.base.memeber.merchant.vo.MchtBaseInfoVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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
public class MchtBaseInfoServiceImpl implements MchtBaseInfoService {

    @Autowired
    private IdWorker idWorker;

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
        try {
            MchtBaseInfo mchtBaseInfo = new MchtBaseInfo();
            // 属性对拷
            BeanUtils.copyProperties(param, mchtBaseInfo);
            // 设置
            mchtBaseInfo.setId(idWorker.nextId());
            mchtBaseInfo.setEnableFlag(false);
            mchtBaseInfo.setCreateTime(new Date());
            mchtBaseInfo.setCreateBy(Long.valueOf(securityAuthority.getSecurityUser().getId()));
            mchtBaseInfoDao.insertSelective(mchtBaseInfo);
            log.info("开始 创建商户基本信息");
        } catch (Exception e) {
            throw CommonException.create(e, ServerResponse.createByError("创建商户基本信息失败,请联系管理员"));
        }
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
     *
     * @param param
     * @param securityAuthority
     * @throws Exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMchtBaseInfo(MchtBaseInfoUpdateParam param, SecurityAuthority securityAuthority) throws Exception {
        log.info("开始 更新商户基本信息:{}", JSON.toJSONString(param));
        if (mchtBaseInfoDao.selectByPrimaryKey(param.getId()) == null) {
            throw CommonException.create(ServerResponse.createByError("商户基本信息不存在"));
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
    public List<MchtBaseInfo> getMchtBaseInfoByUserId(Long userId, SecurityAuthority securityAuthority) throws Exception {
        log.info("开始 根据用户id 查询用户关联的商户基本信息:{}", userId);
        List<MchtBaseInfo> mchtBaseInfoList = null;
        try {
            MchtBaseInfo selectParam = new MchtBaseInfo();
            selectParam.setMchtUserId(userId);
            selectParam.setEnableFlag(true);
            mchtBaseInfoList = mchtBaseInfoDao.select(selectParam);
        } catch (Exception e) {
            throw CommonException.create(ServerResponse.createByError("根据用户id 查询用户关联的商户基本信息失败,请联系管理员"));
        }

        if (CollectionUtils.isEmpty(mchtBaseInfoList)) {
            throw CommonException.create(ServerResponse.createByError("当前用户没有可用的关联商户信息"));
        }
        log.info("完成 根据用户id 查询用户关联的商户基本信息:{}", userId);
        return mchtBaseInfoList;
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
        if (mchtBaseInfoDao.selectCount(checkParam) > 0) {
            throw CommonException.create(ServerResponse.createByError("商户名:" + mchtName + "已经存在"));
        }
    }


}
