/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */

package com.cloud.base.generator.repository.service;

import com.cloud.base.generator.model.dto.TableColumnsQueryResultDto;
import com.cloud.base.generator.model.dto.TableQueryResultDto;
import com.cloud.base.generator.model.param.GeneratorCodeParam;
import com.cloud.base.generator.model.param.TableQueryParam;
import com.cloud.base.generator.repository.dao.GeneratorDao;
import com.cloud.base.generator.util.GenUtils;
import com.cloud.base.common.core.exception.CommonException;
import com.cloud.base.common.core.response.ServerResponse;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.zip.ZipOutputStream;

/**
 * 代码生成器
 */
@Service
public class SysGeneratorService {

    @Autowired
    private GeneratorDao generatorDao;

    @Autowired
    private GenUtils genUtils;

    /**
     * 查询表列表
     *
     * @param param
     * @return
     */
    public List<TableQueryResultDto> queryList(TableQueryParam param) throws Exception {
        List<TableQueryResultDto> tableQueryResultDtos = generatorDao.queryList(param.getTableName());
        return tableQueryResultDtos;
    }

    /**
     * 查询单表信息
     *
     * @param tableName
     * @return
     */
    public TableQueryResultDto queryTable(String tableName) throws Exception {
        if (StringUtils.isBlank(tableName)) {
            throw CommonException.create(ServerResponse.createByError("未上传表名"));
        }
        return generatorDao.queryTable(tableName);
    }

    public List<TableColumnsQueryResultDto> queryColumns(String tableName) throws Exception {
        return generatorDao.queryColumns(tableName);
    }


    public byte[] generatorCode(GeneratorCodeParam param) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);
        for (String tableName : param.getTableList()) {
            //查询表信息
            TableQueryResultDto table = queryTable(tableName);
            //查询列信息
            List<TableColumnsQueryResultDto> columns = queryColumns(tableName);
            //生成代码
            genUtils.generatorCode(table,param.getPackagePath(), columns, zip);
        }

        IOUtils.closeQuietly(zip);
        return outputStream.toByteArray();
    }
}
