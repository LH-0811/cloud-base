package com.cloud.base.example.provider.service;


import com.cloud.base.example.provider.param.ProviderQueryParam;

/**
 * 提供方接口
 *
 * @author lh0811
 * @date 2021/3/23
 */
public interface ProviderService {


    String query(ProviderQueryParam param) throws Exception;
}
