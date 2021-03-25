package com.cloud.base.example.cloud.storage.service;

import com.cloud.base.example.cloud.storage.param.SubtractionStorageParam;
import com.cloud.base.example.cloud.storage.vo.SubtractionStorageVo;

/**
 * @author lh0811
 * @date 2021/3/25
 */
public interface StorageService {
    // 减库存
    SubtractionStorageVo subtractionStorageVo(SubtractionStorageParam param) throws Exception;
}
