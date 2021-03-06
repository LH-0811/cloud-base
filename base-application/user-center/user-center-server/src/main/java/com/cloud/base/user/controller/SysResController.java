package com.cloud.base.user.controller;

import com.cloud.base.common.core.constant.CommonConstant;
import com.cloud.base.common.core.response.ServerResponse;
import com.cloud.base.common.xugou.client.component.annotation.HasUrl;
import com.cloud.base.common.xugou.core.model.entity.SecurityAuthority;
import com.cloud.base.user.constant.UCConstant;
import com.cloud.base.user.param.SysResCreateParam;
import com.cloud.base.user.service.SysResService;
import com.cloud.base.user.vo.SysResSimpleVo;
import com.cloud.base.user.vo.SysResVo;
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
 * 系统管理员接口
 *
 * @author lh0811
 * @date 2021/1/18
 */
@Slf4j
@Api(tags = "用户中心-资源管理接口")
@RestController
@RequestMapping("/sys_res")
@HasUrl
public class SysResController  {

    @Autowired
    private SysResService sysResService;

    /**
     * 创建权限
     */
    @PostMapping("/create")
    @ApiOperation("创建权限")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = CommonConstant.TokenKey, value = "用户token"),
            @ApiImplicitParam(paramType = "body", dataType = "SysResCreateParam", dataTypeClass = SysResCreateParam.class, name = "param", value = "参数")
    })
    public ServerResponse createRes(@Validated @RequestBody SysResCreateParam param, @ApiIgnore SecurityAuthority securityAuthority) throws Exception {
        log.info("|-----------------------------------------------|");
        log.info("进入 创建权限 接口 : SysAdminController-createRes ");
        sysResService.createRes(param, securityAuthority);
        return ServerResponse.createBySuccess("创建成功");
    }

    /**
     * 删除权限信息
     */
    @DeleteMapping("/delete/{resId}")
    @ApiOperation("删除权限信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = CommonConstant.TokenKey, value = "用户token"),
            @ApiImplicitParam(paramType = "body", dataType = "SysResCreateParam", dataTypeClass = SysResCreateParam.class, name = "param", value = "参数")
    })
    public ServerResponse deleteRes(@PathVariable(value = "resId") Long resId, @ApiIgnore SecurityAuthority securityAuthority) throws Exception {
        log.info("|-----------------------------------------------|");
        log.info("进入 删除权限信息 接口 : SysAdminController-deleteRes ");
        sysResService.deleteRes(resId, securityAuthority);
        return ServerResponse.createBySuccess("删除成功");
    }


    /**
     * 获取全部资源树
     */
    @GetMapping("/tree")
    @ApiOperation("获取全部资源树")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = CommonConstant.TokenKey, value = "用户token"),
    })
    public ServerResponse<List<SysResVo>> getAllResTree(@ApiIgnore SecurityAuthority securityAuthority) throws Exception {
        log.info("|-----------------------------------------------|");
        log.info("进入 获取全部资源树 接口 : SysAdminController-getAllResTree ");
        List<SysResVo> allResTree = sysResService.getAllResTree(securityAuthority);
        return ServerResponse.createBySuccess("获取成功", allResTree);
    }

    /**
     * 获取全部资源树
     */
    @GetMapping("/tree/simple")
    @ApiOperation("获取全部资源树(简单)")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = CommonConstant.TokenKey, value = "用户token"),
    })
    public ServerResponse<List<SysResSimpleVo>> getAllResTreeSimple(@ApiIgnore SecurityAuthority securityAuthority) throws Exception {
        log.info("|-----------------------------------------------|");
        log.info("进入 获取全部资源树 接口 : SysAdminController-getAllResTreeSimple ");
        List<SysResSimpleVo> allResTree = sysResService.getAllResTreeSimple();
        return ServerResponse.createBySuccess("获取成功", allResTree);
    }

}
