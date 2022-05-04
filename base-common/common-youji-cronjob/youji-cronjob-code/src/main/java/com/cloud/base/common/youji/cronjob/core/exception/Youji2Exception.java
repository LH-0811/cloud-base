package com.cloud.base.common.youji.cronjob.core.exception;

import com.cloud.base.common.youji.cronjob.core.constant.Youji2Constant;
import com.cloud.base.common.youji.cronjob.core.repository.entity.Youji2TaskInfo;
import com.cloud.base.common.youji.cronjob.core.repository.entity.Youji2TaskWorker;
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
public class Youji2Exception extends Exception {
    private String code;
    private String errMsg;
    private String descStr;
    private Youji2TaskInfo taskInfo;
    private Youji2TaskWorker taskWorker;


    public Youji2Exception(String code, String errMsg, String descStr) {
        this.code = code;
        this.errMsg = errMsg;
        this.descStr = descStr;
    }

    public Youji2Exception(Youji2Constant.YouJiErrorEnum errorEnum, String descStr) {
        this.code = errorEnum.getCode();
        this.errMsg = errorEnum.getMsg();
        this.descStr = descStr;
    }

    public Youji2Exception(Youji2Constant.YouJiErrorEnum errorEnum, Youji2TaskInfo taskInfo, String descStr) {
        this.code = errorEnum.getCode();
        this.errMsg = errorEnum.getMsg();
        this.taskInfo = taskInfo;
        this.descStr = descStr;
    }

    public Youji2Exception(Youji2Constant.YouJiErrorEnum errorEnum, Youji2TaskInfo taskInfo, Youji2TaskWorker taskWorker, String descStr) {
        this.code = errorEnum.getCode();
        this.errMsg = errorEnum.getMsg();
        this.taskInfo = taskInfo;
        this.taskWorker = taskWorker;
        this.descStr = descStr;
    }
}
