package com.cloud.base.user.controller;

import com.cloud.base.common.core.constant.CommonConstant;
import com.cloud.base.common.core.response.ServerResponse;
import com.cloud.base.common.xugou.client.component.annotation.HasUrl;
import com.cloud.base.common.xugou.core.model.entity.SecurityAuthority;
import com.cloud.base.user.param.SysPositionCreateParam;
import com.cloud.base.user.param.SysPositionQueryParam;
import com.cloud.base.user.service.SysPositionService;
import com.cloud.base.user.vo.SysPositionVo;
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

import java.util.List;

/**
 * @author lh0811
 * @date 2021/8/17
 */
@Slf4j
@Api(tags = "用户中心-岗位管理接口")
@RestController
@RequestMapping("/sys_positions")
@HasUrl
public class SysPositionController {

    @Autowired
    private SysPositionService sysPositionService;


    /**
     * 创建岗位信息
     */
    @PostMapping("/create")
    @ApiOperation("创建岗位信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = CommonConstant.TokenKey, value = "用户token"),
            @ApiImplicitParam(paramType = "body", dataType = "SysPositionCreateParam", dataTypeClass = SysPositionCreateParam.class, name = "param", value = "参数")
    })
    public ServerResponse createPosition(@Validated @RequestBody SysPositionCreateParam param, @ApiIgnore SecurityAuthority securityAuthority) throws Exception {
        log.info("|-----------------------------------------------|");
        log.info("进入 创建岗位信息 接口 : SysPositionController-createPosition ");
        sysPositionService.createPosition(param, securityAuthority);
        return ServerResponse.createBySuccess("创建成功");
    }

    /**
     * 删除岗位信息
     */
    @DeleteMapping("/delete/{positionId}")
    @ApiOperation("删除岗位信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = CommonConstant.TokenKey, value = "用户token"),
            @ApiImplicitParam(paramType = "path", dataType = "Long", dataTypeClass = Long.class, name = "positionId", value = "岗位id")
    })
    public ServerResponse deletePosition(@PathVariable(value = "positionId") Long positionId, @ApiIgnore SecurityAuthority securityAuthority) throws Exception {
        log.info("|-----------------------------------------------|");
        log.info("进入 删除岗位信息 接口 : SysPositionController-deletePosition ");
        sysPositionService.deletePosition(positionId, securityAuthority);
        return ServerResponse.createBySuccess("删除成功");
    }

    /**
     * 查询岗位信息
     */
    @PostMapping("/query")
    @ApiOperation("查询岗位信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = CommonConstant.TokenKey, value = "用户token"),
            @ApiImplicitParam(paramType = "body", dataType = "SysPositionQueryParam", dataTypeClass = SysPositionQueryParam.class, name = "param", value = "参数")
    })
    public ServerResponse<PageInfo<SysPositionVo>> queryPosition(@Validated @RequestBody SysPositionQueryParam param, @ApiIgnore SecurityAuthority securityAuthority) throws Exception {
        log.info("|-----------------------------------------------|");
        log.info("进入 查询岗位信息 接口 : SysPositionController-queryPosition ");
        PageInfo<SysPositionVo> sysPositionPageInfo = sysPositionService.queryPosition(param, securityAuthority);
        return ServerResponse.createBySuccess("获取成功", sysPositionPageInfo);
    }

    /**
     * 查询全部岗位列表
     */
    @GetMapping("/query/all")
    @ApiOperation("查询岗位信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = CommonConstant.TokenKey, value = "用户token"),
            @ApiImplicitParam(paramType = "body", dataType = "SysPositionQueryParam", dataTypeClass = SysPositionQueryParam.class, name = "param", value = "参数")
    })
    public ServerResponse<List<SysPositionVo>> queryAllPosition(@ApiIgnore SecurityAuthority securityAuthority) throws Exception {
        log.info("|-----------------------------------------------|");
        log.info("进入 查询岗位信息 接口 : SysPositionController-queryPosition ");
        List<SysPositionVo> sysPositionVoList = sysPositionService.queryAllPosition(securityAuthority);
        return ServerResponse.createBySuccess("获取成功", sysPositionVoList);
    }


}
