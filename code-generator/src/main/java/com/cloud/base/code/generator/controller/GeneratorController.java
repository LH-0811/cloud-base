/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */

package com.cloud.base.code.generator.controller;

import com.cloud.base.code.generator.model.dto.TableQueryResultDto;
import com.cloud.base.code.generator.model.param.GeneratorCodeParam;
import com.cloud.base.code.generator.model.param.SetDataBaseParam;
import com.cloud.base.code.generator.model.param.TableQueryParam;
import com.cloud.base.code.generator.properties.DataBaseProperties;
import com.cloud.base.code.generator.repository.service.SysGeneratorService;
import com.cloud.base.core.common.response.ServerResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.Getter;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * 代码生成器
 *
 * @author Mark sunlightcs@gmail.com
 */
@Api(tags = "代码生成接口")
@Controller
@RestController("/generator")
public class GeneratorController {
    @Autowired
    private SysGeneratorService sysGeneratorService;

    @Autowired
    private DataBaseProperties dataBaseProperties;

    @GetMapping("/db_info/get")
    @ApiOperation("获取当前数据库连接信息")
    private ServerResponse getDbInfo() {
        return ServerResponse.createBySuccess(dataBaseProperties);
    }


    @PostMapping("/db_info/set")
    @ApiOperation("设置当前数据库连接信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = "LHTOKEN", value = "用户token"),
            @ApiImplicitParam(paramType = "body", dataType = "SetDataBaseParam", dataTypeClass = SetDataBaseParam.class, name = "param", value = "参数")
    })
    private ServerResponse setDbInfo(@Valid  @RequestBody SetDataBaseParam param) {
        if (StringUtils.isNotBlank(param.getUrl())) {
            dataBaseProperties.setUrl(param.getUrl());
        }
        if (StringUtils.isNotBlank(param.getUsername())) {
            dataBaseProperties.setUsername(param.getUsername());
        }
        if (StringUtils.isNotBlank(param.getPassword())) {
            dataBaseProperties.setPassword(param.getPassword());
        }
        return ServerResponse.createBySuccess("设置成功");
    }

    /**
     * 列表
     */
    @PostMapping("/list")
    @ApiOperation("获取当前数据库数据表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = "LHTOKEN", value = "用户token"),
            @ApiImplicitParam(paramType = "body", dataType = "TableQueryParam", dataTypeClass = TableQueryParam.class, name = "param", value = "参数")
    })
    public ServerResponse list(@Valid @RequestBody TableQueryParam param) throws Exception {
        List<TableQueryResultDto> tableQueryResultDtos = sysGeneratorService.queryList(param);
        return ServerResponse.createBySuccess(tableQueryResultDtos);
    }

    /**
     * 生成代码
     */
    @PostMapping("/code")
    @ApiOperation("生成代码")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = "LHTOKEN", value = "用户token"),
            @ApiImplicitParam(paramType = "body", dataType = "GeneratorCodeParam", dataTypeClass = GeneratorCodeParam.class, name = "param", value = "要生成的表列表")
    })
    public void code(@Valid @RequestBody GeneratorCodeParam param, HttpServletResponse response) throws Exception {
        byte[] data = sysGeneratorService.generatorCode(param);
        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=\"generator.zip\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");
        IOUtils.write(data, response.getOutputStream());
    }
}
