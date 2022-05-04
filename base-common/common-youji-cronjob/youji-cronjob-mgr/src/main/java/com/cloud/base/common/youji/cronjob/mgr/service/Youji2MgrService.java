package com.cloud.base.common.youji.cronjob.mgr.service;

import com.cloud.base.common.youji.cronjob.core.exception.Youji2Exception;
import com.cloud.base.common.youji.cronjob.core.param.Youji2TaskRegisterParam;
import com.cloud.base.common.youji.cronjob.core.repository.entity.Youji2TaskInfo;
import com.cloud.base.common.youji.cronjob.core.repository.entity.Youji2TaskMgr;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author lh0811
 * @date 2022/4/13
 */
public interface Youji2MgrService {

    /**
     * 注册任务
     */
    void registerTask(Youji2TaskRegisterParam param) throws Exception;

    /**
     * 获取全部任务 按照任务no 正序 排序
     *
     * @return
     * @throws Exception
     */
    List<Youji2TaskInfo> getAllTaskInfo() throws Exception;

    /**
     * 获取全部 管理节点
     *
     * @return
     * @throws Exception
     */
    List<Youji2TaskMgr> getEnableOnlineTaskMgr() throws Exception;

    /**
     * 根据管理节点的index获取管理节点
     *
     * @param mgrIndex
     * @return
     * @throws Exception
     */
    Youji2TaskMgr getTaskMgrByIndex(Integer mgrIndex) throws Exception;

    /**
     * 保存或者更新当前节点的管理节点
     *
     * @throws Exception
     */
    void saveOrUpdateCurrentNodeTaskMgr() throws Exception ;

    /**
     * 更新主节点的绑定关系
     *
     * @throws Exception
     */
    void updateTaskMgrBindEchoMgrRel() throws Exception;

    /**
     * echo成功后 更新被echo的节点数据
     *
     * @param taskMgr
     * @throws Exception
     */
    void echoSuccess(Youji2TaskMgr taskMgr) throws Exception;

    /**
     * echo失败后 更新被echo的节点数据
     *
     * @param taskMgr
     * @throws Exception
     */
    void echoFail(Youji2TaskMgr taskMgr) throws Exception;

    /**
     * 分发任务到管理节点
     *
     * @throws Exception
     */
    void dispatchTaskToMgr() throws Exception;

    /**
     * 执行定时任务
     *
     * @param taskNo
     * @throws Youji2Exception
     */
    void executeTask(String taskNo) throws Youji2Exception;


    /**
     * 客户端心跳检测
     *
     * @throws Exception
     */
    void echoWorker();
}
