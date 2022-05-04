package com.cloud.base.common.youji.cronjob.mgr.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author lh0811
 * @date 2022/4/13
 */
public class Youji2MgrConstant {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public enum CacheKey {
        MgrNodeNum("MgrNodeNum", "管理节点数量"),
        TaskNum("TaskNum", "任务数量")
        ;
        //        1-单节点执行 2-全节点执行
        private String code;
        private String msg;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public enum YoujiTaskConfig {
        SCAN_MGR_TASK_LOCK("scan_mgr_task_lock", "是否有节点正在进行任务扫描"),
        ;
        private String code;
        private String msg;
    }


    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public enum YouJiErrorEnum {

        NOT_EXIST_TASK("1001", "任务不存在"),
        NOT_FIND_WORKER("1002", "未获取到工作节点"),
        FAIL_SEND_TASK_TO_WORKER("1003", "向工作节点发送任务失败"),
        TASK_EXEC_TYPE_ERR("1004", "任务执行方式不合法"),
        WORKER_TASK_RESP_ERR("1005", "工作节点返回响应格式不正确"),
        ;
        private String code;
        private String msg;

    }


}
