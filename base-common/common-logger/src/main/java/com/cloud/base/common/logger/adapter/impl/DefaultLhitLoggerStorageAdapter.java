package com.cloud.base.common.logger.adapter.impl;

import com.alibaba.fastjson.JSONObject;
import com.cloud.base.common.logger.adapter.LhitLoggerStorageAdapter;
import com.cloud.base.common.logger.entity.LhitLoggerEntity;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultLhitLoggerStorageAdapter implements LhitLoggerStorageAdapter {

    @Override
    public void storageLogger(LhitLoggerEntity lhitLoggerEntity) {
        log.info("默认日志存储:{}", JSONObject.toJSONString(lhitLoggerEntity));
    }


}
