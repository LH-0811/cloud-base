package com.cloud.base.core.common.exception;


import com.cloud.base.core.common.entity.CommonMethod;
import com.cloud.base.core.common.entity.ServerResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;

/**
 * 自定义异常处理
 */
@Slf4j
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ControllerAdvice
public class CommonException extends Exception {

    private Throwable error;

    private ServerResponse serverResponse;

    public static CommonException create(ServerResponse responseEntity) {
        CommonException exception = new CommonException();
        exception.serverResponse = responseEntity;
        return exception;
    }

    public static CommonException create(Throwable error, ServerResponse responseEntity) {
        log.error(CommonMethod.getTrace(error));
        CommonException exception = new CommonException();
        exception.serverResponse = responseEntity;
        exception.error = error;
        return exception;
    }


}
