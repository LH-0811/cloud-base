package com.cloud.base.common.core.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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
        String str = "{\"custList\":\"[{\\\"custName\\\":\\\"青岛鸿程永泰商贸有限公司\\\",\\\"custIdno\\\":\\\"913702135577416078\\\"}]\",\"time\":\"20220216100235\",\"seqNo\":\"d216c1553a274b13828f4a36f3466fd7\",\"pageSize\":\"1\",\"jointCustIdno\":\"91370211053086012A\",\"svCustIdno\":\"91370211053086012A\",\"channelNo\":\"bp\",\"certSerialNumber\":\"679CF46B9A86E94D40C1751F32F4B37747F2BE16\"}";

        JSONObject jsonObject = JSON.parseObject(str);

        ReverseFactoringCustomerInfoDto reverseFactoringCustomerInfoDto = JSON.parseObject(str, ReverseFactoringCustomerInfoDto.class);

        System.out.printf("");


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
