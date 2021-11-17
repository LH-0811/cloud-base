package com.cloud.base.core.modules.youji.service;

import com.cloud.base.core.common.response.ServerResponse;
import com.cloud.base.core.modules.youji.code.param.YouJiWorkerReceiveTaskParam;

/**
 * @author lh0811
 * @date 2021/11/17
 */
public interface YouJiWorkerService {
    /**
     * 工作节点完成任务后的处理逻辑
     *
     * @param param
     * @throws Exception
     */
    void finishTask(YouJiWorkerReceiveTaskParam param, ServerResponse serverResponse) throws Exception;
}
