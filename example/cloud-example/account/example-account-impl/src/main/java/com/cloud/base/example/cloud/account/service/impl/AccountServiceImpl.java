package com.cloud.base.example.cloud.account.service.impl;

import com.cloud.base.core.common.entity.CommonMethod;
import com.cloud.base.core.common.entity.ServerResponse;
import com.cloud.base.core.common.exception.CommonException;
import com.cloud.base.example.cloud.account.param.AccountSubtractionParam;
import com.cloud.base.example.cloud.account.repository.dao.TAccountDao;
import com.cloud.base.example.cloud.account.repository.entity.TAccount;
import com.cloud.base.example.cloud.account.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author lh0811
 * @date 2021/3/25
 */
@Slf4j
@Service("accountService")
public class AccountServiceImpl implements AccountService {

    @Autowired
    private TAccountDao accountDao;


    // 扣减余额
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void accountSubtraction(AccountSubtractionParam param) throws Exception {
        log.info("开始 扣减余额");

        // 检查参数有效性
        TAccount account = null;
        try {
            TAccount selectAccount = new TAccount();
            selectAccount.setUserId(param.getUserId());
            account = accountDao.selectOne(selectAccount);
        } catch (Exception e) {
            throw CommonException.create(e, ServerResponse.createByError("系统错误,没有获取到账户信息"));
        }

        if (account == null){
            throw CommonException.create( ServerResponse.createByError("没有获取到账户信息"));
        }

        if (param.getAmt().compareTo(account.getResidue()) > 0){
            throw CommonException.create(ServerResponse.createByError("余额不足"));
        }
        // 扣减余额
        try {
            account.setUsed(account.getUsed().add(param.getAmt()));
            account.setResidue(account.getResidue().subtract(param.getAmt()));
            accountDao.updateByPrimaryKeySelective(account);
        }catch (Exception e){
            throw CommonException.create(e,ServerResponse.createByError("扣减余额失败"));
        }
        log.info("完成 扣减余额");
    }


}
