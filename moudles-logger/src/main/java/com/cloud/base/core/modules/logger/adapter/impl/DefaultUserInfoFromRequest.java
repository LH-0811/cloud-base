package com.cloud.base.core.modules.logger.adapter.impl;

import com.cloud.base.core.modules.logger.adapter.LhitLoggerUserInfoFromRequestAdapter;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class DefaultUserInfoFromRequest implements LhitLoggerUserInfoFromRequestAdapter<Map> {


    @Override
    public Map getUserInfoFromRequest(HttpServletRequest request) {
        Map<String, String> userInfo = new HashMap<>();
        userInfo.put("username", "user");
        userInfo.put("password", "123456");
        userInfo.put("id", "1");
        return userInfo;
    }
}
