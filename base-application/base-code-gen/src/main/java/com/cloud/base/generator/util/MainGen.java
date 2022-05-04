package com.cloud.base.generator.util;

import com.cloud.base.common.core.exception.CommonException;
import com.cloud.base.common.core.response.ServerResponse;
import com.cloud.base.common.core.util.ip.StringUtils;
import com.cloud.base.generator.model.dto.TableColumnsQueryResultDto;
import com.cloud.base.generator.model.dto.TableQueryResultDto;
import com.cloud.base.generator.model.param.GeneratorCodeParam;
import org.apache.commons.io.IOUtils;
import org.assertj.core.util.Lists;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author lh0811
 * @date 2022/2/10
 */
public class MainGen {

    private static List<String> tableList = Lists.newArrayList("common_file_attach", "common_region");

    private static String packageName = "com.fbi.psfs.svc.project.repository";

    private static String dbUrl = "jdbc:mysql://49.232.166.94:13306/psfs";
    private static String dbUsername = "root";
    private static String dbPassword = "123456";

    private static String sourceDirectory = "/Users/lh0811/Desktop/tmp.zip";

    private static String[] prefix = new String[]{"t_"};

    public static void main(String[] args) throws Exception {
        File f=new File(sourceDirectory);
        byte[] bytes = generatorCode();
        FileOutputStream fs=new FileOutputStream(f);
        fs.write(bytes);
        fs.close();
    }

    public static byte[] generatorCode() throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);
        for (String tableName : tableList) {
            TableQueryResultDto table = queryTable(tableName);
            //查询列信息
            List<TableColumnsQueryResultDto> columns = queryColumns(tableName);
            //生成代码
            MainGenUtils.generatorCode(prefix,table, packageName, columns, zip);
        }
        IOUtils.closeQuietly(zip);
        return outputStream.toByteArray();
    }

    public static List<TableColumnsQueryResultDto> queryColumns(String tableName) throws Exception {
        if (StringUtils.isBlank(tableName)) {
            throw CommonException.create(ServerResponse.createByError("未上传表名"));
        }

        List<TableColumnsQueryResultDto> resultDtoList = Lists.newArrayList();
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            // 获取数据库连接
            conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
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

    public static TableQueryResultDto queryTable(String tableName) throws Exception {
        if (StringUtils.isBlank(tableName)) {
            throw CommonException.create(ServerResponse.createByError("未上传表名"));
        }
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            // 获取数据库连接
            conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
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


}
