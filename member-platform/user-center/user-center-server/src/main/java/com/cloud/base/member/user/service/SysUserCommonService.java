package com.cloud.base.member.user.service;

import com.cloud.base.member.user.param.UserOfMchtQueryParam;
import com.cloud.base.member.user.vo.SysUserVo;
import com.github.pagehelper.PageInfo;

/**
 * 用户服务通用服务接口
 *
 * @author lh0811
 * @date 2021/6/9
 */
public interface SysUserCommonService {
    /**
     * 查询商户的会员用户列表
     */
    PageInfo<SysUserVo> getUserVoListOfMcht(UserOfMchtQueryParam param) throws Exception;
}
