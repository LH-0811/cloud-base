package com.cloud.base.common.xugou.client.service;

import com.cloud.base.common.xugou.core.model.entity.SecurityAuthority;

/**
 * @author lh0811
 * @date 2021/5/24
 */
public interface SecurityClient {
    SecurityAuthority tokenToAuthority(Boolean require) throws Exception;

    SecurityAuthority hasUrl(String url) throws Exception;

    SecurityAuthority hasPermsCode(String permsCode) throws Exception;

    SecurityAuthority hasStaticResPath(String resPath) throws Exception;


    SecurityAuthority tokenToAuthority(Boolean require,SecurityAuthority securityAuthority) throws Exception;

    SecurityAuthority hasUrl(String url,SecurityAuthority securityAuthority) throws Exception;

    SecurityAuthority hasPermsCode(String permsCode,SecurityAuthority securityAuthority) throws Exception;

    SecurityAuthority hasStaticResPath(String resPath,SecurityAuthority securityAuthority) throws Exception;
}
