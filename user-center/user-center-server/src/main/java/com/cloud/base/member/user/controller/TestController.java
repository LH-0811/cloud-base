package com.cloud.base.member.user.controller;

import com.alibaba.fastjson.JSON;
import com.cloud.base.core.common.response.ServerResponse;
import com.cloud.base.core.common.util.ThreadLog;
import com.cloud.base.member.user.dto.DeptUserDto;
import com.cloud.base.member.user.param.SysDeptUserQueryParam;
import com.cloud.base.member.user.param.SysUserCreateParam;
import com.cloud.base.member.user.repository.dao.custom.DeptUserCustomDao;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 测试
 *
 * @author lh0811
 * @date 2021/8/7
 */

@Slf4j
@Api(tags = "用户中心-测试")
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private DeptUserCustomDao deptUserCustomDao;

    @PostMapping("/query/dept_user")
    @ApiOperation("测试查询部门用户")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "SysDeptUserQueryParam", dataTypeClass = SysDeptUserQueryParam.class, name = "param", value = "参数")
    })
    public ServerResponse<PageInfo<DeptUserDto>> selectDeptUser(@RequestBody SysDeptUserQueryParam param) throws Exception {
        ThreadLog.info().input("开始 测试查询部门用户 TestController-selectDeptUser: param="+JSON.toJSONString(param));
        // 查询
        PageHelper.startPage(param.getPageNum(), param.getPageSize());
        List<DeptUserDto> deptUserDtos = deptUserCustomDao.selectDeptUser(param);
        PageInfo pageInfo = new PageInfo(deptUserDtos);
        PageHelper.clearPage();
        // 查询完成
        ThreadLog.info().input("完成 测试查询部门用户 TestController-selectDeptUser");

        // 输出当前线程日志
        ThreadLog.info().output();
        return ServerResponse.createBySuccess("查询成功",pageInfo);
    }

}


















