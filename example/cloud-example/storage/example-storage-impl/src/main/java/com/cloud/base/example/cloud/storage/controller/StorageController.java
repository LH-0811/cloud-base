package com.cloud.base.example.cloud.storage.controller;


import com.cloud.base.core.common.response.ServerResponse;
import com.cloud.base.example.cloud.storage.param.SubtractionStorageParam;
import com.cloud.base.example.cloud.storage.service.StorageService;
import com.cloud.base.example.cloud.storage.vo.SubtractionStorageVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Api(tags = "库存接口")
@RestController
@RequestMapping("/storage")
public class StorageController {

    @Autowired
    private StorageService storageService;

    @PostMapping("/subtraction")
    @ApiOperation("扣减库存")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "SubtractionStorageParam", dataTypeClass = SubtractionStorageParam.class, name = "param", value = "参数"),
    })
    public ServerResponse<SubtractionStorageVo> subtractionStorage(@Validated @RequestBody SubtractionStorageParam param) throws Exception {
        log.info(">>>>>>>>>>>进入扣减库存接口OrderController.subtractionStorage>>>>>>>>>>>>>>>>>>>>>>");
        SubtractionStorageVo vo = storageService.subtractionStorageVo(param);
        return ServerResponse.createBySuccess("扣减成功",vo);
    }

}
