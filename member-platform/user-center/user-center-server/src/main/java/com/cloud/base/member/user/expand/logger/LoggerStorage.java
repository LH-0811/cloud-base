package com.cloud.base.member.user.expand.logger;

import com.alibaba.fastjson.JSONObject;
import com.cloud.base.core.modules.logger.adapter.LhitLoggerStorageAdapter;
import com.cloud.base.core.modules.logger.entity.LhitLoggerEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author lh0811
 * @date 2021/2/26
 */
@Slf4j
@Component
public class LoggerStorage implements LhitLoggerStorageAdapter {
    @Override
    public void storageLogger(LhitLoggerEntity lhitLoggerEntity) {
        log.info("我的日志:{}", JSONObject.toJSONString(lhitLoggerEntity));
    }
}