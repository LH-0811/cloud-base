package com.cloud.base.core.common.exception;

import com.alibaba.csp.sentinel.Tracer;
import com.cloud.base.core.common.entity.CommonMethod;
import com.cloud.base.core.common.entity.ServerResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;


@Slf4j
@ControllerAdvice
public class CommonExceptionAdvice {


    private Boolean userSentinel = false;

    @ResponseBody
    @ExceptionHandler(value = {CommonException.class})
    public ServerResponse myError(CommonException e) {
        // 如果有err 则打印堆栈
        if (userSentinel) {
            Tracer.trace(e);
        }
        return e.getServerResponse();
    }


    @ResponseBody
    @ExceptionHandler(value = {Exception.class})
    public ServerResponse globalError(HttpServletRequest request, Exception e) {
        if (userSentinel) {
            Tracer.trace(e);
        }
        log.error(CommonMethod.getTrace(e));
        return ServerResponse.createByError(500, "未知错误！");
    }


}
