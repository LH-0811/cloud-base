package com.cloud.base.member.property.service;

import com.cloud.base.core.modules.lh_security.core.entity.SecurityAuthority;
import com.cloud.base.member.property.param.PropScoreAccountCreateParam;
import org.springframework.transaction.annotation.Transactional;

/**
 * 资产-积分账号服务
 *
 * @author lh0811
 * @date 2021/6/8
 */
public interface PropScoreAccountService {
    /**
     * 创建 用户积分账户
     */
    void createPropScoreAccount(PropScoreAccountCreateParam param, SecurityAuthority securityAuthority) throws Exception;
}
