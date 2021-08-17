package com.cloud.base.user.controller;

import com.cloud.base.core.common.response.ServerResponse;
import com.cloud.base.core.common.util.thread_log.ThreadLog;
import com.cloud.base.core.modules.lh_security.client.component.annotation.HasUrl;
import com.cloud.base.user.param.SysDeptCreateParam;
import com.cloud.base.user.param.SysPositionCreateParam;
import com.cloud.base.user.param.SysPositionQueryParam;
import com.cloud.base.user.repository.entity.SysPosition;
import com.cloud.base.user.repository.entity.SysUser;
import com.cloud.base.user.service.SysPositionService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @author lh0811
 * @date 2021/8/17
 */
@Slf4j
@Api(tags = "用户中心-岗位管理接口")
@RestController
@RequestMapping("/sys_position")
public class SysPositionController extends BaseController {

    @Autowired
    private SysPositionService sysPositionService;


    /**
     * 创建岗位信息
     */
    @PostMapping("/create")
    @ApiOperation("创建岗位信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = "LHTOKEN", value = "用户token"),
            @ApiImplicitParam(paramType = "body", dataType = "SysPositionCreateParam", dataTypeClass = SysPositionCreateParam.class, name = "param", value = "参数")
    })
    @HasUrl(url = "/sys_position/create")
    public ServerResponse createPosition(@Validated @RequestBody SysPositionCreateParam param, @ApiIgnore SysUser sysUser) throws Exception {
        ThreadLog.info("|-----------------------------------------------|");
        ThreadLog.info("进入 创建岗位信息 接口 : SysPositionController-createPosition ");
        sysPositionService.createPosition(param, sysUser);
        return ServerResponse.createBySuccess("创建成功");
    }

    /**
     * 删除岗位信息
     */
    @DeleteMapping("/delete/{positionId}")
    @ApiOperation("删除岗位信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = "LHTOKEN", value = "用户token"),
            @ApiImplicitParam(paramType = "path", dataType = "Long", dataTypeClass = Long.class, name = "positionId", value = "岗位id")
    })
    @HasUrl(url = "/sys_position/delete/*")
    public ServerResponse deletePosition(@PathVariable(value = "positionId") Long positionId, @ApiIgnore SysUser sysUser) throws Exception {
        ThreadLog.info("|-----------------------------------------------|");
        ThreadLog.info("进入 删除岗位信息 接口 : SysPositionController-deletePosition ");
        sysPositionService.deletePosition(positionId, sysUser);
        return ServerResponse.createBySuccess("删除成功");
    }

    /**
     * 查询岗位信息
     */
    @PostMapping("/query")
    @ApiOperation("查询岗位信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = "LHTOKEN", value = "用户token"),
            @ApiImplicitParam(paramType = "body", dataType = "SysPositionQueryParam", dataTypeClass = SysPositionQueryParam.class, name = "param", value = "参数")
    })
    @HasUrl(url = "/sys_position/query")
    public ServerResponse<PageInfo<SysPosition>> queryPosition(@Validated @RequestBody SysPositionQueryParam param, @ApiIgnore SysUser sysUser) throws Exception {
        ThreadLog.info("|-----------------------------------------------|");
        ThreadLog.info("进入 查询岗位信息 接口 : SysPositionController-queryPosition ");
        PageInfo<SysPosition> sysPositionPageInfo = sysPositionService.queryPosition(param, sysUser);
        return ServerResponse.createBySuccess("删除成功", sysPositionPageInfo);
    }

}
