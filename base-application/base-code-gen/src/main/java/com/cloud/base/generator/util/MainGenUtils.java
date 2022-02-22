package com.cloud.base.generator.util;

import com.cloud.base.common.core.exception.CommonException;
import com.cloud.base.common.core.response.ServerResponse;
import com.cloud.base.generator.model.dto.TableColumnsQueryResultDto;
import com.cloud.base.generator.model.dto.TableQueryResultDto;
import com.cloud.base.generator.properties.GeneratorProperties;
import com.cloud.base.generator.repository.entity.ColumnEntity;
import com.cloud.base.generator.repository.entity.TableEntity;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 代码生成器   工具类
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年12月19日 下午11:40:24
 */
public class MainGenUtils {


    private static Map<String,String> tableMap = new HashMap<>();

    static {
        tableMap.put("tinyint","Integer");
        tableMap.put("tinyint_1","Boolean");
        tableMap.put("smallint","Integer");
        tableMap.put("mediumint","Integer");
        tableMap.put("int","Integer");
        tableMap.put("integer","Integer");
        tableMap.put("bigint","Long");
        tableMap.put("float","Float");
        tableMap.put("double","Double");
        tableMap.put("decimal","BigDecimal");
        tableMap.put("bit","Boolean");
        tableMap.put("char","String");
        tableMap.put("varchar","String");
        tableMap.put("tinytext","String");
        tableMap.put("text","String");
        tableMap.put("mediumtext","String");
        tableMap.put("longtext","String");
        tableMap.put("date","Date");
        tableMap.put("datetime","Date");
        tableMap.put("timestamp","Date");
        tableMap.put("NUMBER","Integer");
        tableMap.put("INT","Integer");
        tableMap.put("INTEGER","Integer");
        tableMap.put("BINARY_INTEGER","Integer");
        tableMap.put("LONG","String");
        tableMap.put("FLOAT","Float");
        tableMap.put("BINARY_FLOAT","Float");
        tableMap.put("DOUBLE","Double");
        tableMap.put("BINARY_DOUBLE","Double");
        tableMap.put("DECIMAL","BigDecimal");
        tableMap.put("CHAR","String");
        tableMap.put("VARCHAR","String");
        tableMap.put("VARCHAR2","String");
        tableMap.put("NVARCHAR","String");
        tableMap.put("NVARCHAR2","String");
        tableMap.put("CLOB","String");
        tableMap.put("BLOB","String");
        tableMap.put("DATE","Date");
        tableMap.put("DATETIME","Date");
        tableMap.put("TIMESTAMP","Date");
        tableMap.put("int8","Long");
        tableMap.put("int4","Integer");
        tableMap.put("int2","Integer");
        tableMap.put("numeric","BigDecimal");
        tableMap.put("nvarchar","String");
    }

    public static List<String> getTemplates() {
        List<String> templates = new ArrayList<String>();
        templates.add("template/Entity.java.vm");
        templates.add("template/Mapper.java.vm");
        templates.add("template/Dao.java.vm");
        templates.add("template/DaoImpl.java.vm");
        return templates;
    }


    /**
     * 生成代码
     */
    public static void generatorCode(String[] prefix,TableQueryResultDto table, String packageName, List<TableColumnsQueryResultDto> columns, ZipOutputStream zip) throws Exception {
        //配置信息
        boolean hasBigDecimal = false;
        boolean hasList = false;
        //表信息
        TableEntity tableEntity = new TableEntity();
        tableEntity.setTableName(table.getTableName());
        tableEntity.setComments(table.getTableComment());
        //表名转换成Java类名
        String className = tableToJava(tableEntity.getTableName(), prefix);
        tableEntity.setClassName(className);
        tableEntity.setClassname(StringUtils.uncapitalize(className));
        //列信息
        List<ColumnEntity> columsList = new ArrayList<>();
        for (TableColumnsQueryResultDto column : columns) {
            ColumnEntity columnEntity = new ColumnEntity();
            columnEntity.setColumnName(column.getColumnName());
            columnEntity.setDataType(column.getDataType());
            columnEntity.setComments(column.getColumnComment());
            columnEntity.setExtra(column.getExtra());

            //列名转换成Java属性名
            String attrName = removePrefix(columnEntity.getColumnName());
            columnEntity.setAttrName(attrName);
            columnEntity.setAttrname(StringUtils.uncapitalize(attrName));
            //列的数据类型，转换成Java类型
            String attrType = columnDataTypeToJavaDataType(column.getDataType(), column.getColumnType());
            columnEntity.setAttrType(attrType);


            if (!hasBigDecimal && attrType.equals("BigDecimal")) {
                hasBigDecimal = true;
            }
            if (!hasList && "array".equals(columnEntity.getExtra())) {
                hasList = true;
            }
            //是否主键
            if ("PRI".equalsIgnoreCase(column.getColumnKey()) && tableEntity.getPk() == null) {
                tableEntity.setPk(columnEntity);
            }

            columsList.add(columnEntity);
        }
        tableEntity.setColumns(columsList);

        //没主键，则第一个字段为主键
        if (tableEntity.getPk() == null) {
            tableEntity.setPk(tableEntity.getColumns().get(0));
        }

        //设置velocity资源加载器
        Properties prop = new Properties();
        prop.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        Velocity.init(prop);
        //封装模板数据
        Map<String, Object> map = new HashMap<>();
        map.put("tableName", tableEntity.getTableName());
        map.put("comments", tableEntity.getComments());
        map.put("pk", tableEntity.getPk());
        map.put("className", tableEntity.getClassName());
        map.put("classname", tableEntity.getClassname());
        map.put("pathName", tableEntity.getClassname().toLowerCase());
        map.put("columns", tableEntity.getColumns());
        map.put("hasBigDecimal", hasBigDecimal);
        map.put("hasList", hasList);
        map.put("package", packageName);
        map.put("author", "");
        map.put("email", "");
        map.put("datetime", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));

        VelocityContext context = new VelocityContext(map);

        //获取模板列表
        List<String> templates = getTemplates();
        for (String template : templates) {
            //渲染模板
            StringWriter sw = new StringWriter();
            Template tpl = Velocity.getTemplate(template, "UTF-8");
            tpl.merge(context, sw);

            try {
                //添加到zip
                zip.putNextEntry(new ZipEntry(getFileName(template, tableEntity.getClassName(), packageName)));
                IOUtils.write(sw.toString(), zip, "UTF-8");
                IOUtils.closeQuietly(sw);
                zip.closeEntry();
            } catch (IOException e) {
                throw CommonException.create(e, ServerResponse.createByError("渲染模板失败，表名：" + tableEntity.getTableName()));
            }
        }
    }

    private static String columnDataTypeToJavaDataType(String columnDataType, String columnColumnType) {
        String typeStr = null;
        if (StringUtils.equals(columnDataType, "tinyint")) {
            if (StringUtils.equals(columnColumnType, "tinyint(1)")) {
                typeStr = tableMap.get("tinyint_1");
            }
        } else {
            typeStr = tableMap.get(columnDataType);
        }

        // 默认返回Object
        return StringUtils.isBlank(typeStr) ? "Object" : typeStr;
    }


    /**
     * 列名转换成Java属性名
     */
    public static String removePrefix(String columnName) {
        return WordUtils.capitalizeFully(columnName, new char[]{'_'}).replace("_", "");
    }

    /**
     * 表名转换成Java类名
     */
    public static String tableToJava(String tableName, String[] tablePrefixArray) {
        if (null != tablePrefixArray && tablePrefixArray.length > 0) {
            for (String tablePrefix : tablePrefixArray) {
                if (tableName.startsWith(tablePrefix)) {
                    tableName = tableName.replaceFirst(tablePrefix, "");
                }
            }
        }
        return removePrefix(tableName);
    }

    /**
     * 获取文件名
     */
    public static String getFileName(String template, String className, String packageName) {
        String packagePath = "main" + File.separator + "java" + File.separator;
        if (StringUtils.isNotBlank(packageName)) {
            packagePath += packageName.replace(".", File.separator) + File.separator + File.separator;
        }
        if (template.contains("Entity.java.vm")) {
            return packagePath + "entity" + File.separator + className + ".java";
        }

        if (template.contains("Mapper.java.vm")) {
            return packagePath + "dao" + File.separator + "mapper" + File.separator + className + "Mapper.java";
        }

        if (template.contains("Dao.java.vm")) {
            return packagePath + "dao" + File.separator + className + "Dao.java";
        }

        if (template.contains("DaoImpl.java.vm")) {
            return packagePath + "dao" + File.separator + "impl" + File.separator + className + "DaoImpl.java";
        }


        return null;
    }




}
