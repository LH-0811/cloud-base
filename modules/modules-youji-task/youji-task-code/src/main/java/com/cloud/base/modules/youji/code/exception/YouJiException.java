package com.cloud.base.modules.youji.code.exception;

import com.cloud.base.modules.youji.code.constant.YouJiConstant;
import com.cloud.base.modules.youji.code.repository.entity.TaskInfo;
import com.cloud.base.modules.youji.code.repository.entity.TaskWorker;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author lh0811
 * @date 2021/11/23
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class YouJiException extends Exception {
    private String code;
    private String errMsg;
    private String descStr;
    private TaskInfo taskInfo;
    private TaskWorker taskWorker;


    public YouJiException(String code, String errMsg, String descStr) {
        this.code = code;
        this.errMsg = errMsg;
        this.descStr = descStr;
    }

    public YouJiException(YouJiConstant.YouJiErrorEnum errorEnum, String descStr) {
        this.code = errorEnum.getCode();
        this.errMsg = errorEnum.getMsg();
        this.descStr = descStr;
    }

    public YouJiException(YouJiConstant.YouJiErrorEnum errorEnum, TaskInfo taskInfo, String descStr) {
        this.code = errorEnum.getCode();
        this.errMsg = errorEnum.getMsg();
        this.taskInfo = taskInfo;
        this.descStr = descStr;
    }

    public YouJiException(YouJiConstant.YouJiErrorEnum errorEnum, TaskInfo taskInfo, TaskWorker taskWorker, String descStr) {
        this.code = errorEnum.getCode();
        this.errMsg = errorEnum.getMsg();
        this.taskInfo = taskInfo;
        this.taskWorker = taskWorker;
        this.descStr = descStr;
    }
}
