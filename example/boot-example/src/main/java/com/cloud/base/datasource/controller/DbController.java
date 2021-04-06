package com.cloud.base.datasource.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cloud.base.core.common.entity.ServerResponse;
import com.cloud.base.datasource.param.PeopleCreateParam;
import com.cloud.base.datasource.param.PeopleQueryParam;
import com.cloud.base.datasource.pojo.People;
import com.cloud.base.datasource.service.PeopleService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author lh0811
 * @date 2020/12/9
 */
@Slf4j
@RestController
@RequestMapping("/db")
@Api(tags = "测试多数据源")
public class DbController {


    @Autowired
    private PeopleService peopleService;


    @GetMapping("/test/{hello}")
    public String test(@PathVariable(value = "hello") String hello) throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("hello", hello);
        return jsonObject.toJSONString();
    }


    /**
     * 创建人员信息
     *
     * @param param
     * @throws Exception
     */
    @PostMapping("/people/create")
    @ApiOperation("创建人员信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "PeopleCreateParam", name = "param", value = "参数")
    })
    public ServerResponse peopleCreate(@Validated @RequestBody PeopleCreateParam param) throws Exception {
        log.info(">>>> 进入 TestController.peopleCreate:{}", JSON.toJSONString(param));
        peopleService.peopleCreate(param);
        log.info(">>>> 完成 TestController.peopleCreate");
        return ServerResponse.createBySuccess("创建成功");
    }

    /**
     * 查询人员信息
     *
     * @param param
     * @return
     * @throws Exception
     */
    @PostMapping("/people/query")
    @ApiOperation("查询人员信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "PeopleQueryParam", name = "param", value = "参数")
    })
    public ServerResponse<PageInfo<People>> peopleQuery(@RequestBody PeopleQueryParam param) throws Exception {
        log.info(">>>> 进入 TestController.peopleQuery:{}", JSON.toJSONString(param));
        PageInfo<People> pageInfo = peopleService.peopleQuery(param);
        log.info(">>>> 完成 TestController.peopleQuery");
        return ServerResponse.createBySuccess("查询成功", pageInfo);
    }
}
