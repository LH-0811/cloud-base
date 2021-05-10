package com.cloud.base.core.modules.lh_security.server.token.impl;

import com.cloud.base.core.modules.lh_security.core.entity.SecurityAuthority;
import com.cloud.base.core.modules.lh_security.server.token.TokenGenerate;

import java.util.UUID;

/**
 * 默认token生成
 *
 * @author lh0811
 * @date 2021/5/8
 */
public class DefaultTokenGenerate implements TokenGenerate {
    @Override
    public String generate(SecurityAuthority authority) throws Exception {
        return UUID.randomUUID().toString();
    }
}
