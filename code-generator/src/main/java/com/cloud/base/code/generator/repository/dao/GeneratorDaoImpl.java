package com.cloud.base.code.generator.repository.dao;

import com.cloud.base.code.generator.model.dto.TableColumnsQueryResultDto;
import com.cloud.base.code.generator.model.dto.TableQueryResultDto;
import com.cloud.base.code.generator.properties.DataBaseProperties;
import com.cloud.base.core.common.exception.CommonException;
import com.cloud.base.core.common.response.ServerResponse;
import com.cloud.base.core.common.util.ip.StringUtils;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.List;

/**
 * @author lh0811
 * @date 2021/11/8
 */
@Component
public class GeneratorDaoImpl implements GeneratorDao {

    @Autowired
    private DataBaseProperties dataBaseProperties;

    @Override
    public List<TableQueryResultDto> queryList(String tableName) throws Exception {
        List<TableQueryResultDto> resultDtoList = Lists.newArrayList();
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            // 获取数据库连接
            conn = DriverManager.getConnection(dataBaseProperties.getUrl(), dataBaseProperties.getUsername(), dataBaseProperties.getPassword());
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("select ");
            stringBuffer.append("table_name as tableName, ");
            stringBuffer.append("engine as engine, ");
            stringBuffer.append("table_comment as tableComment, ");
            stringBuffer.append("create_time as createTime ");
            stringBuffer.append("from ");
            stringBuffer.append("information_schema.tables ");
            stringBuffer.append("where ");
            stringBuffer.append("table_schema = (select database()) ");
            if (StringUtils.isNotBlank(tableName)) {
                stringBuffer.append("and table_name like concat('%', ?, '%') ");
            }
            stringBuffer.append("order by create_time desc;");
            preparedStatement = conn.prepareStatement(stringBuffer.toString());
            if (StringUtils.isNotBlank(tableName)) {
                preparedStatement.setString(1, tableName);
            }
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                TableQueryResultDto dto = new TableQueryResultDto();
                dto.setTableName(resultSet.getString("tableName"));
                dto.setEngine(resultSet.getString("engine"));
                dto.setTableComment(resultSet.getString("tableComment"));
                dto.setCreateTime(resultSet.getString("createTime"));
                resultDtoList.add(dto);
            }
            return resultDtoList;
        } catch (Exception e) {
            throw CommonException.create(e, ServerResponse.createByError("查询表数据失败"));
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                resultSet = null;
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public TableQueryResultDto queryTable(String tableName) throws Exception {
        if (StringUtils.isBlank(tableName)) {
            throw CommonException.create(ServerResponse.createByError("未上传表名"));
        }
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            // 获取数据库连接
            conn = DriverManager.getConnection(dataBaseProperties.getUrl(), dataBaseProperties.getUsername(), dataBaseProperties.getPassword());
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("select ");
            stringBuffer.append("table_name as tableName, ");
            stringBuffer.append("engine as engine, ");
            stringBuffer.append("table_comment as tableComment, ");
            stringBuffer.append("create_time as createTime ");
            stringBuffer.append("from ");
            stringBuffer.append("information_schema.tables ");
            stringBuffer.append("where ");
            stringBuffer.append("table_schema = (select database()) ");
            stringBuffer.append("and table_name = ? ");
            stringBuffer.append("order by create_time desc;");
            preparedStatement = conn.prepareStatement(stringBuffer.toString());
            if (StringUtils.isNotBlank(tableName)) {
                preparedStatement.setString(1, tableName);
            }
            resultSet = preparedStatement.executeQuery();
            TableQueryResultDto dto = new TableQueryResultDto();
            while (resultSet.next()) {
                dto.setTableName(resultSet.getString("tableName"));
                dto.setEngine(resultSet.getString("engine"));
                dto.setTableComment(resultSet.getString("tableComment"));
                dto.setCreateTime(resultSet.getString("createTime"));
            }
            return dto;
        } catch (Exception e) {
            throw CommonException.create(e, ServerResponse.createByError("查询表数据失败"));
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public List<TableColumnsQueryResultDto> queryColumns(String tableName) throws Exception {
        if (StringUtils.isBlank(tableName)) {
            throw CommonException.create(ServerResponse.createByError("未上传表名"));
        }

        List<TableColumnsQueryResultDto> resultDtoList = Lists.newArrayList();
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            // 获取数据库连接
            conn = DriverManager.getConnection(dataBaseProperties.getUrl(), dataBaseProperties.getUsername(), dataBaseProperties.getPassword());
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("select ");
            stringBuffer.append("column_name    as columnName, ");
            stringBuffer.append("column_comment as columnComment, ");
            stringBuffer.append("data_type      as dataType, ");
            stringBuffer.append("column_type    as columnType, ");
            stringBuffer.append("column_key     as columnKey, ");
            stringBuffer.append("extra          as extra ");
            stringBuffer.append("from ");
            stringBuffer.append("information_schema.columns ");
            stringBuffer.append("where ");
            stringBuffer.append("table_schema = (select database()) ");
            stringBuffer.append("and table_name = ? ");
            stringBuffer.append("order by ordinal_position;");
            preparedStatement = conn.prepareStatement(stringBuffer.toString());
            preparedStatement.setString(1, tableName);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                TableColumnsQueryResultDto dto = new TableColumnsQueryResultDto();
                dto.setColumnName(resultSet.getString("columnName"));
                dto.setColumnComment(resultSet.getString("columnComment"));
                dto.setDataType(resultSet.getString("dataType"));
                dto.setColumnType(resultSet.getString("columnType"));
                dto.setColumnKey(resultSet.getString("columnKey"));
                dto.setExtra(resultSet.getString("extra"));
                resultDtoList.add(dto);
            }
            return resultDtoList;
        } catch (Exception e) {
            throw CommonException.create(e, ServerResponse.createByError("查询表数据失败"));
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
