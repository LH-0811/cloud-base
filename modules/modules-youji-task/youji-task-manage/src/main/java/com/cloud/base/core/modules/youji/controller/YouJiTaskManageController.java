package com.cloud.base.core.modules.youji.controller;

import com.alibaba.fastjson.JSON;
import com.cloud.base.core.common.response.ServerResponse;
import com.cloud.base.core.modules.youji.code.param.*;
import com.cloud.base.core.modules.youji.code.repository.entity.TaskInfo;
import com.cloud.base.core.modules.youji.service.YouJiManageService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.concurrent.ScheduledFuture;

/**
 * @author lh0811
 * @date 2021/11/15
 */
@Slf4j
@Api(tags = "酉鸡-任务管理接口")
@RequestMapping("/youji/task/manage")
@RestController
public class YouJiTaskManageController {

    @Autowired
    private YouJiManageService youJiManageService;


    /**
     * 查询定时任务列表
     *
     * @param param
     * @return
     * @throws Exception
     */
    @PostMapping("/query")
    @ApiOperation("查询定时任务列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "YouJiTaskInfoQueryParam", dataTypeClass = YouJiTaskInfoQueryParam.class, name = "param", value = "参数")
    })
    public ServerResponse<PageInfo<TaskInfo>> queryTask(@Valid @RequestBody YouJiTaskInfoQueryParam param) throws Exception {
        log.info("[酉鸡 定时任务管理接口] 查询定时任务列表 :param={}", JSON.toJSONString(param));
        return ServerResponse.createBySuccess("查询成功", youJiManageService.queryTask(param));
    }

    /**
     * 更新定时任务基本信息
     *
     * @param param
     * @throws Exception
     */
    @PostMapping("/update")
    @ApiOperation("更新定时任务基本信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "YouJiTaskInfoBaseInfoUpdateParam", dataTypeClass = YouJiTaskInfoBaseInfoUpdateParam.class, name = "param", value = "参数")
    })
    public ServerResponse updateTask(@Valid @RequestBody YouJiTaskInfoBaseInfoUpdateParam param) throws Exception {
        log.info("[酉鸡 定时任务管理接口] 更新定时任务信息 :param={}", JSON.toJSONString(param));
        youJiManageService.updateTask(param);
        return ServerResponse.createBySuccess("更新成功");
    }

    /**
     * 更新定时任务执行计划
     *
     * @param param
     * @throws Exception
     */
    @PostMapping("/update/cron")
    @ApiOperation("更新定时任务执行计划")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "YouJiTaskInfoCronUpdateParam", dataTypeClass = YouJiTaskInfoCronUpdateParam.class, name = "param", value = "参数")
    })
    public ServerResponse changeCron(@Valid @RequestBody YouJiTaskInfoCronUpdateParam param) throws Exception {
        log.info("[酉鸡 定时任务管理接口] 更新定时任务执行计划 :param={}", JSON.toJSONString(param));
        youJiManageService.changeCron(param);
        return ServerResponse.createBySuccess("更新成功");
    }


    /**
     * 修改定时任务停止 和 启动
     *
     * @param param
     * @throws Exception
     */
    @PostMapping("/update/enable")
    @ApiOperation("修改定时任务停止 和 启动")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "YouJiTaskInfoEnableUpdateParam", dataTypeClass = YouJiTaskInfoEnableUpdateParam.class, name = "param", value = "参数")
    })
    public ServerResponse changeTaskEnable(@Valid @RequestBody YouJiTaskInfoEnableUpdateParam param) throws Exception {
        log.info("[酉鸡 定时任务管理接口] 更新定时任务是否可用 :param={}", JSON.toJSONString(param));
        youJiManageService.changeTaskEnable(param);
        return ServerResponse.createBySuccess("更新成功");
    }


}
