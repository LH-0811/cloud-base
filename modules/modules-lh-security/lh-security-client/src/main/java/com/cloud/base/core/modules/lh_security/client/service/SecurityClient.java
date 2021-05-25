package com.cloud.base.core.modules.lh_security.client.service;

import com.cloud.base.core.modules.lh_security.core.entity.SecurityAuthority;

/**
 * @author lh0811
 * @date 2021/5/24
 */
public interface SecurityClient {
    SecurityAuthority tokenToAuthority() throws Exception;

    SecurityAuthority hasUrl(String url) throws Exception;

    SecurityAuthority hasPermsCode(String permsCode) throws Exception;

    SecurityAuthority hasStaticResPath(String resPath) throws Exception;
}
