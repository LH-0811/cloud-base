package com.cloud.base.common.logger.adapter.impl;

import com.cloud.base.common.logger.adapter.LhitLoggerRoleInfoByUserAdapter;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;

public class DefaultRoleInfoByUserAdapter implements LhitLoggerRoleInfoByUserAdapter<String, Map> {
    @Override
    public List<String> getRoleInfoByUserInfo(Map user) {
        return Lists.newArrayList("默认角色");
    }
}
