package com.cloud.base.common.core.exception;

import com.alibaba.csp.sentinel.Tracer;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.cloud.base.common.core.entity.CommonMethod;
import com.cloud.base.common.core.response.ServerResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;


@Slf4j
@ControllerAdvice
public class CommonExceptionAdvice {


    @Value("${spring.cloud.sentinel.enabled:false}")
    private Boolean userSentinel;

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
    @ExceptionHandler(value = {FlowException.class})
    public ServerResponse flowException(HttpServletRequest request, FlowException e) {
        if (userSentinel) {
            Tracer.trace(e);
        }
        log.error(CommonMethod.getTrace(e));
        return ServerResponse.createByError(500, "系统繁忙请稍后");
    }

    @ResponseBody
    @ExceptionHandler(value = {Exception.class})
    public ServerResponse globalError(HttpServletRequest request, Exception e) {
        if (userSentinel) {
            Tracer.trace(e);
        }
        // 输出控制台打印日志
        log.error(CommonMethod.getTrace(e));

        // 返回前端提示信息
        return ServerResponse.createByError(500, "未知错误！");
    }

    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ServerResponse throwCustomException(MethodArgumentNotValidException methodArgumentNotValidException){
        // 这个不假sentinel的异常统计 这个不是系统异常
        return ServerResponse.createByError("非法参数",methodArgumentNotValidException.getBindingResult().getFieldError().getDefaultMessage());
    }


}
