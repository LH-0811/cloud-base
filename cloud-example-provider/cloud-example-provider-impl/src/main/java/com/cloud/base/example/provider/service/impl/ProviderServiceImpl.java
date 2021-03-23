package com.cloud.base.example.provider.service.impl;

import com.alibaba.fastjson.JSON;
import com.cloud.base.example.provider.param.ProviderQueryParam;
import com.cloud.base.example.provider.service.ProviderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author lh0811
 * @date 2021/3/23
 */
@Slf4j
@Service("providerService")
public class ProviderServiceImpl implements ProviderService {

    @Override
    public String query(ProviderQueryParam param) throws Exception {
        log.info(">>>开始 查询:{}",JSON.toJSONString(param));
        log.info("<<<完成 查询:{}",JSON.toJSONString(param));
        return JSON.toJSONString(param);
    }

}
