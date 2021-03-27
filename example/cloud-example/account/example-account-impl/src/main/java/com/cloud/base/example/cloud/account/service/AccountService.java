package com.cloud.base.example.cloud.account.service;

import com.cloud.base.example.cloud.account.param.AccountSubtractionParam;

/**
 * @author lh0811
 * @date 2021/3/25
 */
public interface AccountService {
    // 扣减余额
    void accountSubtraction(AccountSubtractionParam param) throws Exception;
}
