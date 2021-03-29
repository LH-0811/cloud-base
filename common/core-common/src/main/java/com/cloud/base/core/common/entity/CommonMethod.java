package com.cloud.base.core.common.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

@Slf4j
public class CommonMethod {

    public static String getTrace(Throwable t) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        t.printStackTrace(writer);
        StringBuffer buffer = stringWriter.getBuffer();
        return buffer.toString();
    }

    public static JSONArray listToTree(List objList, String rootKey, String pidKey, String idKey,String childKey) {
        JSONArray treeList = new JSONArray();
        JSONArray sourceList = JSONArray.parseArray(JSON.toJSONString(objList));
        for (Object ele : sourceList) {
            if (((JSONObject) ele).getString(pidKey) == null || ((JSONObject) ele).getString(pidKey).equals(rootKey)) {
                treeList.add(ele);
            }
        }
        findChildren(treeList, sourceList, pidKey, idKey,childKey);
        return treeList;
    }

    private static void findChildren(JSONArray treeList, JSONArray sourceList, String pidKey, String idKey,String childKey) {
        for (Object treeEle : treeList) {
            JSONArray children = new JSONArray();
            for (Object ele : sourceList) {
                if (StringUtils.isNotEmpty(((JSONObject) treeEle).getString(idKey)) && ((JSONObject) treeEle).getString(idKey).equals(((JSONObject) ele).getString(pidKey))) {
                    children.add(ele);
                }
            }
            ((JSONObject) treeEle).put(childKey,children);
            findChildren(children, sourceList, pidKey, idKey,childKey);
        }
    }


}
