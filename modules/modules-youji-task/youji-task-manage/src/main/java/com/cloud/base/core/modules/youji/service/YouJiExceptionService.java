package com.cloud.base.core.modules.youji.service;

import com.cloud.base.core.modules.youji.code.exception.YouJiException;
import org.springframework.stereotype.Component;

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
