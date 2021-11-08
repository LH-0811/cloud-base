package com.cloud.base.code.generator.repository.dao;

import com.cloud.base.code.generator.model.dto.TableColumnsQueryResultDto;
import com.cloud.base.code.generator.model.dto.TableQueryResultDto;

import java.util.List;


/**
 * MySQL代码生成器
 *
 * @author Mark sunlightcs@gmail.com
 * @since 2018-07-24
 */
public interface GeneratorDao {



    List<TableQueryResultDto> queryList(String tableName) throws Exception;


    TableQueryResultDto queryTable(String tableName) throws Exception;


    List<TableColumnsQueryResultDto> queryColumns(String tableName) throws Exception;
}
