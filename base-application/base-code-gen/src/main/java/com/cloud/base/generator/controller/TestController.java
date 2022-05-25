package com.cloud.base.generator.controller;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.cloud.base.common.core.exception.CommonException;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.assertj.core.util.Lists;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author lh0811
 * @date 2022/1/21
 */
@Api(tags = "测试")
@Controller
public class TestController {


    @GetMapping("/easy_write")
    public void easyWrite(HttpServletResponse response) throws Exception {



//        ExcelUtils.easyWrite(head(), dataList(), "数据导出","sheet1", response);

//        ExcelUtils.easyWrite(head(), dataList(), "数据导出", "/Users/lh0811/Desktop/test.xlsx");

//        ExcelUtils.<Student>easyWrite("数据导出","sheet1",studentList(),response);
//        ExcelUtils.<Student>easyWrite("sheet1",studentList(),"/Users/lh0811/Desktop/test2.xlsx");


//        ExcelUtils.<Student>easyWriteWithTemplateClass("数据导出","sheet1", "/Users/lh0811/Desktop/template.xlsx",response, studentList());


//        File file2 = ResourceUtils.getFile("classpath:template/template2.xlsx");
//        ExcelUtils.easyWriteWithTemplateClass("数据导出","sheet1", new FileInputStream(file2), response, stuDataList());


//        File file1 = ResourceUtils.getFile("classpath:template/template2.xlsx");
//        ExcelUtils.easyWriteWithTemplateClass("数据导出","sheet1", file1, response, stuDataList());

    }


    private List<List<String>> head() {
        List<List<String>> list = new ArrayList<>();
        List<String> head0 = new ArrayList<>();
        head0.add("姓");
        head0.add("名");
        List<String> head1 = new ArrayList<>();
        head1.add("年龄");
        List<String> head2 = new ArrayList<>();
        head2.add("生日");
        list.add(head0);
        list.add(head1);
        list.add(head2);
        return list;
    }

    private List<List<Object>> dataList() {
        List<List<Object>> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            List<Object> data = new ArrayList<>();
            data.add("张三");
            data.add(25);
            data.add(new Date());
            list.add(data);
        }
        return list;
    }


    private List<Student> studentList() {
        Student student1 = new Student("一", "01", "test01", "80");
        Student student2 = new Student("一", "02", "test02", "82");
        Student student3 = new Student("一", "03", "test03", "83");
        return Lists.newArrayList(student1, student2, student3);
    }

    private List<List<String>> stuDataList() {
        List<String> list1 = Lists.newArrayList("一", "01", "test01", "80");
        List<String> list2 = Lists.newArrayList("一", "02", "test02", "82");
        List<String> list3 = Lists.newArrayList("一", "03", "test03", "83");
        return Lists.newArrayList(list1,list2,list3);
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Student {
        //        @ExcelIgnore
        @ExcelProperty(value = "班级", index = 0)
        private String classNo;

        @ExcelProperty(value = {"学生信息", "学号"}, index = 1)
        private String stuNo;

        @ExcelProperty(value = {"学生信息", "姓名"}, index = 2)
        private String name;

        @ExcelProperty(value = {"学生信息", "得分"}, index = 3)
        private String score;
    }

}


