package com.cloud.base.member.property.service.impl;

import com.cloud.base.core.common.exception.CommonException;
import com.cloud.base.core.common.response.ServerResponse;
import com.cloud.base.core.common.util.IdWorker;
import com.cloud.base.core.modules.lh_security.core.entity.SecurityAuthority;
import com.cloud.base.member.property.param.PropScoreAccountCreateParam;
import com.cloud.base.member.property.repository.dao.PropScoreAccountDao;
import com.cloud.base.member.property.repository.dao.PropScoreHistoryDao;
import com.cloud.base.member.property.repository.entity.PropScoreAccount;
import com.cloud.base.member.property.repository.entity.PropScoreHistory;
import com.cloud.base.member.property.service.PropScoreAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author lh0811
 * @date 2021/6/8
 */
@Slf4j
@Service("propScoreAccountService")
public class PropScoreAccountServiceImpl implements PropScoreAccountService {

    @Autowired
    private PropScoreAccountDao propScoreAccountDao;

    @Autowired
    private PropScoreHistoryDao propScoreHistoryDao;

    @Autowired
    private IdWorker idWorker;


    /**
     * 创建 用户积分账户
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createPropScoreAccount(PropScoreAccountCreateParam param, SecurityAuthority securityAuthority) throws Exception {
        log.info("开始  创建用户积分账户");
        try {
            // 账户基本信息
            PropScoreAccount propScoreAccount = new PropScoreAccount();
            propScoreAccount.setId(idWorker.nextId());
            propScoreAccount.setUserId(param.getUserId());
            propScoreAccount.setMchtId(param.getMchtId());
            propScoreAccount.setScore(0);
            propScoreAccount.setFrozenScore(0);
            propScoreAccount.setEnableFlag(true);
            propScoreAccount.setDelFlag(false);
            propScoreAccount.setCreateBy(Long.valueOf(securityAuthority.getSecurityUser().getId()));
            propScoreAccount.setCreateTime(new Date());
            propScoreAccountDao.insertSelective(propScoreAccount);

            // 账户历史
            PropScoreHistory history = new PropScoreHistory();
            history.setId(idWorker.nextId());
            history.setAccountId(propScoreAccount.getId());
            history.setType(PropScoreHistory.Type.INIT.getCode());
            history.setNotes("");
            history.setScoreChange(0);
            history.setScoreAfter(0);
            history.setScoreBefore(0);
            history.setFrozenScoreChange(0);
            history.setFrozenScoreAfter(0);
            history.setFrozenScoreBefore(0);
            history.setDelFlag(false);
            history.setCreateBy(propScoreAccount.getCreateBy());
            history.setCreateTime(propScoreAccount.getCreateTime());
            propScoreHistoryDao.insertSelective(history);
            log.info("完成  创建用户积分账户");
        } catch (Exception e) {
            throw CommonException.create(e, ServerResponse.createByError("创建用户积分账户失败,请联系管理员"));
        }
    }





}
