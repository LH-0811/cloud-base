package com.cloud.base.core.common.util;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class ImgBase64Util {

    /**
     * 将图片转换成Base64编码
     *
     * @return
     */
    public static String getImgStr(byte[] imgData) {
        // 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        return "data:image/png;base64," + new String(Base64.encodeBase64(imgData));
    }

    public static void main(String[] args) {

        Student student1 = new Student("1", "1", "1");
        Student student2 = new Student("1", "1", "1");
        Student student3 = new Student("1", "1", "1");


        Room room = new Room("11",Lists.newArrayList(student1,student2,student3));
        String jsonStr = JSON.toJSONString(room);

        jsonStr = removeJsonObjKey(jsonStr, "age");
        Room room1 = JSON.parseObject(jsonStr, Room.class);
        log.info("{}",room1);

    }

    public static String removeJsonObjKey(String jsonStr, String key) {
        for (int index = -1; ; ) {
            index = jsonStr.indexOf("\"" + key);
            if (index == -1) break;
            ArrayList<String> endStrLis = Lists.newArrayList(",", "}", "]");
            List<Integer> endIndexList = Lists.newArrayList();
            for (String endStr : endStrLis) {
                int i = jsonStr.indexOf(endStr, index);
                if (i != -1) {
                    endIndexList.add(i);
                }
            }
            int endIndex = endIndexList.stream().min(Integer::compareTo).get();
            String substring = jsonStr.substring(index, endIndex + 1);
            jsonStr = jsonStr.replace(substring, "");
        }
        return jsonStr;
    }

}
