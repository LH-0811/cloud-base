package com.cloud.base.core.common.util;

import com.google.common.collect.Lists;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lh0811
 * @date 2021/1/25
 */
public class Md5Util {

    public static String getMD5Str(String str) {
        byte[] digest = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("md5");
            digest = md5.digest(str.getBytes("utf-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //16是表示转换为16进制数
        String md5Str = new BigInteger(1, digest).toString(16);
        return md5Str;
    }

    public static String getMD5Str(String str, String salt) {
        byte[] digest = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("md5");
            digest = md5.digest((str + salt).getBytes("utf-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //16是表示转换为16进制数
        String md5Str = new BigInteger(1, digest).toString(16);
        return md5Str;
    }

    public static void main(String[] args) {

        List<Student> studentList = Lists.newArrayList(
                new Student(1),
                new Student(2),
                new Student(3),
                new Student(1),
                new Student(1)
        );

        Map<Integer, List<Student>> collect = studentList.stream().collect(Collectors.groupingBy(ele -> ele.getClazz()));

        studentList.remove(0);

        System.out.println(studentList.get(2) == collect.get(3).get(0));

        System.out.println("");


    }


//    @Getter
//    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
//    @EqualsAndHashCode
    @Data
    public static class Student {
        private Integer clazz;
    }



}
