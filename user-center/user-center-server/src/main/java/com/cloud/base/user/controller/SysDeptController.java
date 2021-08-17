package com.cloud.base.user.controller;

import com.cloud.base.core.common.response.ServerResponse;
import com.cloud.base.core.common.util.thread_log.ThreadLog;
import com.cloud.base.core.modules.lh_security.client.component.annotation.HasUrl;
import com.cloud.base.user.dto.DeptUserDto;
import com.cloud.base.user.param.SysDeptCreateParam;
import com.cloud.base.user.param.SysDeptUserQueryParam;
import com.cloud.base.user.repository.entity.SysDept;
import com.cloud.base.user.repository.entity.SysUser;
import com.cloud.base.user.service.SysDeptService;
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
@Api(tags = "用户中心-部门管理接口")
@RestController
@RequestMapping("/sys_dept")
public class SysDeptController extends BaseController {

    @Autowired
    private SysDeptService sysDeptService;

    /**
     * 创建部门 信息
     */
    @PostMapping("/create")
    @ApiOperation("创建部门 信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = "LHTOKEN", value = "用户token"),
            @ApiImplicitParam(paramType = "body", dataType = "SysDeptCreateParam", dataTypeClass = SysDeptCreateParam.class, name = "param", value = "参数")
    })
    @HasUrl(url = "/sys_dept/create")
    public ServerResponse createSysDept(@Validated @RequestBody SysDeptCreateParam param, @ApiIgnore SysUser sysUser) throws Exception {
        ThreadLog.info("|-----------------------------------------------|");
        ThreadLog.info("进入 创建部门信息 接口 : SysDeptController-createSysDept ");
        sysDeptService.createSysDept(param, sysUser);
        return ServerResponse.createBySuccess("创建成功");
    }


    /**
     * 获取部门树
     */
    @GetMapping("/tree/query")
    @ApiOperation("获取部门树")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = "LHTOKEN", value = "用户token"),
    })
    @HasUrl(url = "/sys_dept/query_tree")
    public ServerResponse<List<SysDept>> queryDeptTree(@ApiIgnore SysUser sysUser) throws Exception {
        ThreadLog.info("|-----------------------------------------------|");
        ThreadLog.info("进入 获取部门树 接口 : SysDeptController-queryDeptTree ");
        List<SysDept> sysDeptList = sysDeptService.queryDeptTree(sysUser);
        return ServerResponse.createBySuccess("查询成功", sysDeptList);
    }

    /**
     * 删除部门信息
     */
    @DeleteMapping("/delete/{deptId}")
    @ApiOperation("删除部门信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = "LHTOKEN", value = "用户token"),
            @ApiImplicitParam(paramType = "path", dataType = "Long", dataTypeClass = Long.class, name = "deptId", value = "部门id")
    })
    @HasUrl(url = "/sys_dept/delete/*")
    public ServerResponse deleteSysDept(@PathVariable(value = "deptId") Long deptId, @ApiIgnore SysUser sysUser) throws Exception {
        ThreadLog.info("|-----------------------------------------------|");
        ThreadLog.info("进入 删除部门信息 接口 : SysDeptController-deleteSysDept ");
        sysDeptService.deleteSysDept(deptId, sysUser);
        return ServerResponse.createBySuccess("删除成功");
    }


    /**
     * 获取部门用户信息
     */
    @PostMapping("/user/query")
    @ApiOperation("获取部门用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = "LHTOKEN", value = "用户token"),
            @ApiImplicitParam(paramType = "body", dataType = "SysDeptUserQueryParam", dataTypeClass = SysDeptUserQueryParam.class, name = "param", value = "参数")
    })
    @HasUrl(url = "/sys_dept/user/query")
    public ServerResponse<PageInfo<DeptUserDto>> selectDeptUser(@Validated @RequestBody SysDeptUserQueryParam param, @ApiIgnore SysUser sysUser) throws Exception {
        ThreadLog.info("|-----------------------------------------------|");
        ThreadLog.info("进入 获取部门用户信息 接口 : SysDeptController-selectDeptUser ");
        PageInfo<DeptUserDto> deptUserDtoPageInfo = sysDeptService.selectDeptUser(param, sysUser);
        return ServerResponse.createBySuccess("查询成功", deptUserDtoPageInfo);
    }


}
