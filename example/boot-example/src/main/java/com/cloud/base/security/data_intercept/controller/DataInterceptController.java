package com.cloud.base.security.data_intercept.controller;

import com.cloud.base.core.common.response.ServerResponse;
import com.cloud.base.core.modules.sercurity.defense.annotation.LhitDataIntercept;
import com.cloud.base.security.data_intercept.vo.Room;
import com.cloud.base.security.data_intercept.vo.Student;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lh0811
 * @date 2021/4/15
 */
@RestController
@RequestMapping("/data_intercept")
@Api(tags = "测试数据拦截")
public class DataInterceptController {


    @LhitDataIntercept
    @GetMapping("/student")
    @ApiOperation("测试数据拦截")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = "LHTOKEN", value = "用户token"),
    })
    public ServerResponse<List<Room>> getStudentInfo(@RequestHeader(value = "LHTOKEN", defaultValue = "") String token) throws Exception {
        ArrayList<Student> students = Lists.newArrayList();
        students.add(new Student("1", "1", "1"));
        students.add(new Student("2", "2", "2"));
        students.add(new Student("3", "3", "3"));
        students.add(new Student("3", "3", "3"));
        students.add(new Student("3", "3", "3"));

        Room room2 = new Room("12121",students);
        Room room3 = new Room("122",students);
        Room room4 = new Room("11122",students);

        return ServerResponse.createBySuccess(Lists.newArrayList(room2,room3,room4));
    }


    @LhitDataIntercept
    @GetMapping
    @ApiOperation("测试数据拦截")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = "LHTOKEN", value = "用户token"),
    })
    public ServerResponse<Room> getStudentInfo2(@RequestHeader(value = "LHTOKEN", defaultValue = "") String token) throws Exception {
        ArrayList<Student> students = Lists.newArrayList();
        students.add(new Student("1", "1", "1"));
        students.add(new Student("2", "2", "2"));
        students.add(new Student("3", "3", "3"));
        students.add(new Student("3", "3", "3"));
        students.add(new Student("3", "3", "3"));
        Room room = new Room("12121",students);
        return ServerResponse.createBySuccess(room);
    }

}
