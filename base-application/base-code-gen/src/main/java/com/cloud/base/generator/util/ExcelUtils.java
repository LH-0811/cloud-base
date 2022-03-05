package com.cloud.base.generator.util;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.cloud.base.common.core.exception.CommonException;
import com.cloud.base.common.core.response.ServerResponse;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 1. 自定义头 | 模板追加数据-（指定路径获取模板|InputStream|File）
 * 2. 输出到指定目录 | HttpResponse
 * 3. 输出List<List<Obj>> | Class类描述数据
 *
 * @author lh0811
 * @date 2022/2/22
 */
public class ExcelUtils {


    /**
     * 简单的输出Excel 通过HttpResponse 返回输出流 直接下载到客户端
     *
     * @param headList  标题列表
     *                  这里是个二维数组 eg: 假设传入头 [["姓","名"],["电话"]] 则输出表中
     *                  1  |  姓  |
     *                  2  |  名  |  电话
     *                  电话会合并上下两个单元格
     * @param dataList  数据列表 eg: [["张三","132xxxxxxxx"],["李四","132xxxxxxxx"]]
     * @param fileName  文件名
     * @param sheetName sheet名称
     * @param response  Servlet响应流
     *                  最终输出
     *                  1  |  姓  |
     *                  2  |  名  |  电话
     *                  3  | 张三 |  132xxxxxxxx
     *                  4  | 李四 |  132xxxxxxxx
     * @throws CommonException
     */
    public static void easyWrite(List<List<String>> headList, List<List<Object>> dataList, String fileName, String sheetName, HttpServletResponse response) throws CommonException {
        try (ServletOutputStream outputStream = ExcelUtils.getRespOutputStream(fileName, response)) {
            EasyExcel.write(outputStream).head(headList).sheet(sheetName).doWrite(dataList);
            // 数据刷到前台
            outputStream.flush();
        } catch (Exception e) {
            throw CommonException.create(e, ServerResponse.createByError("数据导出失败"));
        }
    }

    /**
     * 简单的输出Excel 到本地指定目录
     *
     * @param headList  标题列表
     *                  这里是个二维数组 eg: 假设传入头 [["姓","名"],["电话"]] 则输出表中
     *                  1  |  姓  |
     *                  2  |  名  |  电话
     *                  电话会合并上下两个单元格
     * @param dataList  数据列表 eg: [["张三","132xxxxxxxx"],["李四","132xxxxxxxx"]]
     * @param sheetName sheet名称
     * @param filePath  本地文件输出目录: /Users/lh0811/Desktop/test.xlsx
     *                  最终输出
     *                  1  |  姓  |
     *                  2  |  名  |  电话
     *                  3  | 张三 |  132xxxxxxxx
     *                  4  | 李四 |  132xxxxxxxx
     * @throws CommonException
     */
    public static void easyWrite(List<List<String>> headList, List<List<Object>> dataList, String sheetName, String filePath) throws CommonException {
        EasyExcel.write(filePath).head(headList).sheet(sheetName).doWrite(dataList);
    }

    /**
     * 基于Class简单输出Excel，Class需要对象属性需要打上标签。
     *
     * @param sheetName sheet名称
     * @param dataList  数据列表
     * @param filePath  本地输出目录 eg： /Users/lh0811/Desktop/test.xlsx
     * @param <T>       泛型
     */
    public static <T> void easyWrite(String sheetName, List<T> dataList, String filePath) {
        EasyExcel.write(filePath, dataList.get(0).getClass()).sheet(sheetName).doWrite(dataList);
    }

    /**
     * 基于Class简单输出Excel，Class需要对象属性需要打上标签。
     *
     * @param sheetName sheet名称
     * @param dataList  数据列表
     * @param response  Servlet响应流
     * @param <T>       泛型
     */
    public static <T> void easyWrite(String fileName, String sheetName, List<T> dataList, HttpServletResponse response) throws CommonException {
        try (ServletOutputStream outputStream = ExcelUtils.getRespOutputStream(fileName, response)) {
            EasyExcel.write(outputStream, dataList.get(0).getClass()).sheet(sheetName).doWrite(dataList);
            // 数据刷到前台
            outputStream.flush();
        } catch (Exception e) {
            throw CommonException.create(e, ServerResponse.createByError("数据导出失败"));
        }
    }


    /**
     * 向模板中填写数据 逐行追加
     *
     * @param sheetName
     * @param templatePath
     * @param filePath
     * @param dataList
     * @param <T>
     */
    public static <T> void easyWriteWithTemplateClass(String sheetName, String templatePath, String filePath, List<T> dataList) {
        EasyExcel.write(filePath).withTemplate(templatePath).sheet(sheetName).doWrite(dataList);
    }

    public static <T> void easyWriteWithTemplateClass(String fileName, String sheetName, String templatePath, HttpServletResponse response, List<T> dataList) throws Exception {
        try (ServletOutputStream outputStream = ExcelUtils.getRespOutputStream(fileName, response)) {
            EasyExcel.write(outputStream).withTemplate(templatePath).sheet(sheetName).doWrite(dataList);
            // 数据刷到前台
            outputStream.flush();
        } catch (Exception e) {
            throw CommonException.create(e, ServerResponse.createByError("数据导出失败"));
        }
    }

    public static <T> void easyWriteWithTemplateClass(String sheetName, InputStream inputStream, String filePath, List<T> dataList) {
        EasyExcel.write(filePath).withTemplate(inputStream).sheet(sheetName).doWrite(dataList);
    }

    public static <T> void easyWriteWithTemplateClass(String fileName, String sheetName, InputStream inputStream, HttpServletResponse response, List<T> dataList) throws Exception {
        try (ServletOutputStream outputStream = ExcelUtils.getRespOutputStream(fileName, response)) {
            EasyExcel.write(outputStream).withTemplate(inputStream).sheet(sheetName).doWrite(dataList);
            // 数据刷到前台
            outputStream.flush();
        } catch (Exception e) {
            throw CommonException.create(e, ServerResponse.createByError("数据导出失败"));
        }
    }

    public static <T> void easyWriteWithTemplateClass(String sheetName, File file, String filePath, List<T> dataList) {
        EasyExcel.write(filePath).withTemplate(file).sheet(sheetName).doWrite(dataList);
    }

    public static <T> void easyWriteWithTemplateClass(String fileName, String sheetName, File file, HttpServletResponse response, List<T> dataList) throws Exception {
        try (ServletOutputStream outputStream = ExcelUtils.getRespOutputStream(fileName, response)) {
            EasyExcel.write(outputStream).withTemplate(file).sheet(sheetName).doWrite(dataList);
            // 数据刷到前台
            outputStream.flush();
        } catch (Exception e) {
            throw CommonException.create(e, ServerResponse.createByError("数据导出失败"));
        }
    }


    /**
     * 向模板中填写数据 逐行追加
     *
     * @param sheetName    sheet名
     * @param templatePath 模板地址
     * @param filePath     输出文件地址
     * @param dataList     数据
     */
    public static void easyWriteWithTemplateList(String sheetName, String templatePath, String filePath, List<List<String>> dataList) {
        EasyExcel.write(filePath).withTemplate(templatePath).sheet(sheetName).doWrite(dataList);
    }


    /**
     * 直接输出到调用端输出流
     *
     * @param fileName
     * @param response
     * @return
     * @throws Exception
     */
    public static ServletOutputStream getRespOutputStream(String fileName, HttpServletResponse response) throws Exception {
        try {
            fileName = URLEncoder.encode(fileName, "UTF-8");

            response.setContentType("application/vnd.ms-excel");

            response.setCharacterEncoding("utf8");

            response.setHeader("Content-Disposition", "attachment; filename=" + fileName + ".xlsx");

            response.setHeader("Pragma", "public");

            response.setHeader("Cache-Control", "no-store");

            response.addHeader("Cache-Control", "max-age=0");

            return response.getOutputStream();

        } catch (IOException e) {
            throw new Exception("导出excel表格失败!", e);
        }

    }


}
