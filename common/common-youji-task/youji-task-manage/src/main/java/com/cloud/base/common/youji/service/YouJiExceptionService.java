package com.cloud.base.common.youji.service;

import com.cloud.base.common.youji.code.exception.YouJiException;

/**
 * @author lh0811
 * @date 2021/11/23
 */
public interface YouJiExceptionService {


    /**
     * 记录日志
     *
     * @param exception
     */
    void logException(YouJiException exception);
}
