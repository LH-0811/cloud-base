package com.cloud.base.member.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.cloud.base.core.common.exception.CommonException;
import com.cloud.base.core.common.response.ServerResponse;
import com.cloud.base.member.merchant.vo.MchtVipUserVo;
import com.cloud.base.member.user.feign.MchtInfoApiClient;
import com.cloud.base.member.user.param.UserOfMchtQueryParam;
import com.cloud.base.member.user.repository.dao.SysUserDao;
import com.cloud.base.member.user.repository.entity.SysUser;
import com.cloud.base.member.user.service.SysUserCommonService;
import com.cloud.base.member.user.vo.SysUserVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lh0811
 * @date 2021/6/9
 */
@Slf4j
@Service("sysUserCommonService")
public class SysUserCommonServiceImpl implements SysUserCommonService {

    @Autowired
    private SysUserDao sysUserDao;

    @Autowired
    private MchtInfoApiClient mchtInfoApiClient;


    /**
     * 查询商户的会员用户列表
     */
    @Override
    public PageInfo<SysUserVo> getUserVoListOfMcht(UserOfMchtQueryParam param) throws Exception {
        log.info("开始 查询商户会员用户列表:{}", JSON.toJSONString(param));
        ServerResponse<List<MchtVipUserVo>> response = mchtInfoApiClient.getVipUserOfMcht(param.getMchtId());
        if (!response.isSuccess()) {
            throw CommonException.create(response);
        }
        List<MchtVipUserVo> vipUserVoList = response.getData();
        if (CollectionUtils.isEmpty(vipUserVoList)) {
            throw CommonException.create(ServerResponse.createByError("商户下没有会员用户信息"));
        }
        // 获取到所有的会员id
        List<Long> userIdList = vipUserVoList.stream().map(ele -> ele.getUserId()).collect(Collectors.toList());
        try {
            Example example = new Example(SysUser.class);
            example.setOrderByClause(" create_time desc ");
            Example.Criteria criteria = example.createCriteria();
            criteria.andIn("id", userIdList);
            criteria.andEqualTo("delFlag",false);
            if (StringUtils.isNotEmpty(param.getPhone())) {
                criteria.andEqualTo("phone", param.getPhone());
            }
            if (param.getCreateTimeLow() != null) {
                criteria.andGreaterThanOrEqualTo("createTime", param.getCreateTimeLow());
            }
            if (param.getCreateTimeUp() != null) {
                criteria.andLessThanOrEqualTo("createTime", param.getCreateTimeUp());
            }
            PageHelper.startPage(param.getPageNum(), param.getPageSize());
            List<SysUser> sysUsers = sysUserDao.selectByExample(example);
            PageInfo pageInfo = new PageInfo(sysUsers);
            PageHelper.clearPage();
            List<SysUserVo> sysUserVoList = sysUsers.stream().map(ele -> {
                SysUserVo sysUserVo = new SysUserVo();
                BeanUtils.copyProperties(ele, sysUserVo);
                return sysUserVo;
            }).collect(Collectors.toList());
            pageInfo.setList(sysUserVoList);
            log.info("完成 查询商户会员用户列表");
            return pageInfo;
        } catch (Exception e) {
            throw CommonException.create(e, ServerResponse.createByError("查询商户会员用户列表失败,请联系管理员"));
        }
    }


}
