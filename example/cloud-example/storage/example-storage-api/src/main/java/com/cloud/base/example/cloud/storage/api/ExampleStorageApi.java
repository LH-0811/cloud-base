package com.cloud.base.example.cloud.storage.api;

import com.cloud.base.core.common.response.ServerResponse;
import com.cloud.base.example.cloud.storage.param.SubtractionStorageParam;
import com.cloud.base.example.cloud.storage.vo.SubtractionStorageVo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface ExampleStorageApi {

    @PostMapping("/storage/subtraction")
    @ApiOperation("扣减库存")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "SubtractionStorageParam", dataTypeClass = SubtractionStorageParam.class, name = "param", value = "参数"),
    })
    ServerResponse<SubtractionStorageVo> subtractionStorage(@RequestBody SubtractionStorageParam param) throws Exception;
}
