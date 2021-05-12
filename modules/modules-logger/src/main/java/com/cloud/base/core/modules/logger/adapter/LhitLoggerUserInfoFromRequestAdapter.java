package com.cloud.base.core.modules.logger.adapter;

import javax.servlet.http.HttpServletRequest;

/**
 * 根据当前request 获取到用户信息的接口
 *
 * @param <T>
 */
public interface LhitLoggerUserInfoFromRequestAdapter<T> {

    /**
     * 获取当前用户的信息
     *
     * @return
     */
    T getUserInfoFromRequest(HttpServletRequest request) throws Exception;
}
