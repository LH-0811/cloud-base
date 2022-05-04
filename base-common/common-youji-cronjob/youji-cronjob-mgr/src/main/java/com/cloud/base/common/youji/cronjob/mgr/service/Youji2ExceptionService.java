package com.cloud.base.common.youji.cronjob.mgr.service;

import com.cloud.base.common.youji.cronjob.core.exception.Youji2Exception;

/**
 * @author lh0811
 * @date 2021/11/23
 */
public interface Youji2ExceptionService {


    /**
     * 记录日志
     *
     * @param exception
     */
    void logException(Youji2Exception exception);
}
