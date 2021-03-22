package com.cloud.base.core.modules.logger.adapter.impl;

import com.google.common.collect.Lists;
import com.cloud.base.core.modules.logger.adapter.LhitLoggerRoleInfoByUserAdapter;

import java.util.List;
import java.util.Map;

public class DefaultRoleInfoByUserAdapter implements LhitLoggerRoleInfoByUserAdapter<String, Map> {
    @Override
    public List<String> getRoleInfoByUserInfo(Map user) {
        return Lists.newArrayList("默认角色");
    }
}
