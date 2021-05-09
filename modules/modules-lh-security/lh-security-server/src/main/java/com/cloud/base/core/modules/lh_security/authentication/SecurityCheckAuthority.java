package com.cloud.base.core.modules.lh_security.authentication;

/**
 * 权限校验
 *
 * @author lh0811
 * @date 2021/5/9
 */
public interface SecurityCheckAuthority {

    /**
     * 判断是否有url权限
     *
     * @param token
     * @param url
     * @return
     * @throws Exception
     */
    Boolean checkUrl(String token, String url) throws Exception;

    /**
     * 判断是否有permsCode权限
     *
     * @param token
     * @param permsCode
     * @return
     * @throws Exception
     */
    Boolean checkPermsCode(String token, String permsCode) throws Exception;
}
