package com.cloud.base.common.core.util;

/**
 * @author lh0811
 * @date 2022/1/24
 */

public class TestSync {


    public static void main(String[] args) {

        String str1 = Md5Util.getMD5Str("aaaa");
        String str2 = Md5Util.getMD5Str("bbbb");
        String str3 = Md5Util.getMD5Str("cccc");
        String str4 = Md5Util.getMD5Str("dddd");

        System.out.println("str1:"+str1);
        System.out.println("str2:"+str2);
        System.out.println("str3:"+str3);
        System.out.println("str4:"+str4);


        System.out.println(0%3);
        System.out.println(1%3);
        System.out.println(2%3);
        System.out.println(3%3);
    }
}
